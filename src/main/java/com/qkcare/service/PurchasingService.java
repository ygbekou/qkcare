package com.qkcare.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.qkcare.model.BaseEntity;
import com.qkcare.model.stocks.PatientSale;
import com.qkcare.model.stocks.PatientSaleProduct;
import com.qkcare.model.stocks.PurchaseOrder;
import com.qkcare.model.stocks.ReceiveOrder;
import com.qkcare.model.stocks.SaleReturn;


@Service(value="purchasingService")
public interface PurchasingService {
	
	public BaseEntity save(PurchaseOrder purchaseOrder);
	
	public BaseEntity findPurchaseOrder(Class cl, Long key);
	
	public BaseEntity save(ReceiveOrder receiveOrder);
	
	public List<ReceiveOrder> findInitialReceiveOrder(Class cl, Long key) throws NumberFormatException, ParseException;
	
	public BaseEntity findReceiveOrder(Class cl, Long key);
	
	public BaseEntity save(PatientSale patientSale);
	
	public BaseEntity findPatientSale(Class cl, Long key);
	
	public BaseEntity save(SaleReturn saleReturn);
	
	public BaseEntity findSaleReturn(Class cl, Long key);
	
	public BaseEntity findInitialSaleReturn(Class cl, Long patientSaleId) throws NumberFormatException, ParseException;
	
	public BaseEntity save(PatientSaleProduct patientSaleProduct);
}
