package com.qkcare.service;

import java.util.List;

import org.javatuples.Pair;
import org.springframework.stereotype.Service;

import com.qkcare.domain.MenuVO;
import com.qkcare.domain.PermissionVO;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.User;
import com.qkcare.model.authorization.Role;


@Service(value="authorizationService")
public interface AuthorizationService {
	
	public BaseEntity saveUserRoles(User user);
	
	public BaseEntity getUserById(Long id);
	
	public BaseEntity savePermissions(Role role);
	
	public BaseEntity getRoleById(Long id);
	
	public Pair<List<MenuVO>, List<PermissionVO>> getUserResources(Long userId, String lang);
}
