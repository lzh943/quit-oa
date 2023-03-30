package com.hello.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hello.model.process.ProcessTemplate;
import com.hello.result.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;

public interface ProcessTemplateService extends IService<ProcessTemplate> {
    /**
     * 分页查询审批模板
     * @param pageNum
     * @param pageSize
     * @return
     */
    Result pageQueryTemplate(Long pageNum, Long pageSize);

    /**
     * 上传流程定义文件
     * @param file
     * @return
     */
    Result uploadProcessDefinition(MultipartFile file) throws FileNotFoundException;

    /**
     * 部署流程定义(发布)
     * @param id
     */
    void publish(Long id);
}
