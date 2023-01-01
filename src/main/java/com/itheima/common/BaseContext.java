package com.itheima.common;

/**
 * 基于ThreadLocal封装工具类，用户保存和获取当前登录id
 * @author 19856
 * @description:
 * @since 2022/12/27-21:56
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
