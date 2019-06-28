package com.qkcare.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.qkcare.model.BaseEntity;
import com.qkcare.model.User;

@Service(value="userService")
public interface UserService {
	
	public BaseEntity save(BaseEntity entity, MultipartFile file) throws Exception;
	
	public User getUser(String email, String userName, String password);
}
