package com.itheima.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.Mapper.SetmealMapper;
import com.itheima.common.R;
import com.itheima.dto.SetmealDto;
import com.itheima.entity.Setmeal;
import com.itheima.entity.SetmealDish;
import com.itheima.service.CategoryService;
import com.itheima.service.SetmealDishService;
import com.itheima.service.SetmealService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 19856
 * @description:
 * @since 2022/12/28-13:07
 */
@Service
@Transactional
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    @Override
    public R<Page<SetmealDto>> pageList(Long page, Long pageSize,String name) {
        Page<Setmeal> setmealPage = query()
                .like(StringUtils.isNotEmpty(name), "name", name)
                .orderByDesc("update_time")
                .page(new Page<>(page, pageSize));
        Page<SetmealDto> setmealDtoPage = new Page<>();
        //对象拷贝
        BeanUtils.copyProperties(setmealPage, setmealDtoPage,"records");

        //获取套餐集合
        List<Setmeal> setmeals = setmealPage.getRecords();
        List<SetmealDto> setmealDtos;
        //将Setmeal集合转为json数据再转为SetmealDto集合
        setmealDtos = JSON.parseArray(JSON.toJSONString(setmeals), SetmealDto.class);

        //获取套餐分类名称
        for (SetmealDto record : setmealDtos) {
            String categoryName = categoryService.getById(record.getCategoryId()).getName();
            record.setCategoryName(categoryName);
        }

        //放入结果
        setmealDtoPage.setRecords(setmealDtos);
        return R.success(setmealDtoPage);
    }

    @Override
    public void saveSetmeal(SetmealDto setmealDto) {
        //保存套餐信息setmeal
        save(setmealDto);
        //保存套餐中菜品信息setmeal_dish
        //先放入套餐id
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealDto.getId())
        );
        setmealDishService.saveBatch(setmealDishes);

    }

    @Override
    public R<String> updateStatus(List<Long> ids, String status) {
        for (Long id : ids) {
            update().set("status", status).eq("id", id).update();
        }

        return R.success("修改状态成功");
    }

    @Override
    public R<String> deleteSetmeal(List<Long> ids) {
        //删除套餐数据
        for (Long id : ids) {
            LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SetmealDish::getSetmealId,id);
            setmealDishService.remove(wrapper);
        }
        //删除套餐
        removeByIds(ids);
        return R.success("删除成功");
    }

    @Override
    public R<SetmealDto> getSetmealAndSetmealDishById(Long id) {
        SetmealDto setmealDto = new SetmealDto();
        //获取套餐信息
        Setmeal setmeal = getById(id);
        //拷贝到setmealDto中
        BeanUtils.copyProperties(setmeal,setmealDto);
        //查询套餐内菜品信息
        List<SetmealDish> setmealDishList = setmealDishService.query().eq("setmeal_id", id).list();
        setmealDto.setSetmealDishes(setmealDishList);

        return R.success(setmealDto);
    }

    @Override
    public R<String> updateSetmealAndSetmealDish(SetmealDto setmealDto) {
        updateById(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //删除之前的菜品
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(wrapper);
        //将新添加的菜品保存
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealDto.getId()));
        setmealDishService.saveBatch(setmealDishes);
        return R.success("修改成功");
    }

}
