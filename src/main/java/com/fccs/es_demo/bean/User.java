package com.fccs.es_demo.bean;

import java.util.Date;


import com.fasterxml.jackson.annotation.JsonFormat;

public class User {
	private String name;
	private int age;
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date birthday;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
}
