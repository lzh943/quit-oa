package com.hello.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hello.model.process.ProcessRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProcessRecordMapper extends BaseMapper<ProcessRecord> {

}