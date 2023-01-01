package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.common.R;
import com.itheima.entity.DishFlavor;
import com.itheima.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 19856
 * @description:
 * @since 2022/12/28-13:07
 */
public interface UserService extends IService<User> {

    R<User> login(User user, HttpServletRequest request);
}
