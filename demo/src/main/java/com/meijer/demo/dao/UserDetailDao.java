package com.meijer.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meijer.demo.beans.UserDetail;

public interface UserDetailDao extends JpaRepository<UserDetail, Integer> {

}