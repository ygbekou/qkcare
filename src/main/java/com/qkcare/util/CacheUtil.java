package com.qkcare.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.javatuples.Quartet;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.qkcare.domain.GenericVO;
import com.qkcare.service.GenericService;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@Component
public class CacheUtil implements InitializingBean {
	
	public static String SYMPTOM = "symptom";
	public static String ALLERGY = "allergy";
	public static String MEDICAL_HISTORY = "medicalhistory";
	public static String SOCIAL_HISTORY = "socialhistory";
	public static String DISCHARGE_REASON = "dischargereason";
	public static String LAB_TEST_GROUP = "labTestGroup";
	public static String LAB_TEST = "labTest";
	public static String LAB_TEST_METHOD = "labTestMethod";
	public static String LAB_TEST_UNIT = "labTestUnit";
	
	
	@Autowired 
	@Qualifier("genericService")
	GenericService genericService;
	
	public static CacheManager CACHE_MANAGER = null;
	
	public CacheUtil() {
		
	}
	
	public List<GenericVO> getCategoryReferences(String query) {
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		List<Object[]> list = this.genericService.getNativeByCriteria(query, 
				paramTupleList, null, null);
		Map<GenericVO, List<GenericVO>> resultMap = new HashMap<>();
		List<GenericVO> results = new ArrayList<>();
		Map<Long, GenericVO> keyMap = new HashMap<Long, GenericVO>();
		
		for (Object[] objects : list) {
			Long cateoryId = new Long(objects[0].toString());
			String cateoryName = objects[1].toString();
			Long allergyId = new Long(objects[2].toString());
			String allergyName = objects[3].toString();
			if (keyMap.get(cateoryId) == null) {
				keyMap.put(cateoryId, new GenericVO(cateoryId, cateoryName));
			}
			
			GenericVO keyVO = keyMap.get(cateoryId);
			if (resultMap.get(keyVO) == null) {
				resultMap.put(keyVO, new ArrayList<>());
			}
			resultMap.get(keyVO).add(new GenericVO(allergyId, allergyName));
		}
		
		for (GenericVO vo : resultMap.keySet()) {
			vo.setChilds(resultMap.get(vo));
		}
		return new ArrayList(resultMap.keySet());
	}

	public List<GenericVO> getReferences(String query) {
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		List<Object[]> list = this.genericService.getNativeByCriteria(query, 
				paramTupleList, null, null);
		List<GenericVO> results = new ArrayList<>();
		
		for (Object[] objects : list) {
			Long elementId = new Long(objects[0].toString());
			String elementName = objects[1].toString();
			results.add(new GenericVO(elementId, elementName));
		}
		
		return results;
	}

	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		CACHE_MANAGER = CacheManager.getInstance();
		
		this.addCacheToManager(SYMPTOM, "SELECT C.CATEGORY_ID, C.NAME CAT_NAME, SYMPTOM_ID, S.NAME SYMP_NAME "
				+ "FROM SYMPTOM S "
				+ "JOIN CATEGORY C ON S.CATEGORY_ID = C.CATEGORY_ID "
				+ "WHERE S.STATUS = 0 AND C.STATUS = 0", this::getCategoryReferences);
		
		this.addCacheToManager(ALLERGY, "SELECT C.CATEGORY_ID, C.NAME CAT_NAME, ALLERGY_ID, A.NAME ALL_NAME "
				+ "FROM ALLERGY A "
				+ "JOIN CATEGORY C ON A.CATEGORY_ID = C.CATEGORY_ID "
				+ "WHERE A.STATUS = 0 AND C.STATUS = 0", this::getCategoryReferences);
		
		this.addCacheToManager(MEDICAL_HISTORY, "SELECT MEDICALHISTORY_ID, NAME "
				+ "FROM MEDICALHISTORY "
				+ "WHERE STATUS = 0", this::getReferences);
		
		this.addCacheToManager(SOCIAL_HISTORY, "SELECT SOCIALHISTORY_ID, NAME "
				+ "FROM SOCIALHISTORY "
				+ "WHERE STATUS = 0", this::getReferences);
		
		this.addCacheToManager(DISCHARGE_REASON, "SELECT DISCHARGE_REASON_ID, NAME "
				+ "FROM DISCHARGE_REASON "
				+ "WHERE STATUS = 0", this::getReferences);
		
		this.addCacheToManager(LAB_TEST_GROUP, "SELECT LAB_TEST_ID, NAME "
				+ "FROM LAB_TEST "
				+ "WHERE PARENT_ID IS NULL AND STATUS = 0", this::getReferences);
		
		this.addCacheToManager(LAB_TEST, "SELECT LAB_TEST_ID, NAME "
				+ "FROM LAB_TEST "
				+ "WHERE STATUS = 0", this::getReferences);
		
		this.addCacheToManager(LAB_TEST_METHOD, "SELECT LAB_TEST_METHOD_ID, NAME "
				+ "FROM LAB_TEST_METHOD "
				+ "WHERE STATUS = 0", this::getReferences);
		
		this.addCacheToManager(LAB_TEST_UNIT, "SELECT LAB_TEST_UNIT_ID, NAME "
				+ "FROM LAB_TEST_UNIT "
				+ "WHERE STATUS = 0", this::getReferences);
		
	}
	
	
	private void addCacheToManager(String cacheKey, String sqlQuery, Function<String, List<GenericVO>> getCatg) {
		CACHE_MANAGER.addCache(cacheKey);
		Cache cache = CACHE_MANAGER.getCache(cacheKey);
		cache.put(new Element("active", getCatg.apply(sqlQuery), true));
	}
	

}
