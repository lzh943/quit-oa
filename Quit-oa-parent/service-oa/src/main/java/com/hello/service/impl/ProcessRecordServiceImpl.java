package com.hello.service.impl;

import com.hello.custom.LoginUserInfoHelper;
import com.hello.mapper.ProcessRecordMapper;
import com.hello.model.process.ProcessRecord;
import com.hello.model.system.SysUser;
import com.hello.service.ProcessRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class ProcessRecordServiceImpl extends ServiceImpl<ProcessRecordMapper, ProcessRecord>
        implements ProcessRecordService {
    @Resource
    SysUserService sysUserService;
    @Override
    public void record(Long processId, Integer status, String description) {
        SysUser sysUser = sysUserService.getById(LoginUserInfoHelper.getUserId());
        ProcessRecord processRecord = new ProcessRecord();
        processRecord.setProcessId(processId);
        processRecord.setStatus(status);
        processRecord.setDescription(description);
        processRecord.setOperateUserId(sysUser.getId());
        processRecord.setOperateUser(sysUser.getName());
        baseMapper.insert(processRecord);
    }
}
