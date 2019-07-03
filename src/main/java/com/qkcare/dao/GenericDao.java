package com.qkcare.dao;

import java.sql.Connection;
import java.util.List;

import org.hibernate.Session;
import org.javatuples.Quartet;

import com.qkcare.model.BaseEntity;

public interface GenericDao<E,K> {
	public E persist(E entity);
	public E merge(E entity);
	public void delete(E entity);
	public void delete(Class cl, List<Long> ids);
	public E find(Class cl, Long key);
	public List<E> getAll(Class cl);
	public List<E> getByCriteria(String queryStr, List<Quartet<String, String, String, String>> parameters, String orderBy);
	public List<E> getByCriteria(String queryStr, List<Quartet<String, String, String, String>> parameters, String orderBy, int maxResult);

	public List<Object[]> getNativeByCriteria(String queryStr, List<Quartet<String, String, String, String>> parameters, 
			String orderBy, String groupBy);
	public Integer deleteByCriteria(String queryStr, List<Quartet<String, String, String, String>> parameters);
	public Session getConnection();
	public List<BaseEntity> getByCriteria(Class<? extends BaseEntity> c, String parentName, Long parentId) ;

}
