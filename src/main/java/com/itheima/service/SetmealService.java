package com.itheima.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.common.R;
import com.itheima.dto.SetmealDto;
import com.itheima.entity.Dish;
import com.itheima.entity.Setmeal;

import java.util.List;

/**
 * @author 19856
 * @description:
 * @since 2022/12/28-13:07
 */
public interface SetmealService extends IService<Setmeal> {

    R<Page<SetmealDto>> pageList(Long page, Long pageSize,String name);

    void saveSetmeal(SetmealDto setmealDto);

    R<String> updateStatus(List<Long> ids, String status);

    R<String> deleteSetmeal(List<Long> ids);

    R<SetmealDto> getSetmealAndSetmealDishById(Long id);

    R<String> updateSetmealAndSetmealDish(SetmealDto setmealDto);
}
