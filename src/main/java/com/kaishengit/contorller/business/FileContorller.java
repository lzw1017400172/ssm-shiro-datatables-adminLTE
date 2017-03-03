package com.kaishengit.contorller.business;

import com.kaishengit.dto.AjaxResult;
import com.kaishengit.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 刘忠伟 on 2017/2/17.
 */
@Controller
@RequestMapping("/business/file")
public class FileContorller {


    @Autowired
    private FileService fileService;
    /**
     * springmvc文件上传，需要依赖commons-upload，配置视图解析器文件上传
     * @return data新文件名和源文件名
     */
    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult fileUpload(MultipartFile file){


        try {
            String newFileName = fileService.fileUpload(file.getContentType(),
                                                            file.getOriginalFilename(),
                                                                    file.getInputStream());
            Map<String,Object> map = new HashMap<>();//或者Maps.newHashMap();
            map.put("sourceFileName",file.getOriginalFilename());
            map.put("newFileName",newFileName);
            return new AjaxResult(map);
        } catch (Exception e) {//范围大点，还要接受runtimen自定义异常
            e.printStackTrace();
            return new AjaxResult(e.getMessage());
        }

    }

}
