package com.qkcare.dao;

import java.sql.Connection;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.tuple.Triple;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.javatuples.Quartet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.qkcare.model.BaseEntity;
import com.qkcare.util.DateUtil;

@SuppressWarnings("unchecked")
@Repository
public class GenericDaoImpl<E, K> implements GenericDao<E, K> {
	private static Logger logger = Logger.getLogger(GenericDaoImpl.class) ;
	
	@Autowired
	private EntityManager entityManager;

	public E persist(E entity) {
	    entityManager.persist(entity);
		return entity;
	}

	public E merge(E entity) {
	    entityManager.merge(entity);
		return entity;
	}
	
	public void delete(E entity) {
		entityManager.remove(entity);
	}

	public void delete(Class cl, List<Long> ids) {
		for (Long id : ids) {
			this.delete(this.find(cl, id));
		}
	}
	
	public E find(Class cl, Long key) {
		return (E) entityManager.find(cl, key);
	}

	public List<E> getAll(Class cl) {
		CriteriaQuery<E> criteria = entityManager.getCriteriaBuilder().createQuery(cl);
	    criteria.select(criteria.from(cl));
	    List<E> ListOfEmailDomains = entityManager.createQuery(criteria).getResultList();
	    return ListOfEmailDomains;
	}

	public List<E> getByCriteria(String queryStr, List<Quartet<String, String, String, String>> parameters, String orderBy) {
		Query query = this.buildQuery(queryStr, parameters, orderBy, null, false);
	    List<E> ListOfEmailDomains = query.getResultList();
	    return ListOfEmailDomains;
	}
	
	public List<E> getByCriteria(String queryStr, List<Quartet<String, String, String, String>> parameters, String orderBy, int maxResult) {
		Query query = this.buildQuery(queryStr, parameters, orderBy, null, false);
		query.setMaxResults(maxResult);
	    List<E> ListOfEmailDomains = query.getResultList();
	    return ListOfEmailDomains;
	}
	
	
	public List<Object[]> getNativeByCriteria(String queryStr, List<Quartet<String, String, String, String>> parameters, 
			String orderBy, String groupBy) {		
		Query query = this.buildQuery(queryStr, parameters, orderBy, groupBy, true);		
	    List<Object[]> list = query.getResultList();
	    return list;
	}
	
	public Integer deleteByCriteria(String queryStr, List<Quartet<String, String, String, String>> parameters) {
		Query query = this.buildQuery(queryStr, parameters, null, null, false);
	    Integer nbDel = query.executeUpdate();
	    return nbDel;
	}

	private Query buildQuery(String queryStr, List<Quartet<String, String, String, String>> parameters, String orderBy, 
			String groupBy, boolean nativeQuery) {
		StringBuilder queryBuilder = new StringBuilder(queryStr);
		
		// Build the query
		for (Quartet<String, String, String, String> parameter : parameters) {
			queryBuilder.append(" AND " + parameter.getValue0() + " :" + parameter.getValue1());
		}
		
		if (groupBy != null) {
			queryBuilder.append(groupBy);
		} 
		
		if (orderBy != null) {
			queryBuilder.append(orderBy);
		}
		
		
		Query query = null;
		
		if (!nativeQuery)
			query = entityManager.createQuery(queryBuilder.toString());
		else 
			query = entityManager.createNativeQuery(queryBuilder.toString());
		
		// Set the parameters on the query
		for (Quartet<String, String, String, String> parameter : parameters) {
			if ("Long".equals(parameter.getValue3())) {
				query.setParameter(parameter.getValue1(), new Long(parameter.getValue2()));
			} else if ("Integer".equals(parameter.getValue3())) {
				query.setParameter(parameter.getValue1(), new Integer(parameter.getValue2()));
			} else if ("String".equals(parameter.getValue3())) {
				query.setParameter(parameter.getValue1(), parameter.getValue2());
			} else if ("List".equals(parameter.getValue3())) {
				query.setParameter(parameter.getValue1(), Arrays.asList(parameter.getValue2().split("\\s*,\\s*"))
						.stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList()));
			} else if ("List<String>".equals(parameter.getValue3())) {
				query.setParameter(parameter.getValue1(), Arrays.asList(parameter.getValue2().split("\\s*,\\s*"))
						.stream().map(s -> s.trim()).collect(Collectors.toList()));
			} else if ("Date".equals(parameter.getValue3())) {
				try {
					query.setParameter(parameter.getValue1(), DateUtil.parseDate(parameter.getValue2(), "MM/dd/yyyy"), TemporalType.DATE);
				} catch(ParseException pe) {
					logger.error("Exception happened: " + pe);
				}
			} else if ("Date2".equals(parameter.getValue3())) {
				try {
					query.setParameter(parameter.getValue1(), DateUtil.parseDate(parameter.getValue2(), "MM-dd-yyyy"), TemporalType.DATE);
				} catch(ParseException pe) {
					logger.error("Exception happened: " + pe);
				}
			} else if ("Timestamp".equals(parameter.getValue3())) {
				try {
					query.setParameter(parameter.getValue1(), DateUtil.parseDate(parameter.getValue2(), "MM/dd/yyyy hh:mm:ss a"), TemporalType.TIMESTAMP);
				} catch(ParseException pe) {
					logger.error("Exception happened: " + pe);
				}
			}
		}
		
		return query;
		
	}
	
	
	public Session getConnection() {
		return entityManager.unwrap(Session.class);
	}

}
