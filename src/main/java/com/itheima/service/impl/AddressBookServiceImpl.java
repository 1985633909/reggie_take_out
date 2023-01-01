package com.itheima.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.Mapper.AddressBookMapper;
import com.itheima.entity.AddressBook;
import com.itheima.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author 19856
 * @description:
 * @since 2022/12/31-14:04
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
