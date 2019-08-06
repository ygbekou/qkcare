package com.qkcare.service;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
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
import com.qkcare.model.Company;
import com.qkcare.util.Constants;

@Service(value = "genericService")
public class GenericServiceImpl implements GenericService {

	Map<String, List<String>> entityCascades;

	@Autowired
	GenericDao<BaseEntity, String> genericDao;

	public GenericServiceImpl() {
		this.entityCascades = new HashMap<String, List<String>>();
		this.entityCascades.put("Department", Arrays.asList(new String[] { "User" }));
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
	public BaseEntity saveWithFiles(BaseEntity entity, List<MultipartFile> files, List<String> attributes,
			boolean useId) {
		this.save(entity);

		try {
			int i = 0;
			for (MultipartFile file : files) {
				String fileName = saveFile(file, entity.getClass().getSimpleName(),
						useId ? entity.getId() + "" : attributes.get(i));
				Field field = entity.getClass().getDeclaredField(attributes.get(i));
				field.setAccessible(true);
				field.set(entity, fileName);
				this.save(entity);
				i++;
			}
		} catch (Exception ex) {

		}

		return entity;
	}

	@Transactional
	public void delete(BaseEntity entity) {
		this.genericDao.delete(entity);
	}

	@Transactional
	public void deleteCascade(String parentEntity, String entityName, Long id) {
		String query = "DELETE FROM " + entityName + " WHERE " + parentEntity + "_ID IN (SELECT " + parentEntity;
		List<String> childEntities = this.entityCascades.get(entityName);
		for (String childEntity : childEntities) {
			deleteCascade(entityName, childEntity, id);
		}
		// this.genericDao.delete(entity);
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

	public List<BaseEntity> getByCriteria(String queryStr, List<Quartet<String, String, String, String>> parameters,
			String orderBy, int maxResult) {
		return this.genericDao.getByCriteria(queryStr, parameters, orderBy, maxResult);
	}

	public List<Object[]> getNativeByCriteria(String queryStr, List<Quartet<String, String, String, String>> parameters,
			String orderBy, String groupBy) {
		return this.genericDao.getNativeByCriteria(queryStr, parameters, orderBy, groupBy);
	}

	public Integer deleteByCriteria(String queryStr, List<Quartet<String, String, String, String>> parameters) {
		return this.genericDao.deleteByCriteria(queryStr, parameters);
	}
	
	public Integer deleteNativeByCriteria(String queryStr, List<Quartet<String, String, String, String>> parameters) {
		return this.genericDao.deleteNativeByCriteria(queryStr, parameters);
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
					storageDirectory = Constants.IMAGE_FOLDER + entityName.toLowerCase() + File.separator;
					File dir = new File(storageDirectory);
					if (!dir.exists()) {
						dir.mkdirs();
					}

				} else {
					throw new Exception("Unknown");
				}

				String newFilename = null;
				newFilename = fileLabel;

				File newFile = new File(storageDirectory + "/" + newFilename);
				file.transferTo(newFile);

				return newFilename;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public Company getCompany(String language) {
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();

		paramTupleList.add(Quartet.with("c.language = ", "language", language, "String"));
		paramTupleList.add(Quartet.with("c.status = ", "status", "0", "Integer"));
		String queryStr = "SELECT c FROM Company c WHERE 1 = 1";
		List<BaseEntity> companies = this.getByCriteria(queryStr, paramTupleList, null);
		if (companies.size() > 0) {
			return (Company) companies.get(0);
		}

		return null;

	}

	@Transactional
	public BaseEntity saveWithFiles(BaseEntity entity, List<MultipartFile> files, boolean useId,
			List<String> attributeNames) {
		this.save(entity);

		try {
			int i = 0;
			for (MultipartFile file : files) {
				String originalFileExtension = file.getOriginalFilename()
						.substring(file.getOriginalFilename().lastIndexOf("."));

				String fileName = saveFile(file, entity.getClass().getSimpleName(),
						useId ? entity.getId() + originalFileExtension : file.getOriginalFilename());

				String fieldName = useId ? attributeNames.get(i) : file.getOriginalFilename().split("\\.")[0];

				Field field = entity.getClass().getDeclaredField(fieldName);
				field.setAccessible(true);
				field.set(entity, fileName);
				this.save(entity);
				i++;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return entity;
	}

	public List<BaseEntity> getByCriteria(Class<? extends BaseEntity> c, String parentName, Long parentId) {
		return genericDao.getByCriteria(c, parentName, parentId);
	}
}
