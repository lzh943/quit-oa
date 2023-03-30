package com.hello.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hello.model.process.ProcessType;
import com.hello.result.Result;

public interface ProcessTypeService extends IService<ProcessType> {
    /**
     * 分页查询审批类型
     * @param pageNum
     * @param pageSize
     * @return
     */
    Result pageQueryType(Long pageNum, Long pageSize);

    /**
     * 获取全部审批分类及模板
     * @return
     */
    Result findProcessType();
}
