package com.itheima.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.Mapper.UserMapper;
import com.itheima.common.R;
import com.itheima.entity.User;
import com.itheima.service.UserService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author 19856
 * @description:
 * @since 2022/12/28-13:07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Override
    public R<User> login(User user, HttpServletRequest request) {
        //获取session
        HttpSession session = request.getSession();
        //查询数据库是否有员工数据，如果没有则注册
        User phone = query().eq("phone", user.getPhone()).one();
        if (phone == null ){
            //新增用户
            save(user);
            //将有id的user传给phone
            phone = user;
        }
        //有员工数据，直接登录
        session.setAttribute("user",phone.getId());
        System.out.println("phone.getId() = " + phone.getId());
        return R.success(user);
    }
}
