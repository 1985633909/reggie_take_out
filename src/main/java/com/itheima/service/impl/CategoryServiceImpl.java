package com.itheima.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.Mapper.CategoryMapper;
import com.itheima.common.R;
import com.itheima.entity.Category;
import com.itheima.service.CategoryService;
import com.itheima.service.DishService;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 19856
 * @description:
 * @since 2022/12/28-13:07
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    @Override
    public R<String> addCategory(Category category) {
        boolean save = save(category);
        if (!save){
            return R.error("保存失败");
        }
        return R.success("保存成功");
    }

    @Override
    public R<Page<Category>> pageList(Long page, Long pageSize) {
        Page<Category> categoryPage = query().orderByAsc("sort").page(new Page<>(page, pageSize));
        return R.success(categoryPage);
    }

    @Override
    public R<String> deleteCategory(Long id) {
        //查询当前分类是否关联菜品，如果已经关联，直接抛出异常 select * from dish where category_id = ?
        Integer count;
        count = dishService.query().eq("category_id", id).count();
        if (count != 0){
            return R.error("已关联菜品");
        }
        //查询当前分类是否关联套餐，如果已经关联，直接抛出异常
        count = setmealService.query().eq("category_id", id).count();
        if (count !=0){
            return R.error("已关联套餐");
        }
        boolean b = removeById(id);
        if (!b){
            return R.error("删除菜品失败");
        }
        return R.success("删除成功");
    }

    @Override
    public R<String> updateCategory(Category category) {
        boolean success = updateById(category);
        if (!success){
            return R.error("修改失败");
        }
        return R.success("修改成功");
    }


}
