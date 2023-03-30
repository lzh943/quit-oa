package com.hello.controller.api;

import com.hello.model.process.ProcessTemplate;
import com.hello.result.Result;
import com.hello.service.ProcessService;
import com.hello.service.ProcessTemplateService;
import com.hello.service.ProcessTypeService;
import com.hello.service.SysUserService;
import com.hello.vo.process.ApprovalVo;
import com.hello.vo.process.ProcessFormVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@Api(tags = "审批流管理")
@RestController
@RequestMapping(value="/admin/process")
@CrossOrigin  //跨域
public class ProcessController {
    @Autowired
    private ProcessTypeService processTypeService;
    @Autowired
    private ProcessTemplateService processTemplateService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private SysUserService sysUserService;
    @ApiOperation(value = "获取全部审批分类及模板")
    @GetMapping("findProcessType")
    public Result findProcessType() {
        return processTypeService.findProcessType();
    }

    @ApiOperation(value = "获取审批模板")
    @GetMapping("getProcessTemplate/{processTemplateId}")
    public Result get(@PathVariable Long processTemplateId) {
        ProcessTemplate processTemplate = processTemplateService.getById(processTemplateId);
        return Result.ok(processTemplate);
    }

    @ApiOperation(value = "启动流程")
    @PostMapping("/startUp")
    public Result start(@RequestBody ProcessFormVo processFormVo) {
        processService.startUp(processFormVo);
        return Result.ok();
    }
    @ApiOperation(value = "查询待处理任务")
    @GetMapping("/findPending/{page}/{limit}")
    public Result findPending(@PathVariable("page") Long page,@PathVariable("limit") Long limit){
        return processService.findPending(page,limit);
    }
    @ApiOperation(value = "查看审批详情")
    @GetMapping("show/{id}")
    public Result show(@PathVariable Long id) {
        return processService.show(id);
    }
    @ApiOperation(value = "审批")
    @PostMapping("approve")
    public Result approve(@RequestBody ApprovalVo approvalVo) {
        processService.approve(approvalVo);
        return Result.ok();
    }
    @ApiOperation(value = "已处理")
    @GetMapping("/findProcessed/{page}/{limit}")
    public Result findProcessed(@PathVariable("page") Long page, @PathVariable("limit") Long limit){
        return processService.findProcessed(page,limit);
    }
    @ApiOperation(value = "已发起")
    @GetMapping("/findStarted/{page}/{limit}")
    public Result findStarted(@PathVariable("page") Long page,@PathVariable("limit") Long limit){
        return processService.findStarted(page,limit);
    }
    @ApiOperation(value = "获取当前用户基本信息")
    @GetMapping("getCurrentUser")
    public Result getCurrentUser() {
        return sysUserService.getCurrentUser();
    }
}
