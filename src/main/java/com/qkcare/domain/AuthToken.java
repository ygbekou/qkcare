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

    public AuthToken(){

    }

    public AuthToken(String token, String userName, String password, 
    		String firstName, String lastName, String roleName,
    		String picture, String firstTimeLogin, List authorities, 
    		List<MenuVO> menus){
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
	
}
