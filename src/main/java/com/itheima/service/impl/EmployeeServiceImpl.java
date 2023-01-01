package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.common.R;
import com.itheima.entity.Employee;
import com.itheima.Mapper.EmployeeMapper;
import com.itheima.service.EmployeeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author 19856
 * @description:
 * @since 2022/12/26-11:09
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Override
    public R<Employee> login(Employee employee, HttpServletRequest request) {
        //1.md5加密，查询数据库
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = getOne(queryWrapper);
        //2.失败，返回失败结果
        if (emp == null) {
            return R.error("登录失败");
        }
        //3.成功，比对密码是否一致
        if (!emp.getPassword().equals(password)) {
            //4.不一致，返回失败结果
            return R.error("登录失败");
        }
        //5.一致，查看员工状态是否已禁用 status 是否 = 1
        if (emp.getStatus() != 1) {
            //6.禁用，返回员工已禁用
            return R.error("账号已禁用");
        }
        //7.成功，将员工id存入session
        HttpSession session = request.getSession();
        session.setAttribute("employee", emp.getId());

        return R.success(emp);
    }

    @Override
    public R<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("employee");
        return R.success("退出成功");
    }

    @Override
    public R<Page<Employee>> pageList(Long page, Long pageSize, String name) {
        Page<Employee> employeePage = query()
                .like(StringUtils.isNotEmpty(name), "name", name)
                .orderByAsc("update_time")
                .page(new Page<>(page, pageSize));
        return R.success(employeePage);
    }

    @Override
    public R<String> addEmployee(Employee employee, HttpServletRequest request) {
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateUser((Long) request.getSession().getAttribute("user"));
//        employee.setUpdateUser((Long) request.getSession().getAttribute("user"));
        save(employee);
        return R.success("添加员工成功");
    }

    @Override
    public R<String> updateEmployee(HttpServletRequest request, Employee employee) {
//        HttpSession session = request.getSession();
        //获取修改者id
//        employee.setUpdateUser((Long) session.getAttribute("user"));
//        employee.setUpdateTime(LocalDateTime.now());
        boolean update = updateById(employee);
        if (!update) {
            return R.error("修改失败");
        }
        return R.success("修改状态成功");
    }

    @Override
    public R<Employee> getEmployeeById(Long id) {
        Employee employee = getById(id);
        return R.success(employee);
    }


}
