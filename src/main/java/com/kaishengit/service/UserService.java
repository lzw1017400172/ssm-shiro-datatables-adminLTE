package com.kaishengit.service;

import com.kaishengit.pojo.Role;
import com.kaishengit.pojo.User;
import com.kaishengit.utiils.Page;

import java.util.List;

/**
 * Created by 刘忠伟 on 2017/1/12.
 * 面向接口编程
 */
public interface UserService {
    Page findAll(Integer pageNum);
    User findById(Integer id);
    //void save(User user);

    void del(Integer id);

    void update(User user,Integer[] roleids);

    List<Role> finRoleAll();

    void saveNewUser(User user, Integer[] roleIds);

    Page findAllByPageAndSearchParam(Integer pageNum, String userName, Integer roleId);

    User findByUserName(String userName);
}
