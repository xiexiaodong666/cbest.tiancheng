package com.welfare.servicemerchant.controller;

import com.welfare.persist.entity.FileTemplate;
import com.welfare.service.FileTemplateService;
import com.welfare.servicemerchant.service.FileUploadService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件模板服务控制器
 *
 * @author hao.yin
 * @since 2021-01-18 17:24:46
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/file-template")
@Api(tags = "文件模板")
public class FileTemplateController implements IController {
    private final FileTemplateService fileTemplateService;
    private final FileUploadService uploadService;

    @PostMapping("/upload")
    public R<String> upload(String fileType,@RequestParam(name = "file") MultipartFile multipartFile)
            throws IOException {
        long size = multipartFile.getSize();
        String contentType = multipartFile.getContentType();
        String filename = multipartFile.getOriginalFilename();
        String path = uploadService.uploadFile(filename, multipartFile.getInputStream(), contentType, size);
        String url=uploadService.getFileServerUrl(path);
        FileTemplate fileTemplate=new FileTemplate();
        fileTemplate.setFileType(fileType);
        fileTemplate.setUrl(url);
        fileTemplateService.add(fileTemplate);
        return success(url);
    }

    @GetMapping("/get-template")
    public R<String> getTemplate(String fileType) {
        return success(fileTemplateService.getByType(fileType));
    }
}