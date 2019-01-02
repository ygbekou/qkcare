package com.qkcare.service;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.javatuples.Quartet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.qkcare.dao.GenericDao;
import com.qkcare.model.BaseEntity;
import com.qkcare.util.Constants;

@Service(value="genericService")
public class GenericServiceImpl implements GenericService {
	
	Map<String, List<String>> entityCascades;
	
	@Autowired
	GenericDao<BaseEntity, String> genericDao;
	
	public GenericServiceImpl() {
		this.entityCascades = new HashMap<String, List<String>>();
		this.entityCascades.put("Department", Arrays.asList(new String[]{"User"}));
	}
	
	@Transactional
	public BaseEntity save(BaseEntity entity) {	

		entity.setModDate(new Date());                    
		entity.setModifiedBy(1L);    
		if (entity.getId() == null) {
			entity.setCreateDate(new Date());                  
			return this.genericDao.persist(entity);
		} else {	
			return this.genericDao.merge(entity);
		}
				             
	}
	
	@Transactional
	public BaseEntity saveWithFiles(BaseEntity entity, List<MultipartFile> files, List<String> attributes) {
		this.save(entity);
		
		try {
			int i = 0;
			for (MultipartFile file : files) {
				String fileName = saveFile(file, entity.getClass().getSimpleName(), attributes.get(i));
				Field field = entity.getClass().getDeclaredField(attributes.get(i));
				field.setAccessible(true);
		        field.set(entity, fileName);
		        this.save(entity);
		        i++;
			}
		} catch(Exception ex) {
			
		}
				
		return entity;
	}
	
	@Transactional
	public void delete(BaseEntity entity) {
		this.genericDao.delete(entity);
	}
	
	@Transactional
	public void deleteCascade(String parentEntity, String entityName, Long id) {
		String query = "DELETE FROM " + entityName + " WHERE " + parentEntity + "_ID IN (SELECT " + parentEntity ;
		List<String> childEntities = this.entityCascades.get(entityName);
		for (String childEntity : childEntities) {
			deleteCascade(entityName, childEntity, id);
		}
		//this.genericDao.delete(entity);
	}
	
	@Transactional
	public void delete(Class cl, List<Long> ids) {
		this.genericDao.delete(cl, ids);
	}

	public BaseEntity find(Class cl, Long key) {
		return (BaseEntity) this.genericDao.find(cl, key);
	}

	public List<BaseEntity> getAll(Class cl) {
		return this.genericDao.getAll(cl);
	}
	
	public List<BaseEntity> getByCriteria(String queryStr, List<Quartet<String, String, String, String>> parameters, 
			String orderBy) {
		return this.genericDao.getByCriteria(queryStr, parameters, orderBy);
	}

	public List<Object[]> getNativeByCriteria(String queryStr, List<Quartet<String, String, String, String>> parameters, 
			String orderBy, String groupBy) {
		return this.genericDao.getNativeByCriteria(queryStr, parameters, orderBy, groupBy);
	}
	
	public Integer deleteByCriteria(String queryStr, List<Quartet<String, String, String, String>> parameters) {
		return this.genericDao.deleteByCriteria(queryStr, parameters);
	}
	
	public Session getConnection() {
		return this.genericDao.getConnection();
	}
	
	private String saveFile(MultipartFile file, String entityName, String fileLabel) {
		if (!file.isEmpty()) {
			try {
				String originalFileExtension = file.getOriginalFilename()
						.substring(file.getOriginalFilename().lastIndexOf("."));
	
				// transfer to upload folder
				String storageDirectory = null;
				if (entityName != null) {					
					storageDirectory = Constants.DOC_FOLDER	+ entityName + File.separator;
					File dir = new File(storageDirectory);
					if (!dir.exists()) {
						dir.mkdirs();
					}
	
				} else {
					throw new Exception("Unknown");
				}
				
				String newFilename = null;
				newFilename = fileLabel + originalFileExtension;
				
				File newFile = new File(storageDirectory + "/" + newFilename);
		        file.transferTo(newFile);
		        
		        return newFilename;
		        
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
}
