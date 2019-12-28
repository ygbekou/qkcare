package com.qkcare.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.qkcare.model.BaseEntity;
import com.qkcare.model.User;

@Service(value="userService")
public interface UserService extends GenericService{
	
	public BaseEntity save(BaseEntity entity, MultipartFile file) throws Exception;
	
	public User getUser(String email, String userName, String password);

	public User getTempUser(String userName, Date birthDate);
	
	public String changePassword(User user, String password);
}
