package com.itheima.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 19856
 * @description:
 * @since 2022/12/26-11:13
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
