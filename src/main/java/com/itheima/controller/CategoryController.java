package com.itheima.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.R;
import com.itheima.entity.Category;
import com.itheima.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 19856
 * @description:
 * @since 2022/12/28-13:08
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    /**
     * 新增菜品
     *
     * @param category 菜品分类/套餐分类
     * @return 成功或失败
     */
    @PostMapping
    public R<String> addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

    /**
     * 分页查询
     *
     * @param page     当前页
     * @param pageSize 每页大小
     * @return 分页数据
     */
    @GetMapping("/page")
    public R<Page<Category>> pageList(@RequestParam("page") Long page, @RequestParam("pageSize") Long pageSize) {
        return categoryService.pageList(page, pageSize);
    }

    /**
     * 根据id删除分类
     *
     * @param id 菜品id
     * @return 是否成功
     */
    @DeleteMapping
    public R<String> deleteCategory(@RequestParam("ids") Long id) {
        return categoryService.deleteCategory(id);
    }

    /**
     * 修改菜品信息
     *
     * @param category 菜品信息
     * @return 是否成功
     */
    @PutMapping
    public R<String> updateCategory(@RequestBody Category category) {
        return categoryService.updateCategory(category);
    }


    /**
     * 获取菜品/套餐种类
     *
     * @param type 1是菜品2是套餐
     * @return 返回菜品/套餐集合
     */
    @GetMapping("/list")
    public R<List<Category>> list(@RequestParam(value = "type", required = false) Integer type) {
        List<Category> list = categoryService.query().eq(type != null, "type", type).list();
        return R.success(list);
    }


}
