package com.qkcare.service;

import java.util.List;

import org.hibernate.Session;
import org.javatuples.Quartet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.qkcare.model.BaseEntity;
import com.qkcare.model.Company;

@Service(value="genericService")
public interface GenericService {
	
	public BaseEntity save(BaseEntity entity);
	public BaseEntity saveWithFiles(BaseEntity entity, List<MultipartFile> files, List<String> attributes, boolean useId);
	public void delete(BaseEntity entity);
	public void delete(Class cl, List<Long> ids);
	public BaseEntity find(Class cl, Long key);
	public List<BaseEntity> getAll(Class cl);
	public List<BaseEntity> getByCriteria(String queryStr, List<Quartet<String, String, String, String>> parameters, 
			String orderBy);
	public List<Object[]> getNativeByCriteria(String queryStr, List<Quartet<String, String, String, String>> parameters, 
			String orderBy, String groupBy);
	public Integer deleteByCriteria(String queryStr, List<Quartet<String, String, String, String>> parameters);
	public Session getConnection();
	public Company getCompany(String language);
	public BaseEntity saveWithFiles(BaseEntity entity, List<MultipartFile> files, 
			boolean useId, List<String> attributeNames);
}
