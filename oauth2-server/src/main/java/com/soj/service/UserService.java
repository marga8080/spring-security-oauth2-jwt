package com.soj.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soj.mapper.UserMapper;
import com.soj.model.User;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

	public User getByUsername(String userName) {
		QueryWrapper<User> query = new QueryWrapper<>();
		query.eq("username", userName);
		return this.getOne(query);
	}

	
}
