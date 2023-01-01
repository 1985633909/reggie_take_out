package com.itheima.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.Mapper.DishMapper;
import com.itheima.common.R;
import com.itheima.dto.DishDto;
import com.itheima.entity.Category;
import com.itheima.entity.Dish;
import com.itheima.entity.DishFlavor;
import com.itheima.service.CategoryService;
import com.itheima.service.DishFlavorService;
import com.itheima.service.DishService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 19856
 * @description:
 * @since 2022/12/28-13:07
 */
@Service
@Transactional
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    @Override
    public R<Page<DishDto>> pageList(Long page, Long pageSize, String name) {
        //分页查询
        Page<Dish> categoryPage = query()
                .like(StringUtils.isNotEmpty(name), "name", name)
                .orderByAsc("create_time").page(new Page<>(page, pageSize));

        Page<DishDto> dishDtoPage = new Page<>();
        //对象拷贝
        BeanUtils.copyProperties(categoryPage, dishDtoPage, "records");

        //获取菜品集合
        List<Dish> records = categoryPage.getRecords();
        List<DishDto> dishDtos;

        //stream流解决
/*        List<DishDto> collect = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            //获取category对象
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());*/

        //将dish集合转为json数据再转为dishDto集合
        dishDtos = JSON.parseArray(JSON.toJSONString(records), DishDto.class);
        //将集合中每一个数据的getCategoryId()查询数据得出菜品种类后写入回去
        for (DishDto dishDto : dishDtos) {
            Category category = categoryService.getById(dishDto.getCategoryId());
            if (category == null) {
                continue;
            }
            dishDto.setCategoryName(category.getName());
        }

        //赋值
        dishDtoPage.setRecords(dishDtos);
//        dishDtoPage.setRecords(collect);
        return R.success(dishDtoPage);
    }


    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表
        save(dishDto);

        //保存菜品口味数据到菜品口味表dish_flavor
        List<DishFlavor> flavors = dishDto.getFlavors();
/*         flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());*/
        flavors.forEach(f -> f.setDishId(dishDto.getId()));

        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public R<String> updateStatus(List<Long> ids, String status) {
        for (Long id : ids) {
            update().set("status", status).eq("id", id).update();
        }

        return R.success("修改状态成功");
    }

    @Override
    public R<String> deleteDish(List<Long> ids) {
        for (Long id : ids) {
            //删除菜品
            removeById(id);
            //删除菜品口味
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId, id);
            dishFlavorService.remove(queryWrapper);
        }

        return R.success("删除成功");
    }

    @Override
    public R<DishDto> getDishDtoById(Long id) {
        Dish dish = getById(id);
        DishDto dishDto = JSON.parseObject(JSON.toJSONString(dish), DishDto.class);
        //获取dishId
        Long dishId = dishDto.getId();
        //获取口味
        List<DishFlavor> dishFlavorList = dishFlavorService.query().eq("dish_id", dishId).list();
        if (dishFlavorList != null) {
            //将口味放入DishDto中
            dishDto.setFlavors(dishFlavorList);
        }

        return R.success(dishDto);
    }

    @Override
    public R<String> updateDish(DishDto dishDto) {
        //获取口味信息
        List<DishFlavor> dishFlavors = dishDto.getFlavors();

        //新增的口味没有写入菜品id，所以都写入一下菜品id
        dishFlavors.forEach(dishFlavor -> {
            dishFlavor.setDishId(dishDto.getId());
        });
        //修改口味信息  先删除之前的信息 然后再重新写入
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
        dishFlavorService.saveBatch(dishFlavors);

        //修改菜品信息
        updateById(dishDto);
        return R.success("修改成功");
    }

    @Override
    public R<List<DishDto>> getDishListByCategoryId(Long categoryId,Integer status) {
        List<Dish> dishList = query().eq("category_id", categoryId)
                .eq(status != null,"status",status)
                .orderByDesc("update_time").list();

        List<DishDto> dishDtoList = dishList.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            //根据id查询分类对象
            Category category = categoryService.getById(item.getCategoryId());
            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            //SQL:select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }


}
