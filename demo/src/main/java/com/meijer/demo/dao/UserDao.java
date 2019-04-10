package com.meijer.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meijer.demo.beans.User;

//@RestResource(path="users", rel="users")
public interface UserDao  extends JpaRepository<User, Integer>{
	User findByUsername(String username);
}