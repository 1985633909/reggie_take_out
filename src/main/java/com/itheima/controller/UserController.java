package com.itheima.controller;

import com.itheima.common.R;
import com.itheima.entity.Employee;
import com.itheima.entity.User;
import com.itheima.service.DishFlavorService;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author 19856
 * @description:
 * @since 2022/12/28-13:08
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param user 用户
     * @param request 放入session
     * @return 登录是否成功
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody User user, HttpServletRequest request){
        return userService.login(user,request);
    }

    @PostMapping("/loginout")
    public R<String> loginout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        return R.success("退出成功");
    }


}
