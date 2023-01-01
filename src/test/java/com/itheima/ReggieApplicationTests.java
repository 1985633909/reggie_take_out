package com.itheima;

import com.itheima.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ReggieApplicationTests {

    @Autowired
    private EmployeeService employeeService;

    @BeforeEach

    @Test
    void contextLoads() {
        String a = "123456";
        String b = 123456 + "";
        System.out.println(a.equals(b));
    }

    @Test
    void updateEmployeeTest() {

    }
}
