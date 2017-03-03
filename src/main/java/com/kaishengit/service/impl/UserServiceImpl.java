package com.kaishengit.service.impl;

import com.kaishengit.mapper.RoleMapper;
import com.kaishengit.mapper.UserMapper;
import com.kaishengit.pojo.Role;
import com.kaishengit.pojo.User;
import com.kaishengit.service.UserService;

import com.kaishengit.service.WeiXinService;
import com.kaishengit.utiils.Page;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 刘忠伟 on 2017/1/12.
 */
/*此类需要被bean管理*/
@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    /*需要使用Mapper接口实现类对象，调用sql。已经被mybtis里面扫描过所有接口，全部都创建好对象，只需要调用*/

    @Autowired
    private WeiXinService weiXinService;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    /*肯定需要盐值，盐值是在config文件，已经读取存在于spring容器，读取到容器中的值，用占位符获取${}*/
    @Value("${salt}")
                private String salt;


    @Override
    public Page findAll(Integer pageNum) {
        //是数字时，大于总页数或者小于1，都在page工具类限制过。不用管*/
        int total = userMapper.allCount().intValue();
        Page page = new Page(total,pageNum);
        List<User> users = userMapper.findAllPage(page);
        page.setItems(users);
        return page;
    }

    @Override
    public User findById(Integer id) {
        return userMapper.findById(id);
    }

    /*@Override
    public void save(User user) {
        logger.debug("salt:"+salt);
        //密码需要加盐
        user.setPassWord(DigestUtils.md5Hex(salt+user.getPassWord()));
        userMapper.save(user);
    }*/

    @Override
    //执行多个sql，需要开启事务，全部回滚，全部成功
    @Transactional
    public void del(Integer id) {

        //有外键约束，不能直接删除user，先删除外键关系表的记录
        roleMapper.delUserAndRole(id);
        //删除 user
        userMapper.del(id);

    }

    @Override
    @Transactional/*开启事务*/
    public void update(User user,Integer[] roleids) {

        //修改关系表。先删除原来的，再新添
        roleMapper.delUserAndRole(user.getId());
        if(roleids != null) {/*为空表示没有选，就不添加关系*/
            for (Integer roleid : roleids) {
                Role role = roleMapper.findById(roleid);
                if(role != null) {
                    roleMapper.saveNewUserRole(user.getId(), roleid);
                }
            }
        }
        //修改user
        if(StringUtils.isNotEmpty(user.getPassWord())){
            //密码帐号都改,只需要判断password的null，在mapper用动态sql就可以
           user.setPassWord(DigestUtils.md5Hex(salt+user.getPassWord()));

        }
        userMapper.update(user);
    }

    @Override
    public List<Role> finRoleAll() {
        return roleMapper.findAll();
    }

    @Override
    @Transactional/*有多个sql，需要开启事务*/
    public void saveNewUser(User user, Integer[] roleIds) {
        //保存用户
        logger.debug("salt:"+salt);
        logger.debug("电话"+user.getMobile());
        /*//密码需要加盐
        user.setPassWord(DigestUtils.md5Hex(salt+user.getPassWord()));*/
        /*我们需要添加完用户的用户id，使用可以返回id的insert*/
        userMapper.save(user);
        //2保存用户和角色关系
        if(roleIds != null){
            for(Integer roleid : roleIds){
                Role role = roleMapper.findById(roleid);

                if(role != null) {//添加关系.传了两个参数，需要使用注解，或者使用param1234来获取
                    roleMapper.saveNewUserRole(user.getId(), roleid);
                    /*里面配置的insert，插入数据后，会把取得的id赋值给传进去的user，所以直接user.getid。同一片内存空间的user，*/
                }
            }
        }

        //3保存到微信，添加微信成员
        com.kaishengit.dto.wx.User user2 = new com.kaishengit.dto.wx.User();
        user2.setUserid(user.getId().toString());
        user2.setName(user.getUserName());
        user2.setMobile(user.getMobile());
        user2.setDepartment(Arrays.asList(roleIds));//将一个数组转换成一个集合
        //调用微信的创建用户
        weiXinService.wxCeateUser(user2);
    }

    @Override
    public Page findAllByPageAndSearchParam(Integer pageNum, String userName, Integer roleId) {
        //不再是所有数量，要按照条件name和roleid
        //int total = userMapper.allCount().intValue();
        int total = userMapper.countByParam(userName,roleId).intValue();

        Page page = new Page(total,pageNum);
        //查询值也要按照关键字查询
        //List<User> users = userMapper.findAllPage(page);
        logger.debug(""+ page.getStart());
        List<User> users = userMapper.findPageBySearchParam(page,userName,roleId);
        page.setItems(users);
        return page;
    }

    @Override
    public User findByUserName(String userName) {

        return userMapper.findByUserName(userName);
    }
}
