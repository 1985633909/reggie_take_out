package com.itheima.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.R;
import com.itheima.entity.Employee;
import com.itheima.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 19856
 * @description
 * @since 2022/12/25-20:57
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    /**
     * 员工登录
     * @param employee 员工信息
     * @param request 放入session
     * @return 登录是否成功
     */
    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee, HttpServletRequest request){
        return employeeService.login(employee,request);
    }

    /**
     * 退出登录
     * @param request 获取session从而获取登录用户
     * @return 是否退出成功
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        return employeeService.logout(request);
    }


    /**
     * 员工列表分页查询
     * @param page 第几页
     * @param pageSize 每页几条数据
     * @return 分页查询所需数据
     */
    @GetMapping("/page")
    public R<Page<Employee>> pageList(@RequestParam("page") Long page, @RequestParam("pageSize") Long pageSize, @RequestParam(value = "name",required = false) String name){
        return employeeService.pageList(page,pageSize,name);
    }

    /**
     * 添加员工
     * @param employee 员工信息
     * @param request 获取session从而获取登录者的信息
     * @return 添加是否成功
     */
    @PostMapping
    public R<String> addEmployee(@RequestBody Employee employee, HttpServletRequest request){
        return employeeService.addEmployee(employee,request);
    }

    /**
     * 修改员工信息
     * @param employee 员工信息
     * @return 修改是否成功
     */
    @PutMapping
    public R<String> updateEmployee(HttpServletRequest request,@RequestBody Employee employee){
        return employeeService.updateEmployee(request,employee);
    }

    /**
     * 根据id获取员工信息
     * @param id 员工id
     * @return 员工信息
     */
    @GetMapping("/{id}")
    public R<Employee> getEmployeeById(@PathVariable Long id){
        return employeeService.getEmployeeById(id);
    }

}
