package com.qkcare.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.qkcare.model.User;

@SuppressWarnings("unchecked")
@Repository
public class UserDaoImpl implements UserDao{
	private static Logger logger = Logger.getLogger(UserDaoImpl.class) ;
	@Autowired
	private EntityManager entityManager;

	public User getUser(String email, String userName, String password) {
		User user = null;
		if(userName == null){
			userName = "";
		}
		if(email == null){
			email = "";
		}
		List list = entityManager.createQuery("SELECT u FROM User u WHERE (u.email = :email OR u.userName = :userName) ")
			    .setParameter("email", email)
			    .setParameter("userName", userName)
			    .getResultList();		
		if (list.size() > 0) {
			user = (User) list.get(0);
		}
		return user;
	}


}
