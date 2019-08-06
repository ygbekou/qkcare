package com.qkcare.service;

import org.springframework.stereotype.Service;

import com.qkcare.model.BaseEntity;
import com.qkcare.model.Bill;
import com.qkcare.model.BillPayment;


@Service(value="billingService")
public interface BillingService {
	
	public BaseEntity save(com.qkcare.model.Package pckage);
	
	public BaseEntity save(Bill bill);
	
	public BaseEntity save(BillPayment billPayment);
	
	public BaseEntity findBill(Class cl, Long key, String itemLabel, Long itemId);
	
	public BaseEntity findBillInitial(String itemLabel, Long itemNumber);
	
	public BaseEntity findPackage(Class cl, Long key);
}
