package com.soj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soj.config.custom.OAuthUserDetails;
import com.soj.mapper.UserMapper;
import com.soj.model.User;

/**
 * 用户服务
 * 
 * @author mawei
 */
@Service
public class CustomUserService implements UserDetailsService {

	
	@Autowired
    private UserMapper userMapper;
	
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
		User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", userName));
		if (user == null) {
			throw new UsernameNotFoundException("用户不存在");
		}
		OAuthUserDetails oauthUser = new OAuthUserDetails();
		oauthUser.setUnid(user.getUnid());
		oauthUser.setUsername(userName);
		oauthUser.setName(user.getName());
		oauthUser.setPassword(user.getPassword());
		return oauthUser;
	}
	
	
	public UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException {
		User user = userMapper.selectOne(new QueryWrapper<User>().eq("phone", phone));
		if (user == null) {
			throw new UsernameNotFoundException("系统中不存在该手机号码");
		}
		OAuthUserDetails oauthUser = new OAuthUserDetails();
		oauthUser.setUnid(user.getUnid());
		oauthUser.setUsername(user.getUsername());
		oauthUser.setName(user.getName());
		oauthUser.setPassword(user.getPassword());
		return oauthUser;
	}
	
}
