package com.itheima.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.common.R;
import com.itheima.entity.Employee;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 19856
 * @description:
 * @since 2022/12/26-11:08
 */
public interface EmployeeService extends IService<Employee> {

    R<Employee> login(Employee employee, HttpServletRequest request);

    R<String> logout(HttpServletRequest request);

    R<Page<Employee>> pageList(Long page, Long pageSize, String name);

    R<String> addEmployee(Employee employee, HttpServletRequest request);

    R<String> updateEmployee(HttpServletRequest request, Employee employee);

    R<Employee> getEmployeeById(Long id);
}
