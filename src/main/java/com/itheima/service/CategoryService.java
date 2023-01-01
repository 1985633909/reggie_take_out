package com.itheima.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.common.R;
import com.itheima.entity.Category;

/**
 * @author 19856
 * @description:
 * @since 2022/12/28-13:07
 */
public interface CategoryService extends IService<Category> {
    R<String> addCategory(Category category);

    R<Page<Category>> pageList(Long page, Long pageSize);

    R<String> deleteCategory(Long id);

    R<String> updateCategory(Category category);
}
