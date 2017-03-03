package com.kaishengit.service;

import com.kaishengit.pojo.Disk;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by 刘忠伟 on 2017/2/21.
 */
public interface DiskService {
    List<Disk> findByFid(Integer fid);

    Disk  findById(Integer id);

    FileInputStream getInputStream(Disk disk) throws FileNotFoundException;

    void saveNewFile(MultipartFile file, Integer id);

    void saveNewFolder(Integer fid, String newFolderName);

    void remove(Integer id);
}
