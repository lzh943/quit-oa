package com.hello.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.custom.LoginUserInfoHelper;
import com.hello.mapper.ProcessMapper;
import com.hello.model.process.Process;
import com.hello.model.process.ProcessRecord;
import com.hello.model.process.ProcessTemplate;
import com.hello.model.system.SysUser;
import com.hello.result.Result;
import com.hello.service.*;
import com.hello.vo.process.ApprovalVo;
import com.hello.vo.process.ProcessFormVo;
import com.hello.vo.process.ProcessQueryVo;
import com.hello.vo.process.ProcessVo;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

@Service
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements ProcessService {
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private ProcessTemplateService processTemplateService;
    @Autowired
    private ProcessRecordService processRecordService;
    @Autowired
    private MessageService messageService;
    @Override
    public Result pageQueryProcess(Long pageNum, Long pageSize, ProcessQueryVo processQueryVo) {
        Page<ProcessVo> processPage=new Page(pageNum,pageSize);
        Page<ProcessVo> processVoPage = baseMapper.selectPage(processPage, processQueryVo);
        return Result.ok(processVoPage);
    }

    @Override
    public void deployByZip(String processDefinitionPath) {
        // 定义zip输入流
        InputStream inputStream = this
                .getClass()
                .getClassLoader()
                .getResourceAsStream(processDefinitionPath);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        // 流程部署
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
    }

    @Override
    public void startUp(ProcessFormVo processFormVo) {
        SysUser sysUser = sysUserService.getById(LoginUserInfoHelper.getUserId());
        ProcessTemplate template = processTemplateService.getById(processFormVo.getProcessTemplateId());
        Process process=new Process();
        BeanUtils.copyProperties(processFormVo, process);
        String workNo = System.currentTimeMillis() + "";
        process.setProcessCode(workNo);
        process.setUserId(LoginUserInfoHelper.getUserId());
        process.setFormValues(processFormVo.getFormValues());
        process.setTitle(sysUser.getName() + "发起" + template.getName() + "申请");
        process.setStatus(1);
        baseMapper.insert(process);
        //部署流程key
        String processDefinitionKey = template.getProcessDefinitionKey();
        //业务Key
        String businessKey = String.valueOf(process.getId());
        //流程参数
        Map<String, Object> variables = new HashMap<>();
        //将表单数据放入流程实例中
        JSONObject jsonObject = JSON.parseObject(process.getFormValues());
        JSONObject formData = jsonObject.getJSONObject("formData");
        Map<String, Object> map = new HashMap<>();
        //循环转换
        for (Map.Entry<String, Object> entry : formData.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        variables.put("data", map);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
        //业务表关联当前流程实例id
        String processInstanceId = processInstance.getId();
        process.setProcessInstanceId(processInstanceId);
        //计算下一个审批人，可能有多个（并行审批）
        List<Task> taskList = this.getCurrentTaskList(processInstanceId);
        if (!CollectionUtils.isEmpty(taskList)) {
            List<String> assigneeList = new ArrayList<>();
            for(Task task : taskList) {
                SysUser user = sysUserService.getByUsername(task.getAssignee());
                assigneeList.add(user.getName());
                //TODO 推送消息给下一个审批人，后续完善
                messageService.pushPendingMessage(process.getId(),user.getId(),
                        task.getId());
            }
            process.setDescription("等待" + StringUtils.join(assigneeList.toArray(), ",") + "审批");
        }
        baseMapper.updateById(process);
        processRecordService.record(process.getId(), 1, "发起申请");
    }

    @Override
    public Result findPending(Long page, Long limit) {
        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(LoginUserInfoHelper.getUsername())
                .orderByTaskCreateTime().desc();
        int begin= (int) ((page-1)*limit);
        List<Task> taskList = taskQuery.listPage(begin, Math.toIntExact(limit));
        long count = taskQuery.count();
        List<ProcessVo> processVoList=new ArrayList<>();
        for(Task task : taskList){
            // 根据流程的业务ID查询实体并关联
            String processInstanceId = task.getProcessInstanceId();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
            // 业务key获取Process对象
            String businessKey = processInstance.getBusinessKey();
            if (businessKey == null) {
                continue;
            }
            Process process = this.getById(Long.parseLong(businessKey));
            ProcessVo processVo=new ProcessVo();
            BeanUtils.copyProperties(process,processVo );
            processVo.setTaskId(task.getId());
            processVoList.add(processVo);
        }
        Page<ProcessVo> voPage=new Page(page,limit,count);
        voPage.setRecords(processVoList);
        return Result.ok(voPage);
    }

    @Override
    public Result show(Long id) {
        Process process=this.getById(id);
        LambdaQueryWrapper<ProcessRecord> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(ProcessRecord::getProcessId, id);
        List<ProcessRecord> records = processRecordService.list(wrapper);
        ProcessTemplate template = processTemplateService.getById(process.getProcessTemplateId());
        boolean isApprove=false;
        List<Task> taskList = getCurrentTaskList(process.getProcessInstanceId());
        for (Task task : taskList){
            if(task.getAssignee().equals(LoginUserInfoHelper.getUsername())){
                isApprove=true;
            }
        }
        Map<String,Object> map=new HashMap<>();
        map.put("process", process);
        map.put("processRecordList",records);
        map.put("processTemplate", template);
        map.put("isApprove", isApprove);
        return Result.ok(map);
    }

    @Override
    public void approve(ApprovalVo approvalVo) {
        Map<String, Object> variables = taskService.getVariables(approvalVo.getTaskId());
        for(Map.Entry<String,Object> entry :variables.entrySet()){
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
        if(approvalVo.getStatus()==1){
            taskService.complete(approvalVo.getTaskId());
        }else {
            this.endTask(approvalVo.getTaskId());
        }
        String description = approvalVo.getStatus().intValue() == 1 ? "已通过" : "驳回";
        processRecordService.record(approvalVo.getProcessId(), approvalVo.getStatus(),description);
        Process process = getById(approvalVo.getProcessId());
        List<Task> tasks = getCurrentTaskList(process.getProcessInstanceId());
        if(!CollectionUtils.isEmpty(tasks)){
            List<String> assignees=new ArrayList<>();
            for (Task task :tasks){
                String assign=task.getAssignee();
                SysUser sysUser = sysUserService.getByUsername(assign);
                assignees.add(sysUser.getName());
                //TODO 消息推送
                messageService.pushProcessedMessage(process.getId(),process.getUserId(),
                        approvalVo.getStatus());
            }
            process.setDescription("等待" + StringUtils.join(assignees.toArray(), ",") + "审批");
            process.setStatus(1);
         }else {
            if(approvalVo.getStatus().intValue() == 1) {
                process.setDescription("审批完成（同意）");
                process.setStatus(2);
            } else {
                process.setDescription("审批完成（拒绝）");
                process.setStatus(-1);
            }
        }
        this.updateById(process);
    }

    @Override
    public Result findProcessed(Long page, Long limit) {
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(LoginUserInfoHelper.getUsername())
                .finished().orderByTaskCreateTime().desc();
        int begin= (int) ((page-1)*limit);
        List<HistoricTaskInstance> listPage = query.listPage(begin, Math.toIntExact(limit));
        long count = query.count();
        List<ProcessVo> processVoList=new ArrayList<>();
        for(HistoricTaskInstance item : listPage){
            String processInstanceId = item.getProcessInstanceId();
            LambdaQueryWrapper<Process> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(Process::getProcessInstanceId, processInstanceId);
            Process process = getOne(wrapper);
            ProcessVo processVo=new ProcessVo();
            BeanUtils.copyProperties(process, processVo);
            processVoList.add(processVo);
        }
        Page<ProcessVo> voPage=new Page<>(page, limit,count );
        voPage.setRecords(processVoList);
        return Result.ok(voPage);
    }

    @Override
    public Result findStarted(Long page, Long limit) {
        ProcessQueryVo processQueryVo=new ProcessQueryVo();
        processQueryVo.setUserId(LoginUserInfoHelper.getUserId());
        Page<ProcessVo> voPage=new Page<>(page,limit);
        Page<ProcessVo> processVoPage = baseMapper.selectPage(voPage, processQueryVo);
        return Result.ok(processVoPage);
    }

    //驳回任务
    private void endTask(String taskId) {
        //获取当前对象
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        //获取流程定义模型 BpmnModel
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        //获取结束流向节点
        List<EndEvent> endEvents = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class);
        if(CollectionUtils.isEmpty(endEvents)){
            return;
        }
        FlowNode endflowNode = endEvents.get(0);
        //当前流向节点
        FlowNode currentFlowNode=(FlowNode) bpmnModel.getMainProcess()
                .getFlowElement(task.getTaskDefinitionKey());
        //  临时保存当前活动的原始方向
        List originalSequenceFlowList = new ArrayList<>();
        originalSequenceFlowList.addAll(currentFlowNode.getOutgoingFlows());
        //清理当前流动方向
        currentFlowNode.getOutgoingFlows().clear();
        //创建新的流动方向
        SequenceFlow sequenceFlow=new SequenceFlow();
        sequenceFlow.setId("sequenceFlow");
        sequenceFlow.setSourceFlowElement(currentFlowNode);
        sequenceFlow.setTargetFlowElement(endflowNode);
        //当前节点指向新流动方向
        List sequenceFlowList=new ArrayList();
        sequenceFlowList.add(sequenceFlow);
        currentFlowNode.setOutgoingFlows(sequenceFlowList);
        //完成当前任务
        taskService.complete(task.getId());

    }

    //当前流程任务列表
    private List<Task> getCurrentTaskList(String processInstanceId) {
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        return tasks;
    }
}
