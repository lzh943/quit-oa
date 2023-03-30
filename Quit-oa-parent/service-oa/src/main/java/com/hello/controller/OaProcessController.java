package com.hello.controller;

import com.hello.result.Result;
import com.hello.service.ProcessService;
import com.hello.vo.process.ProcessQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@Api(tags = "审批流管理")
@RestController
@RequestMapping(value = "/admin/process")
@SuppressWarnings({"unchecked", "rawtypes"})
public class OaProcessController {
    @Resource
    private ProcessService processService;
    @PreAuthorize("hasAuthority('bnt.process.list')")
    @ApiOperation(value = "获取分页列表")
    @GetMapping("{pageNum}/{pageSize}")
    public Result pageQueryProcess(@PathVariable("pageNum") Long pageNum,
                            @PathVariable("pageSize")Long pageSize, ProcessQueryVo processQueryVo){
        return processService.pageQueryProcess(pageNum,pageSize,processQueryVo);
    }
}
