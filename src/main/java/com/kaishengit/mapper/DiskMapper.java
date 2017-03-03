package com.kaishengit.mapper;

import com.kaishengit.pojo.Disk;

import java.util.List;

/**
 * Created by 刘忠伟 on 2017/2/21.
 */
public interface DiskMapper {
    List<Disk> findByFid(Integer fid);

    Disk findById(Integer id);

    /*添加新的文件和文件夹都用同一个*/
    void save(Disk disk);


    void delete(Integer id);

    void deleteList(List<Integer> idList);

    List<Disk>  findAll();
}
