package com.kaishengit.service;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by 刘忠伟 on 2017/2/17.
 */
public interface FileService {
    String fileUpload(String contentType, String originalFilename, InputStream inputStream) ;
}
