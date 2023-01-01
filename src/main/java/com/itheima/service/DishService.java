package com.itheima.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.common.R;
import com.itheima.dto.DishDto;
import com.itheima.entity.Category;
import com.itheima.entity.Dish;

import java.util.List;

/**
 * @author 19856
 * @description:
 * @since 2022/12/28-13:07
 */
public interface DishService extends IService<Dish> {

    R<Page<DishDto>> pageList(Long page, Long pageSize,String name);

    //新增菜品，同时插入菜品对应的口味数据
    public void saveWithFlavor(DishDto dishDto);

    R<String> updateStatus(List<Long> id, String status);

    R<String> deleteDish(List<Long> ids);

    R<DishDto> getDishDtoById(Long id);

    R<String> updateDish(DishDto dishDto);

    R<List<DishDto>> getDishListByCategoryId(Long categoryId,Integer status);
}
