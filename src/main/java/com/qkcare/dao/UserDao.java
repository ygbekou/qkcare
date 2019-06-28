package com.qkcare.dao;

import java.util.List;

import com.qkcare.model.User;

public interface UserDao {
	public User getUser(String email, String userName, String password);
	public List<User> findMembers(String firstName, String lastName, String login, String email);

}
