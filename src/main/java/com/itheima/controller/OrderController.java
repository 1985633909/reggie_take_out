package com.itheima.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.R;
import com.itheima.entity.Orders;
import com.itheima.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @param orders 订单数据
     * @return 是否成功
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/page")
    public R<Page<Orders>> listPage(@RequestParam("page") Long page, @RequestParam("pageSize") Long pageSize,
                                    @RequestParam(value = "number",required = false) String number,
                                    @RequestParam(value = "beginTime",required = false)String beginTime,
                                    @RequestParam(value = "endTime",required = false) String endTime){
        Page<Orders> pageInfo = orderService.query()
                .ge(beginTime != null,"order_time",beginTime)
                .le(endTime != null,"order_time",endTime)
                .eq(number != null,"number",number)
                .page(new Page<>(page, pageSize));
        return R.success(pageInfo);

    }

}