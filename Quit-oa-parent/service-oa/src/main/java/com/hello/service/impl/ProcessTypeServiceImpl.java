package com.hello.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.mapper.ProcessTypeMapper;
import com.hello.model.process.ProcessTemplate;
import com.hello.model.process.ProcessType;
import com.hello.result.Result;
import com.hello.service.ProcessTemplateService;
import com.hello.service.ProcessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessTypeServiceImpl extends ServiceImpl<ProcessTypeMapper, ProcessType>
        implements ProcessTypeService {
    @Autowired
    private ProcessTemplateService processTemplateService;
    @Override
    public Result pageQueryType(Long pageNum, Long pageSize) {
        Page<ProcessType> typePage=new Page<>(pageNum, pageSize);
        page(typePage);
        return Result.ok(typePage);
    }

    @Override
    public Result findProcessType() {
        List<ProcessType> processTypes = list();
        for(ProcessType processType :processTypes){
            LambdaQueryWrapper<ProcessTemplate> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(ProcessTemplate::getProcessTypeId, processType.getId());
            List<ProcessTemplate> templates = processTemplateService.list(wrapper);
            processType.setProcessTemplateList(templates);
        }
        return Result.ok(processTypes);
    }
}
