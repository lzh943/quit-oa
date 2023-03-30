package com.hello.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hello.model.process.ProcessType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProcessTypeMapper extends BaseMapper<ProcessType> {
}
