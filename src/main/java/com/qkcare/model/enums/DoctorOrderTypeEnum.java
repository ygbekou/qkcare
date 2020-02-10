package com.qkcare.model.enums;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qkcare.domain.GenericVO;
import com.qkcare.util.CacheUtil;

import net.sf.ehcache.Element;

public enum DoctorOrderTypeEnum {
	Pharmacie(1),
	Laboratoire(2),
	Medical(3),
	Lit(4),
	Autre(5); 
	private static Map<Integer, String> typeMap = new HashMap<>();
    private Integer type;
	
    DoctorOrderTypeEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
    
    public String getDescription() {
    	if (typeMap.isEmpty()) {
    		this.populateMap();
    	}
    	return this.typeMap.get(this.getType());
    }
    
    private void populateMap() {
    	Element ele = CacheUtil.CACHE_MANAGER.getCache(CacheUtil.DOCTOR_ORDER_TYPE).get("active");
		List<GenericVO> results = (List<GenericVO>) ele.getObjectValue();
		for (GenericVO genericVO : results) {
			this.typeMap.put(genericVO.getId().intValue(), genericVO.getName());
		}
    }
  
}
