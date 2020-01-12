package com.qkcare.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.javatuples.Quartet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.qkcare.model.BaseEntity;
import com.qkcare.model.Company;
import com.qkcare.model.Patient;
import com.qkcare.model.User;

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
	public List<BaseEntity> getByCriteria(String queryStr, List<Quartet<String, String, String, String>> parameters, 
			String orderBy, int maxResult);
	public List<Object[]> getNativeByCriteria(String queryStr, List<Quartet<String, String, String, String>> parameters, 
			String orderBy, String groupBy);
	public Integer deleteByCriteria(String queryStr, List<Quartet<String, String, String, String>> parameters);
	public Integer deleteNativeByCriteria(String queryStr, List<Quartet<String, String, String, String>> parameters);
	public Session getConnection();
	public Company getCompany(String language);
	public BaseEntity saveWithFiles(BaseEntity entity, List<MultipartFile> files, 
			boolean useId, List<String> attributeNames);
	public BaseEntity saveWithFiles(BaseEntity entity, List<MultipartFile> files, boolean useId,
			List<String> attributeNames, String folderName, boolean saveFilesOnly);
	public List<BaseEntity> getByCriteria(Class<? extends BaseEntity> c, String parentName, Long parentId);
	public List<String> readFiles(String entityName, String id);
	public void deleteFiles(String entityName, String id, List<String> fileNames) throws IOException;
	public List<Long> deriveAddedChilds(String parentTable, String parentEntity, String keyColumn, 
			Long parentId, Set<Long> selectedIds, String childEntity);
	public List<Long> deriveAddedChilds(String parentTable, String parentEntity, String keyColumn, 
			Long parentId, Set<Long> selectedIds, String childEntity, String childTable, String relationEntity, String relationTable);
	
	public String getHomePage(User user);
	
	public Patient getPatient(User user);
}
