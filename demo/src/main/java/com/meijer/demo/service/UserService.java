package com.meijer.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meijer.demo.beans.User;
import com.meijer.demo.beans.UserProfile;
import com.meijer.demo.dao.UserDao;
import com.meijer.demo.http.Response;
import com.meijer.demo.security.SecurityUtils;


@Service
@Transactional
public class UserService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Autowired
	UserDao userDao;

	@Autowired
	PasswordEncoder passwordEncoder;
	
//	@Autowired
//	EmailService emailService;

	public Response register(User user) {
		// TODO: validation.
		try {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			List<UserProfile> profiles = new ArrayList<UserProfile>();
			profiles.add(new UserProfile(2));
			user.setProfiles(profiles);
			userDao.save(user);
			// TODO: send email. assume username is the email for now
//			emailService.sendSimpleMessage(user.getUsername(), "Account Created", user.getUsername() + ", your account is created!");
			return new Response(true);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new Response(false, 400, e.getMessage());
		}
		
	}
	
	public Response changePassword(User user, Authentication authentication) {
		if(user.getUsername().equals(authentication.getName()) || SecurityUtils.isAdmin(authentication.getAuthorities())) {
			User u = userDao.findByUsername(user.getUsername());
			u.setPassword(passwordEncoder.encode(user.getPassword()));
			userDao.save(u);
		}else {
			//TODO: Not authorize to update password if not current loggedin user or admin.
			return new Response(false);
		}
		return new Response(true);
	}
	
	public Response deleteUser(int id) {
		if(userDao.findById(id).get() != null) {
			userDao.deleteById(id);
			return new Response(true);
		}else {
			return new Response(false, "User is not found!");
		}
	}

}
