package com.qkcare.service;



import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.javatuples.Quartet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qkcare.model.BaseEntity;
import com.qkcare.model.DoctorOrder;
import com.qkcare.model.Investigation;
import com.qkcare.model.LabTest;
import com.qkcare.model.Product;
import com.qkcare.model.User;
import com.qkcare.model.enums.DoctorOrderTypeEnum;
import com.qkcare.model.stocks.PatientSale;

@Service(value="doctorOrderService")
public class DoctorOrderServiceImpl  implements DoctorOrderService {
	
	@Autowired
	GenericService genericService;
	
	@Autowired
	InvestigationService investigationService;
	
	@Autowired
	PurchasingService purchasingService;
	
	@Transactional
	public BaseEntity save(DoctorOrder doctorOrder) {
		return this.save(doctorOrder, false);
	}
	
	@Transactional
	public BaseEntity save(DoctorOrder doctorOrder, boolean notChildInclude) {
		boolean isUpdate = doctorOrder.getId() != null;
		DoctorOrder docOrder = (DoctorOrder)this.genericService.save(doctorOrder);
		
		if (!notChildInclude) {
			if (doctorOrder.getDoctorOrderTypeEnum() == DoctorOrderTypeEnum.LABORATORY) {
				for (LabTest labTest : doctorOrder.getLabTests()) {
					this.investigationService.save(new Investigation(doctorOrder, labTest));
				}
			}
			
			if (doctorOrder.getDoctorOrderTypeEnum() == DoctorOrderTypeEnum.PHARMACY) {
				this.purchasingService.save(new PatientSale(doctorOrder));
			}
		}
		
		return docOrder;
		
	}
	
	
	public BaseEntity getById(Long id) {
		DoctorOrder doctorOrder = (DoctorOrder) this.genericService.find(DoctorOrder.class, id);
		
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("PS.DOCTOR_ORDER_ID = ", ":doctorOrderId", id + "", "Long"));
		String queryStr =  "SELECT P.PRODUCT_ID, P.NAME \r\n" + 
				"FROM PRODUCT P\r\n" + 
				"JOIN PATIENT_SALE_PRODUCT PSP ON P.PRODUCT_ID = PSP.PRODUCT_ID\r\n" + 
				"JOIN PATIENT_SALE PS ON PSP.PATIENT_SALE_ID = PS.PATIENT_SALE_ID WHERE 1 = 1 ";
		List<Object[]> list = genericService.getNativeByCriteria(queryStr, paramTupleList, " ", " ");
		List<Product> doctorOrderProducts = new ArrayList<Product>();
		
		for (Object[] objects : list) {
			doctorOrderProducts.add(new Product(new Long(objects[0].toString()), objects[1].toString()));
		}
		
		doctorOrder.setProducts(doctorOrderProducts);
		
		User modifiedBy = (User) this.genericService.find(User.class, doctorOrder.getModifiedBy());
		if (modifiedBy != null) {

			doctorOrder.setModifiedByName(modifiedBy.getLastName() + " " + modifiedBy.getFirstName());
		}
		
		
		return doctorOrder;
	}

}
