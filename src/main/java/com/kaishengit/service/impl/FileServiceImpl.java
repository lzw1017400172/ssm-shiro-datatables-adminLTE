package com.kaishengit.service.impl;

import com.kaishengit.service.FileService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.UUID;

/**
 * Created by 刘忠伟 on 2017/2/17.
 */
@Service
public class FileServiceImpl implements FileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    //文件路径不能硬编码，写在config文件，读到容器，直接使用    @Value(${键})注解自动注入
    //获取容器中config文件内容，两种    一在配置文件中获取，使用${键},二在类中使用@Value("${键}")注解自动注入
    @Value("${upload.path}")
    private String filePath;


    /**
     * 合同文件的上传到本地
     * @param contentType 文件类型
     * @param originalFilename 文件名原始名字
     * @param inputStream 文件的输入流
     * @return 返回文件原名，和新名字
     */
    @Override
    public String fileUpload(String contentType, String originalFilename, InputStream inputStream) {
        //文件新名字
        String newFileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
        File file = new File(new File(filePath),newFileName);

        try {
            OutputStream outputStream = new FileOutputStream(file);
            IOUtils.copy(inputStream,outputStream);
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            return newFileName;

        } catch (IOException e) {
            logger.error("文件上传异常",e);
            throw new RuntimeException("文件上传异常",e);
        }


    }
}
