package com.qkcare.service;

import org.springframework.stereotype.Service;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.DoctorOrder;


@Service(value="doctorOrderService")
public interface DoctorOrderService {
	
	public BaseEntity save(DoctorOrder doctorOrder);
	
	public BaseEntity save(DoctorOrder doctorOrder, boolean notChildInclude);
	
	public BaseEntity getById(Long id);
	
}
