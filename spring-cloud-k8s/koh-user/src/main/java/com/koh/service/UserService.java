package com.koh.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.koh.entity.User;
import com.koh.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService extends ServiceImpl<UserMapper, User> {
}
