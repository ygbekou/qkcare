package com.qkcare.service;

import org.javatuples.Pair;
import org.springframework.stereotype.Service;

import com.qkcare.model.BaseEntity;
import com.qkcare.model.Bill;
import com.qkcare.model.BillPayment;
import com.qkcare.model.PatientPackage;
import com.qkcare.model.PatientService;
import com.qkcare.model.stocks.PatientSaleProduct;


@Service(value="billingService")
public interface BillingService {
	
	public BaseEntity save(com.qkcare.model.Package pckage);
	
	public BaseEntity save(Bill bill);
	
	public BaseEntity save(BillPayment billPayment);
	
	public BaseEntity findBill(Class cl, Long key, String itemLabel, Long itemId);
	
	public BaseEntity findBillInitial(String itemLabel, Long itemNumber);
	
	public BaseEntity findPackage(Class cl, Long key);
	
	public Pair<String, Long> deleteBillService(Long id);
	
	public PatientService save(PatientService patientService);
	
	public void delete(PatientService patientService);
	
	public PatientPackage save(PatientPackage patientPackage);
	
	public void delete(PatientPackage patientPackage);
	
	public PatientSaleProduct save(PatientSaleProduct patientSaleProduct);
}
