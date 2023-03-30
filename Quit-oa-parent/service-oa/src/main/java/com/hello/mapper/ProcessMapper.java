package com.hello.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hello.model.process.Process;
import com.hello.vo.process.ProcessQueryVo;
import com.hello.vo.process.ProcessVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface ProcessMapper extends BaseMapper<Process> {
    /**
     *查询和审批申请有关的数据,例如用户姓名,审批类型
     */
    Page<ProcessVo> selectPage(@Param("page") Page<ProcessVo> processPage, @Param("vo") ProcessQueryVo processQueryVo);
}
