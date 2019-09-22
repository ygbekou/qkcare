package com.qkcare.controller;


import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qkcare.domain.GenericDto;
import com.qkcare.domain.MenuVO;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.User;
import com.qkcare.model.authorization.Role;
import com.qkcare.model.authorization.Permission;
import com.qkcare.service.AuthorizationService;
import com.qkcare.service.GenericService;
import com.qkcare.util.Constants;


@RestController
@RequestMapping(value="/service/authorization")
@CrossOrigin
public class AuthorizationController extends BaseController {
	
		private static final Logger LOGGER = Logger.getLogger(AuthorizationController.class);
	
		@Autowired 
		@Qualifier("genericService")
		GenericService genericService;
		
		@Autowired 
		@Qualifier("authorizationService")
		AuthorizationService authorizationService;
		
		
		@RequestMapping(value="/userRoles/save",method = RequestMethod.POST)
		public BaseEntity saveUserRoles(@RequestBody GenericDto dto) throws JsonParseException, 
		JsonMappingException, IOException, ClassNotFoundException {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			BaseEntity obj = (BaseEntity) mapper.readValue(dto.getJson().replaceAll("'", "\"").replaceAll("/", "\\/"),
					Class.forName(Constants.PACKAGE_NAME + "User"));
			authorizationService.saveUserRoles((User)obj);
			
			return obj;
		}
		
		@RequestMapping(value="user/get/{id}",method = RequestMethod.GET)
		public BaseEntity getUserWithRoles(@PathVariable("id") Long id) throws ClassNotFoundException{
			BaseEntity result = authorizationService.getUserById(id);
			
			return result;
		}
		
		
		@RequestMapping(value="/permissions/save",method = RequestMethod.POST)
		public BaseEntity savePermissions(@RequestBody GenericDto dto) throws JsonParseException, 
		JsonMappingException, IOException, ClassNotFoundException {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			BaseEntity obj = (BaseEntity) mapper.readValue(dto.getJson().replaceAll("'", "\"").replaceAll("/", "\\/"),
					Class.forName(Constants.PACKAGE_AUTHORIZATION_NAME + "Role"));
			authorizationService.savePermissions((Role)obj);
			
			for (Permission permission : ((Role)obj).getPermissions() ) {
				permission.setRole(null);
			}
			
			return obj;
		}
		
		@RequestMapping(value="role/get/{id}",method = RequestMethod.GET)
		public BaseEntity getRoleWithResources(@PathVariable("id") Long id) throws ClassNotFoundException{
			Role role = (Role) authorizationService.getRoleById(id);
			
			for (Permission permission : role.getPermissions() ) {
				permission.setRole(null);
			}
			
			return role;
		}
		
		
		@RequestMapping(value="user/menus/{userId}",method = RequestMethod.GET)
		public List<MenuVO> getUserMenus(@PathVariable("userId") Long userId) throws ClassNotFoundException{
			
			return this.authorizationService.getUserResources(userId);
		}
		
		
}
