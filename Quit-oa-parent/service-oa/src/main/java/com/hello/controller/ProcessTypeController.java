package com.hello.controller;

import com.hello.model.process.ProcessType;
import com.hello.result.Result;
import com.hello.service.ProcessTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(value = "审批类型", tags = "审批类型")
@RestController
@RequestMapping(value = "/admin/process/processType")
@SuppressWarnings({"unchecked", "rawtypes"})
public class ProcessTypeController {
    @Resource
    private ProcessTypeService processTypeService;
    @ApiOperation(value = "获取全部审批分类")
    @GetMapping("findAll")
    public Result findAll() {
        return Result.ok(processTypeService.list());
    }
    @PreAuthorize("hasAuthority('bnt.processType.list')")
    @ApiOperation(value = "获取分页列表")
    @GetMapping("{pageNum}/{pageSize}")
    public Result pageQueryType(@PathVariable("pageNum") Long pageNum,
                                @PathVariable("pageSize")Long pageSize){
        return processTypeService.pageQueryType(pageNum,pageSize);
    }
    @PreAuthorize("hasAuthority('bnt.processType.list')")
    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        ProcessType processType = processTypeService.getById(id);
        return Result.ok(processType);
    }

    @PreAuthorize("hasAuthority('bnt.processType.add')")
    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody ProcessType processType) {
        processTypeService.save(processType);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.processType.update')")
    @ApiOperation(value = "修改")
    @PutMapping("update")
    public Result updateById(@RequestBody ProcessType processType) {
        processTypeService.updateById(processType);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.processType.remove')")
    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        processTypeService.removeById(id);
        return Result.ok();
    }
}
