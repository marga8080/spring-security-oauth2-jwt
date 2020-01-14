package com.soj.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soj.config.custom.OAuthUserDetails;
import com.soj.mapper.UserMapper;
import com.soj.model.User;

@Service
public class UserService extends ServiceImpl<UserMapper, User> implements UserDetailsService {

	public User getByUsername(String userName) {
		QueryWrapper<User> query = new QueryWrapper<>();
		query.eq("username", userName);
		return this.getOne(query);
	}

	/**
	 * 查询用户信息
	 *
	 * @param username 用户登录名
	 *
	 * @return 用户信息，不允许为null
	 *
	 * @throws UsernameNotFoundException 如果找不到用户或用户没有GrantedAuthority
	 */
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = this.getByUsername(userName);
		if (user == null) {
			throw new UsernameNotFoundException("用户不存在");
		}
		OAuthUserDetails oauthUser = new OAuthUserDetails();
		oauthUser.setId(user.getId());
		oauthUser.setUsername(userName);
		oauthUser.setName(user.getName());
		oauthUser.setPassword(user.getPassword());
		return oauthUser;
	}
	
	
	public UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException {
		QueryWrapper<User> query = new QueryWrapper<>();
		query.eq("phone", phone);
		User user = this.getOne(query);
		if (user == null) {
			throw new UsernameNotFoundException("系统中不存在该手机号码");
		}
		OAuthUserDetails oauthUser = new OAuthUserDetails();
		oauthUser.setId(user.getId());
		oauthUser.setUsername(user.getUsername());
		oauthUser.setName(user.getName());
		oauthUser.setPassword(user.getPassword());
		return oauthUser;
	}
}
