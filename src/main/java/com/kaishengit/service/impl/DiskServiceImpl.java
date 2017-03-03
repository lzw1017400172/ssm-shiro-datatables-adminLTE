package com.kaishengit.service.impl;

import com.google.common.collect.Lists;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.mapper.DiskMapper;
import com.kaishengit.pojo.Disk;
import com.kaishengit.service.DiskService;
import com.kaishengit.utiils.ShiroUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.UUID;

/**
 * Created by 刘忠伟 on 2017/2/21.
 */
@Service//交给bean管理
public class DiskServiceImpl implements DiskService {


    @Autowired
    private DiskMapper diskMapper;
    @Value("${upload.path}")
    private String downloadPath;
    /**
     * 根据fid就可以查到任意文件夹下的文件，fid=0时根文件下
     * @param fid
     * @return
     */
    @Override
    public List<Disk> findByFid(Integer fid) {
        List<Disk> diskList = diskMapper.findByFid(fid);

        return diskList;
    }

    /**
     * 根据资源id查资源
     * @param id
     * @return
     */
    @Override
    public Disk findById(Integer id) {

        return diskMapper.findById(id);
    }

    /**
     * 根据资源老名字创建文件输入流
     * @param disk
     * @return
     */
    @Override
    public FileInputStream getInputStream(Disk disk) throws FileNotFoundException {

        return new FileInputStream(new File(downloadPath,disk.getNewName()));
    }

    /**
     * 文件上传，1上传到此盘2保存到数据库fid=id
     * @param file
     * @param id
     */
    @Override
    @Transactional//开启事务
    public void saveNewFile(MultipartFile file, Integer id)  {

        String sourceName = file.getOriginalFilename();//原名字

        //保存到磁盘的文件一般也是需要后缀的。所以需要判断，文件原名是否有后缀。有也加上，没有也不加
        String newName =  UUID.randomUUID().toString();
        if(sourceName.lastIndexOf(".")!= -1){//找不到就返回-1
            //有后缀
            newName = newName + sourceName.substring(sourceName.lastIndexOf("."));
        }
        //1文件上传
        try {
            InputStream inputStream = file.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(new File(new File(downloadPath), newName));
            IOUtils.copy(inputStream, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
        }catch (IOException ex){
            throw new ServiceException("文件上传失败",ex);//文件上传失败要手动抛出异常，因为要保证事务回滚
        }
        //2保存数据库,id作为此文件的fid，表示位置
        Disk disk = new Disk();
        disk.setSourceName(sourceName);
        disk.setNewName(newName);
        disk.setCreateUser(ShiroUtils.getUserName());
        disk.setFid(id);//当前文件夹的id，作为fid
        disk.setType("file");
        disk.setSize(FileUtils.byteCountToDisplaySize(file.getSize()));
        //FileUtils来自commonsio包，将long文件大小转换成可视的带mb，kb

        diskMapper.save(disk);
    }

    /**
     * 创建新的文件夹
     * @param fid 父id，所属文件夹
     * @param newFolderName
     */
    @Override
    public void saveNewFolder(Integer fid, String newFolderName) {
        Disk disk = new Disk();
        disk.setType(Disk.FOLDER);
        disk.setFid(fid);
        disk.setCreateUser(ShiroUtils.getUserName());
        disk.setSourceName(newFolderName);

        diskMapper.save(disk);
    }


    /*自己写的删除文件（文件夹）和更新数据库的递归*/

   /* *//**
     * 根据资源id删除资源，需要判断文件还是文件夹
     * 文件直接删除
     * 文件夹：文件夹内，下一级还有文件或者文件夹，递归删除
     * @param id
     *//*
    @Override
    @Transactional
    public void remove(Integer id) {

        //先判断是文件还是文件夹，是文件夹直接删除没有然后了
        Disk disk = diskMapper.findById(id);
        if(disk != null){
            if(Disk.FILE.equals(disk.getType())){
                //是文件直接删除
                //删除数据库
                diskMapper.delete(id);
                //删除文件
                File file = new File(downloadPath,disk.getNewName());
                file.delete();
            } else {
                //是文件夹，需要根据此id去当作fid去查询表，查出来的集合，是都要继续删除的。里面可能还有文件夹
               //先删除本条

                //创建一个集合，是所有需要删除的数据库的id。本次的id也要加上去。不一次一次删会提高性能
                List<Integer> idList = Lists.newArrayList();

                List<Disk> diskList = diskMapper.findByFid(id);//需要删除的集合
                findDelId(diskList,idList);
                idList.add(id);

                //在这里删除集合所有元素的数据库对应记录即可.集合是对象，传过去可对集合改变
                diskMapper.deleteList(idList);
            }
        } else {
            throw new NotFoundException();
        }
    }


    *//**
     *  使用递归查找文件夹内的所有文件和文件夹的id（删除数据库），和删除文件。因为文件夹不存在所以不用删除了
     *
     * @param         当前要删除的集合
     *//*
    public void findDelId(List<Disk> diskList,List<Integer> idList){

        for(Disk disk:diskList){

            if(Disk.FILE.equals(disk.getType())){
                //是文件直接删除文件。并且删除数据库。
                File file = new File(downloadPath,disk.getNewName());
                file.delete();
                idList.add(disk.getId());
            } else {
                //是文件夹。先删除本条数据库。不删除文件，使用disk.getId()在查找全部，递归
                idList.add(disk.getId());
                List<Disk> diskList1 = diskMapper.findByFid(disk.getId());
                findDelId(diskList1,idList);
            }


        }
    }*/




    /*使用老师的递归*/

    /**
     * 删除指定资源或者文件夹
     * @param id
     */
    @Override
    @Transactional
    public void remove(Integer id) {
        Disk disk = diskMapper.findById(id);
        if(disk != null){
            if(Disk.FILE.equals(disk.getType())){
                //是文件直接删除文件和数据库
                diskMapper.delete(id);
                File file = new File(downloadPath,disk.getNewName());
                file.delete();
            } else {
                //是文件夹，需要迭代文件夹内的资源。可能还有文件夹需要递归。使用批量删除，所以创建一个集合
                //把所有需要删除的id放进去。集合属于对象，下面对齐操作

                /*这次递归的思路：查询一次全部的数据库，迭代出判断fid和此id是否相同，找出文件夹的所有资源。判断是文件还是
                * 文件夹，继续递归*/

                List<Integer> idList = Lists.newArrayList();
                List<Disk> diskList = diskMapper.findAll();
                //需要加入本文件夹的id，
                idList.add(id);
                findDelId(idList,diskList,id);
                //文件在递归中已经一个一个删除了，数据庫在这里批量删除。这就是idList的意义性能
                diskMapper.deleteList(idList);
            }
        }

    }

    /**
     * 递归，和全部disk比较，找出id文件夹的所有子，继续判断是否是文件夹，文件：直接删除文件，添加一条数据到IdList。文件夹：递归，不删除文件（因为没文件），但是本条数据库要删，加入idList
     * @param idList
     * @param diskList
     */
    public void findDelId(List<Integer> idList,List<Disk> diskList,Integer id){

        for(Disk disk :diskList){//迭代所有disk,找出所有fid=id的，所属这个文件夹的
            if(id == disk.getFid()){
                //只要是这个文件夹里面的，就要删除数据库中此disk对应id的记录。加入idList
                idList.add(disk.getId());
                //判断是文件还是文件夹
                if(Disk.FILE.equals(disk.getType())){
                    //文件，就直接把文件删除了，数据库记录要删除，已经添加带idList
                    File file = new File(downloadPath,disk.getNewName());
                    file.delete();
                } else {
                    //文件夹。  不需要删除因为不存在。需要删除数据库记录，已经加入idList
                    //但是文件夹内部有资源。继续迭代所有disk，判断和fid=id的disk。所以递归
                    findDelId(idList,diskList,disk.getId());
                }
            }
        }


    }

}
