package com.itheima.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.R;
import com.itheima.dto.SetmealDto;
import com.itheima.entity.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 19856
 * @description:
 * @since 2022/12/28-13:08
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 分页查询
     * @param page 当前页
     * @param pageSize 每页大小
     * @return 分页数据
     */
    @GetMapping("/page")
    public R<Page<SetmealDto>> pageList(@RequestParam("page") Long page, @RequestParam("pageSize") Long pageSize,@RequestParam(value = "name",required = false) String name){
        return setmealService.pageList(page,pageSize,name);
    }

    /**
     * 保存套餐信息(setmeal)以及套餐包含的菜品信息(setmeal_dish)
     * @param setmealDto setmealDto 包含 setmeal 与list<setmealDishe>
     * @return 成功或失败
     */
    @PostMapping
    public R<String> saveSetmeal(@RequestBody SetmealDto setmealDto){
        setmealService.saveSetmeal(setmealDto);
        return R.success("保存成功");
    }

    /**
     * 修改菜品状态/批量修改
     * @param ids 菜品id
     * @param status 修改的菜品状态
     * @return 是否修改成功
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@RequestParam("ids") List<Long> ids, @PathVariable String status){
        return setmealService.updateStatus(ids,status);
    }

    /**
     * 删除套餐
     * @param ids 菜品id
     * @return 是否修改成功
     */
    @DeleteMapping
    public R<String> deleteSetmeal(@RequestParam("ids") List<Long> ids){
        return setmealService.deleteSetmeal(ids);
    }

    /**
     * 根据id获取套餐信息setmeal 与setmealDish
     * @param id 数据id
     * @return 套餐信息
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getSetmealAndSetmealDishById(@PathVariable("id") Long id){
        return setmealService.getSetmealAndSetmealDishById(id);
    }

    /**
     * 修改套餐信息与套餐内菜品信息
     * @param setmealDto  包含套餐信息与 套餐内菜品信息
     * @return 是否成功
     */
    @PutMapping
    public R<String> updateSetmealAndSetmealDish(@RequestBody SetmealDto setmealDto){
        return setmealService.updateSetmealAndSetmealDish(setmealDto);
    }

}
