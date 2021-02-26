package com.welfare.common.util;

import com.welfare.common.annotation.ConditionalOnHavingProperty;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 2/8/2021
 */
@Configuration
@ConditionalOnHavingProperty("ftp.enabled")
@Slf4j
public class FtpUtil {
    @Value("${ftp.host:}")
    private String hostName;
    @Value("${ftp.user:}")
    private String username;
    @Value("${ftp.password:}")
    private String password;
    @Value("${ftp.port:}")
    private Integer port;
    @Value("${ftp.default-path:}")
    private String uploadPath;

    @SneakyThrows
    public void upload(File file){
        FTPClient ftpClient = createAndLogin();
        ftpClient.changeWorkingDirectory(uploadPath);
        ftpClient.enterLocalPassiveMode();
        ftpClient.storeFile(file.getName(),new FileInputStream(file));
        ftpClient.disconnect();
    }

    @SneakyThrows
    public void upload(String fileName, InputStream inputStream){
        FTPClient ftpClient = createAndLogin();
        ftpClient.changeWorkingDirectory(uploadPath);
        ftpClient.enterLocalPassiveMode();
        ftpClient.storeFile(fileName,inputStream);
        ftpClient.disconnect();
    }

    private FTPClient createAndLogin() throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("UTF-8");
        ftpClient.enterLocalPassiveMode();
        ftpClient.connect(hostName,port);
        boolean loggedIn = false;
        if(StringUtils.isEmpty(username)){
            loggedIn = ftpClient.login("Anonymous","");
        }else{
            loggedIn = ftpClient.login(username,password);
        }
        Assert.isTrue(loggedIn,"FTP登录失败,username:"+username);
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        return ftpClient;
    }


}
