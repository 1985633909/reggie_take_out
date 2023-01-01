package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.common.R;
import com.itheima.entity.ShoppingCart;

public interface ShoppingCartService extends IService<ShoppingCart> {

    R<String> saveShoppCart(ShoppingCart shoppingCart);
}
