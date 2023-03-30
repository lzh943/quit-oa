package com.hello.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hello.model.process.ProcessRecord;

public interface ProcessRecordService extends IService<ProcessRecord> {
    /**
     * 添加记录描述信息
     * @param processId
     * @param status
     * @param description
     */
    void record(Long processId, Integer status, String description);
}