package com.meijer.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.meijer.demo.beans.User;
import com.meijer.demo.beans.UserDetail;
import com.meijer.demo.dao.UserDao;
import com.meijer.demo.dao.UserDetailDao;
import com.meijer.demo.http.Response;
import com.meijer.demo.http.UserDetailResponse;


@Service
public class UserDetailService {

	@Autowired
	UserDao userDao;
	
	@Autowired
	UserDetailDao userDetailDao;
	
	public Response addUserDetail(UserDetail userDetail, Authentication authentication) {
		User user = userDao.findByUsername(authentication.getName());
		userDetail.setUser(user);
		return new UserDetailResponse(true, userDetailDao.save(userDetail));
	}
	
	public Response updateUserDetail(UserDetail userDetail) {
		UserDetail ud = userDetailDao.findById(userDetail.getId()).get();
		userDetail.setUser(ud.getUser());
		ud = userDetail;
		userDetailDao.save(ud);
		return new Response(true);
	}
}
