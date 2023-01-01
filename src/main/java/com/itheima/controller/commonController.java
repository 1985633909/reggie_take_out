package com.itheima.controller;

import com.itheima.common.R;
import com.itheima.service.commonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 19856
 * @description: 文件上传与下载
 * @since 2022/12/28-19:10
 */
@RestController
@RequestMapping("/common")
public class commonController {

    @Autowired
    private commonService commonService;

    /**
     * 文件上传
     * @param file 文件
     * @return 文件名
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        return commonService.upload(file);
    }

    /**
     * 文件下载
     * @param name 文件名
     * @param response 相应
     * @return
     */
    @GetMapping("download")
    public void download(@RequestParam("name") String name, HttpServletResponse response){
        commonService.download(name,response);
    }

}
