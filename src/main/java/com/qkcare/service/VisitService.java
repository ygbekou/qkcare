package com.qkcare.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.qkcare.domain.GenericVO;
import com.qkcare.model.Admission;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.Visit;


@Service(value="visitService")
public interface VisitService {
	
	public BaseEntity save(Visit visit);
	
	public BaseEntity findVisit(Class cl, Long key);
	
	public List<BaseEntity> getVisitChilds(String child, Long visitId);
	
	public BaseEntity findPrescription(Class cl, Long key);
	
	public Set<GenericVO> getCategoryAllergies();
	
	public List<GenericVO> getMedicalHistories();
	
	public List<GenericVO> getSocialHistories();
	
	public Map<Integer, List<Visit>> getVisitsByMonth();
	
}
