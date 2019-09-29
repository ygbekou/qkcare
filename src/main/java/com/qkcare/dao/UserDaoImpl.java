package com.qkcare.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.qkcare.model.User;

@SuppressWarnings("unchecked")
@Repository
public class UserDaoImpl implements UserDao {
	private static Logger logger = Logger.getLogger(UserDaoImpl.class);
	@Autowired
	private EntityManager entityManager;

	public User getUser(String email, String userName, String password) {
		User user = null;
		if (userName == null) {
			userName = "";
		}
		if (email == null) {
			email = "";
		}
		List list = entityManager
				.createQuery("SELECT u FROM User u WHERE (u.email = :email OR u.userName = :userName) ")
				.setParameter("email", email).setParameter("userName", userName).getResultList();
		if (list.size() > 0) {
			user = (User) list.get(0);
		}
		return user;
	}

	public List<User> findMembers(String firstName, String lastName, String login, String email) {
		firstName = firstName == null ? "#" : firstName;
		lastName = lastName == null ? "#" : lastName;
		login = login == null ? "#" : login;
		email = email == null ? "#" : email;
		Query query = entityManager.createQuery(
				" FROM User U WHERE (UPPER(U.firstName) = ? AND UPPER(U.lastName) = ?) OR UPPER(U.userName)=? OR UPPER(U.email)=?");
		query.setParameter(1, firstName.toUpperCase());
		query.setParameter(2, lastName.toUpperCase());
		query.setParameter(3, login.toUpperCase());
		query.setParameter(4, email.toUpperCase());
		return query.getResultList();

	}

	public List<User> getExistingUser(String firstName, String lastName, String sex, Date birthDate) {
		firstName = firstName == null ? "#" : firstName;
		lastName = lastName == null ? "#" : lastName;
		Query query = entityManager.createQuery(" FROM User U WHERE (UPPER(U.firstName) = ? AND UPPER(U.lastName) = ?"
				+ " UPPER(U.sex)=? AND U.birthDate=?");
		query.setParameter(1, firstName.toUpperCase());
		query.setParameter(2, lastName.toUpperCase());
		query.setParameter(3, sex.toUpperCase());
		query.setParameter(4, birthDate);
		return query.getResultList();

	}

	@Override
	public User getTempUser(String userName, Date birthDate) {
		User user = null;
		if (userName == null) {
			userName = "";
		}
		List list = entityManager
				.createQuery("SELECT u FROM User u WHERE (u.birthDate = :birthDate AND u.userName = :userName) ")
				.setParameter("birthDate", birthDate).setParameter("userName", userName).getResultList();
		if (list.size() > 0) {
			user = (User) list.get(0);
		}
		return user;
	}

}
