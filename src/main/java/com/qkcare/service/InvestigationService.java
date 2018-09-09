package com.qkcare.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.qkcare.model.BaseEntity;
import com.qkcare.model.Investigation;
import com.qkcare.model.InvestigationTest;


@Service(value="investigationService")
public interface InvestigationService {
	
	public BaseEntity save(Investigation investigation);
	
	public void saveInvestigationTests(List<InvestigationTest> investigationTests);
	
}
