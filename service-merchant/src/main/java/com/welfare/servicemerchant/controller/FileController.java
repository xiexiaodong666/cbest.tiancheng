package com.welfare.servicemerchant.controller;

import com.welfare.servicemerchant.service.FileUploadService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/8  12:47 AM
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/file")
@Api(tags = "文件相关")
public class FileController implements IController{

  @Autowired
  private FileUploadService uploadService;

  @PostMapping("/upload")
  public R<String> upload(@RequestParam(name = "file") MultipartFile multipartFile)
          throws IOException {
    long size = multipartFile.getSize();
    String contentType = multipartFile.getContentType();
    String filename = multipartFile.getOriginalFilename();
    String path = uploadService.uploadFile(filename, multipartFile.getInputStream(), contentType, size);
    return success(uploadService.getFileServerUrl(path));
  }
}