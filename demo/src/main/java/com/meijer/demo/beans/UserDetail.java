package com.meijer.demo.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user_detail")
public class UserDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "USER_DETAIL_SEQ_GEN")
	@SequenceGenerator(name = "USER_DETAIL_SEQ_GEN", sequenceName = "USER_DETAIL_SEQ", allocationSize = 1)
	int id;
	@Column
	String name;
	@Column
	String phone;
	@Column
	String email;
	
	@OneToOne
	@JoinColumn(name = "USER_ID")
	@JsonIgnore
	User user;

	public UserDetail() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "UserDetail [id=" + id + ", name=" + name + ", phone=" + phone + ", email=" + email + ", user=" + user + "]";
	}

}
