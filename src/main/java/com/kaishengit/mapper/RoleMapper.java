package com.kaishengit.mapper;

import com.kaishengit.pojo.Role;

import java.util.List;

/**
 * Created by 刘忠伟 on 2017/1/12.
 */
public interface RoleMapper {

        Role findById(Integer id);
        List<Role> findAll();

        void saveNewUserRole(Integer userid, Integer roleid);

        void delUserAndRole(Integer id);

        List<Role> findByUserId(Integer id);
}
