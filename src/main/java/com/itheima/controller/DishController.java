package com.itheima.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.R;
import com.itheima.dto.DishDto;
import com.itheima.entity.Category;
import com.itheima.entity.Dish;
import com.itheima.entity.DishFlavor;
import com.itheima.service.CategoryService;
import com.itheima.service.DishFlavorService;
import com.itheima.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 19856
 * @description:
 * @since 2022/12/28-13:08
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;


    /**
     * 分页查询
     *
     * @param page     当前页
     * @param pageSize 每页大小
     * @return 分页数据
     */
    @GetMapping("/page")
    public R<Page<DishDto>> pageList(@RequestParam("page") Long page, @RequestParam("pageSize") Long pageSize, @RequestParam(value = "name", required = false) String name) {
        return dishService.pageList(page, pageSize, name);
    }

    /**
     * 新增菜品,同时插入菜品对应的口味数据
     *
     * @param dishDto 菜品dto
     * @return 新增成功
     */
    @PostMapping
    public R<String> saveDish(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 修改菜品状态/批量修改
     *
     * @param id     菜品id
     * @param status 修改的菜品状态
     * @return 是否修改成功
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@RequestParam("ids") List<Long> id, @PathVariable String status) {
        return dishService.updateStatus(id, status);
    }

    /**
     * 根据id删除菜品/批量删除
     *
     * @param ids 菜品id
     * @return 是否成功
     */
    @DeleteMapping
    public R<String> deleteDish(@RequestParam("ids") List<Long> ids) {
        return dishService.deleteDish(ids);
    }

    /**
     * 根据id获取菜品信息
     *
     * @param id 菜品id
     * @return 菜品信息(包括dish表与dish_flavor口味表)
     */
    @GetMapping("/{id}")
    public R<DishDto> getDishById(@PathVariable("id") Long id) {
        return dishService.getDishDtoById(id);
    }

    /**
     * 修改菜品以及口味信息
     *
     * @param dishDto 菜品以及口味
     * @return 是否成功
     */
    @PutMapping
    public R<String> updateDish(@RequestBody DishDto dishDto) {
        return dishService.updateDish(dishDto);

    }


    /**
     * 根据菜品种类id获取当前菜品类型的所有菜品
     *
     * @param categoryId 菜品种类id
     * @return 符合条件的菜品的集合
     */

    @GetMapping("list")
    public R<List<DishDto>> getDishListByCategoryId(@RequestParam("categoryId") Long categoryId, @RequestParam(value = "status", required = false) Integer status) {
        return dishService.getDishListByCategoryId(categoryId, status);
    }

    /*@GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);

        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

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
    }*/


}
