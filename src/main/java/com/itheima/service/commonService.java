package com.itheima.service;

import com.itheima.common.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 19856
 * @description:
 * @since 2022/12/28-19:11
 */
public interface commonService {
    R<String> upload(MultipartFile file);

    void download(String name, HttpServletResponse response);
}
