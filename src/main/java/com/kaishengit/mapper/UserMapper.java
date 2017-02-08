package com.kaishengit.mapper;

import com.kaishengit.pojo.User;
import com.kaishengit.utiils.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 刘忠伟 on 2017/1/12.
 *
 *  每个mapper。xml对应一个mapper接口。为了框架自动创建实现类对象。完全限定名和namescope一致，方法名和id一致，自动创建实现类要对照，
 */
public interface UserMapper {

    /*mybatis自动找到对应方法，创建实现类，封装方法，包括把结果封装，加入缓存等等。所以我们直接接调用方法就可以获取结果*/
    List<User> findAllPage(Page page);
    User findById(Integer id);
    Integer save(User user);
    void saves(List<User> userList);
    void update(User user);
    void del(Integer id);

    Long allCount();

    Long countByParam(@Param("userName") String userName, @Param("roleId") Integer roleId);

    List<User> findPageBySearchParam(@Param("page") Page page,@Param("userName") String userName,@Param("roleId") Integer roleId);

    User findByUserName(String userName);
}
