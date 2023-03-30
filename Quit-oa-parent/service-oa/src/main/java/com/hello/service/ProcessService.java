package com.hello.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hello.model.process.Process;
import com.hello.result.Result;
import com.hello.vo.process.ApprovalVo;
import com.hello.vo.process.ProcessFormVo;
import com.hello.vo.process.ProcessQueryVo;


public interface ProcessService extends IService<Process> {
    /**
     * 条件分页查询审批申请
     * @param pageNum
     * @param pageSize
     * @param processQueryVo
     * @return
     */
    Result pageQueryProcess(Long pageNum, Long pageSize, ProcessQueryVo processQueryVo);

    /**
     * 部署流程定义
     * @param processDefinitionPath
     */
    void deployByZip(String processDefinitionPath);

    /**
     * 启动流程实例
     * @param processFormVo
     */
    void startUp(ProcessFormVo processFormVo);

    /**
     * 查询待处理的任务
     * @param page
     * @param limit
     * @return
     */
    Result findPending(Long page, Long limit);

    /**
     * 查看审批详情
     * @param id
     * @return
     */
    Result show(Long id);

    /**
     * 审批
     * @param approvalVo
     */
    void approve(ApprovalVo approvalVo);

    /**
     * 查询已处理
     * @param page
     * @param limit
     * @return
     */
    Result findProcessed(Long page, Long limit);

    /**
     * 查询已发起
     * @param page
     * @param limit
     * @return
     */
    Result findStarted(Long page, Long limit);
}
