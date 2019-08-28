package com.qkcare.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.qkcare.domain.SearchCriteria;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.Investigation;
import com.qkcare.model.InvestigationTest;
import com.qkcare.model.LabTest;
import com.qkcare.model.imaging.RadInvestigation;


@Service(value="radInvestigationService")
public interface RadInvestigationService {
	
	public BaseEntity save(RadInvestigation investigation);
	
	public List<BaseEntity> getInvestigations(SearchCriteria searchCriteria);
	
}
