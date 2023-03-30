package com.hello.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.mapper.ProcessTemplateMapper;
import com.hello.model.process.ProcessTemplate;
import com.hello.model.process.ProcessType;
import com.hello.result.Result;
import com.hello.service.ProcessService;
import com.hello.service.ProcessTemplateService;
import com.hello.service.ProcessTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProcessTemplateServiceImpl extends ServiceImpl<ProcessTemplateMapper, ProcessTemplate>
        implements ProcessTemplateService {
    @Resource
    private ProcessTypeService processTypeService;
    @Resource
    private ProcessService processService;
    @Override
    public Result pageQueryTemplate(Long pageNum, Long pageSize) {
        Page<ProcessTemplate> templatePage=new Page<>(pageNum, pageSize);
        page(templatePage);
        List<ProcessTemplate> templates = templatePage.getRecords();
        for(ProcessTemplate template:templates){
             ProcessType processType = processTypeService.getById(template.getProcessTypeId());
             if(processType==null){
                 continue;
             }
             template.setProcessTypeName(processType.getName());
        }
        templatePage.setRecords(templates);
        return Result.ok(templatePage);
    }

    @Override
    public Result uploadProcessDefinition(MultipartFile file) throws FileNotFoundException {
        String path = new File(ResourceUtils.getURL("classpath:")
                .getPath()).getAbsolutePath();
        String fileName = file.getOriginalFilename();
        // 上传目录
        File tempFile = new File(path + "/processes/");
        // 判断目录是否存着
        if (!tempFile.exists()) {
            tempFile.mkdirs();//创建目录
        }
        // 创建空文件用于写入文件
        File imageFile = new File(path + "/processes/" + fileName);
        // 保存文件流到本地
        try {
            file.transferTo(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("上传失败");
        }
        Map<String, Object> map = new HashMap<>();
        //根据上传地址后续部署流程定义，文件名称为流程定义的默认key
        map.put("processDefinitionPath", "processes/" + fileName);
        map.put("processDefinitionKey", fileName.substring(0, fileName.lastIndexOf(".")));
        return Result.ok(map);
    }
    @Transactional
    @Override
    public void publish(Long id) {
        ProcessTemplate template = baseMapper.selectById(id);
        template.setStatus(1);
        baseMapper.updateById(template);
        //优先发布在线流程设计
        if(!StringUtils.isEmpty(template.getProcessDefinitionPath())) {
            processService.deployByZip(template.getProcessDefinitionPath());
        }

    }
}
