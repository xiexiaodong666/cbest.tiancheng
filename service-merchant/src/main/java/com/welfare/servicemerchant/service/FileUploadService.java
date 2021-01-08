package com.welfare.servicemerchant.service;

import com.alibaba.excel.EasyExcel;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/8  12:45 AM
 */
@Service
@Slf4j
public class FileUploadService {

  @Value("${cdn.server}")
  private String cdnServerAddress;

  @Autowired
  AmazonS3 cbestAmazonS3Client;

  @Value("${cbest.oss.config.bucket}")
  private String bucketName;

  @Value("${cdn.server}")
  private String cndServer;

  /**
   * 上传文件到oss
   * @param fileName 文件名称 xxx.jpg
   * @param inputStream
   * @param contentType 文本类型
   * @param size 文件大小
   * @return 访问地址
   * @throws IOException
   */
  public String uploadFile(String fileName, InputStream inputStream, String contentType, long size) throws IOException {
    Date date = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssS");
    String name = getFileNameNoEx(fileName);
    String suffix = getExtensionName(fileName);
    String nowStr = "-" + format.format(date);
    fileName = name + nowStr + "." + suffix;
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(contentType);
    metadata.setContentLength(size);
    PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName,
            inputStream,metadata)
            .withCannedAcl(CannedAccessControlList.PublicRead);
    cbestAmazonS3Client.putObject(putObjectRequest);
    URL url = cbestAmazonS3Client.getUrl(bucketName, fileName);
    return url.getPath();
  }

  /**
   * 生成Excel并上传到oss
   * @param list 表格的数据
   * @param cla  每一行对应的数据对象类型（bean、map）
   * @param fileName  不包含扩展名
   * @return path
   * @throws IOException
   */
  public String uploadExcelFile(List<Object> list, Class cla, String fileName) throws IOException {
    String path = null;
    ByteArrayOutputStream outputStream = null;
    try {
      fileName = fileName + ".xlsx";
      outputStream = new ByteArrayOutputStream();
      EasyExcel.write(outputStream, cla).sheet().doWrite(list);
      ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
      path = uploadFile(fileName, inputStream, "application/vnd.ms-excel", outputStream.size());
    } catch (Exception e) {
      log.error("上传表格文件失败,fileName:{}", fileName, e);
    } finally {
      if (outputStream != null) {
        outputStream.close();
      }
    }
    return path;
  }

  /**
   * 文件oss后获取访问地址
   * @param path
   * @return
   */
  public String getFileServerUrl(String path) {
    return cdnServerAddress + path;
  }

  /**
   * 获取文件扩展名，不带 .
   */
  private static String getExtensionName(String filename) {
    if ((filename != null) && (filename.length() > 0)) {
      int dot = filename.lastIndexOf('.');
      if ((dot > -1) && (dot < (filename.length() - 1))) {
        return filename.substring(dot + 1);
      }
    }
    return filename;
  }

  /**
   * Java文件操作 获取不带扩展名的文件名
   */
  private static String getFileNameNoEx(String filename) {
    if ((filename != null) && (filename.length() > 0)) {
      int dot = filename.lastIndexOf('.');
      if ((dot > -1) && (dot < (filename.length()))) {
        return filename.substring(0, dot);
      }
    }
    return filename;
  }
}