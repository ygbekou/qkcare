package com.qkcare.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.qkcare.domain.SearchCriteria;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.imaging.RadInvestigation;


@Service(value="radInvestigationService")
public interface RadInvestigationService {
	
	public BaseEntity save(RadInvestigation investigation);
	
	public BaseEntity findInvestigation(Class cl, Long key);
	
	public List<BaseEntity> getInvestigations(SearchCriteria searchCriteria);
	
}
