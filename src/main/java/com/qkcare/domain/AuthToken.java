package com.qkcare.domain;

import java.util.List;

public class AuthToken {

	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String token;
	private String roleName;
	private String picture;
	private String firstTimeLogin;
	private List authorities;
	private List<MenuVO> menus;
	private Long userId;
	private String homePage;
	private List<PermissionVO> nonMenuPermissions;

	public AuthToken() {

	}

	public AuthToken(String token, String userName, String password, String firstName, String lastName, String roleName,
			String picture, String firstTimeLogin, List authorities, List<MenuVO> menus,
			List<PermissionVO> nonMenuPermissions, Long userId, String homePage) {
		this.token = token;
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.roleName = roleName;
		this.picture = picture;
		this.firstTimeLogin = firstTimeLogin;
		this.authorities = authorities;
		this.menus = menus;
		this.nonMenuPermissions = nonMenuPermissions;
		this.userId = userId;
		this.homePage = homePage;
	}

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getFirstTimeLogin() {
		return firstTimeLogin;
	}

	public void setFirstTimeLogin(String firstTimeLogin) {
		this.firstTimeLogin = firstTimeLogin;
	}

	public List getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List authorities) {
		this.authorities = authorities;
	}

	public List<MenuVO> getMenus() {
		return menus;
	}

	public void setMenus(List<MenuVO> menus) {
		this.menus = menus;
	}

	public List<PermissionVO> getNonMenuPermissions() {
		return nonMenuPermissions;
	}

	public void setNonMenuPermissions(List<PermissionVO> nonMenuPermissions) {
		this.nonMenuPermissions = nonMenuPermissions;
	}

}
