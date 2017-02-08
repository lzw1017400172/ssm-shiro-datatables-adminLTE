package com.kaishengit.shiro;

import com.kaishengit.mapper.RoleMapper;
import com.kaishengit.mapper.UserMapper;
import com.kaishengit.pojo.Role;
import com.kaishengit.pojo.User;
import com.kaishengit.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 刘忠伟 on 2017/1/19.
 *  第二部创建ShiroDbRelm类，继承AuthorizingRealm类。实现其抽象方法
 *  此类需要被注入，所以纳入bean管理。此包已经被扫描，直接注解
 */
@Component
public class ShiroDbRelm extends AuthorizingRealm {


    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;//扫描过，自动创建实现类和对象，存在容器。自动获取

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //权限认证，判断当前用户的查看权限，需要查询当前用户的角色。根据角色，把角色返回到jsp。使用shiro节点获取。包含在内就可显示

        //第一步需要获取，当前登录对象
        User user = (User) principalCollection.getPrimaryPrincipal();
        //2查询此对象对应的角色。最好要封装到authorizationInfo对象里面，才返回
        User user1 = userMapper.findById(user.getId());
        List<Role> roleList = user1.getRoleList();
        if(!roleList.isEmpty()){
            //创建authorizationInfo对象，返回父类，可以返回子类对象
            SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
            for(Role role:roleList){
                authorizationInfo.addRole(role.getRoleName());
            }
            //把一个user对应的所有的roleName都装给authorizationInfo对象。返回，在jsp使用shiro标签调用时返回。并且自动判断是否包含
            return authorizationInfo;
        }
        //是空就返回null
        return null;
    }


    /**
     * 权限认证
     * @param authenticationToken 把username和password封装的对象，需要强转成子类对象。机制是在service调用此方法进行登录验证，如果返回null就会抛出异常，在service收到异常代表登录失败
     * @return
     * @throws AuthenticationException 在service要接受的异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        //在contorller里面调用的此方法。传入的token是一个子类。需要强转
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken)authenticationToken;
        //获取传过来的username.get方法是类里面封装的
        String userName = usernamePasswordToken.getUsername();
        //通过usrname去找对象
        User user = userService.findByUserName(userName);

        if(user != null){
            //用户存在就去验证密码
            return new SimpleAuthenticationInfo(user,user.getPassWord(),getName());
            //这个方法返回null就表示那边抛出异常。
            //第一个参数user，是传入session的值。方法内部将user对象放入session。可以在客户端使用shiro标签获取session。就是这第一个参数
            //第二个参数，。是找到的对象的密码，
            //第三个参数是个方法，会使用这个方法执行前面的两个参数。存到session，比较密码。错误返回null
        }
        //没有找到直接返回null，就会报异常
        return null;
    }
}
