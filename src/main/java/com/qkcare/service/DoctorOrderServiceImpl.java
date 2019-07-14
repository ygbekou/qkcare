package com.qkcare.service;



import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.javatuples.Pair;
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
	
	private static final Logger LOGGER = Logger.getLogger(DoctorOrderServiceImpl.class);
	
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
				if (isUpdate) {
					Pair<List<Long>, List<Long>> labTestIdPairs = this.getRemovedAndNewLabTestIds(doctorOrder);
					
					LOGGER.info(String.format("These LabTests were removed %s ", labTestIdPairs.getValue0()));
					LOGGER.info(String.format("These LabTests were added %s ", labTestIdPairs.getValue1()));
					
					int deletedInvestigationNumber = this.deleteRemovedLabTests(doctorOrder);
					int deletedLabTestNumber = this.deleteRemovedInvestigationLabTests(doctorOrder);
					
					doctorOrder.getLabTests().removeIf((LabTest lb) -> !labTestIdPairs.getValue1().contains(lb.getId()));
					
				}
				
				for (LabTest labTest : doctorOrder.getLabTests()) {
					this.investigationService.save(new Investigation(doctorOrder, labTest));
				}
			}
			
			if (doctorOrder.getDoctorOrderTypeEnum() == DoctorOrderTypeEnum.PHARMACY) {
				if (isUpdate) {
					Pair<List<Long>, List<Long>> productIdPairs = this.getRemovedAndNewProductIds(doctorOrder);
					
					LOGGER.info(String.format("These Products were removed %s ", productIdPairs.getValue0()));
					LOGGER.info(String.format("These Products were added %s ", productIdPairs.getValue1()));
					
					int deletedItemNumber = this.deleteRemovedProdcuts(doctorOrder);
					
					doctorOrder.getProducts().removeIf((Product p) -> !productIdPairs.getValue1().contains(p.getId()));
					
				}
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
		
		// Get the doctor order labTests 
		paramTupleList.clear();
		paramTupleList.add(Quartet.with("I.DOCTOR_ORDER_ID = ", ":doctorOrderId", id + "", "Long"));
		queryStr =  "SELECT L.LAB_TEST_ID, L.NAME \r\n" + 
				"FROM LAB_TEST L\r\n" + 
				"JOIN INVESTIGATION I ON L.LAB_TEST_ID = I.LAB_TEST_ID WHERE 1 = 1 ";
		list = genericService.getNativeByCriteria(queryStr, paramTupleList, " ", " ");
		List<LabTest> doctorOrderLabTests = new ArrayList<LabTest>();
		
		for (Object[] objects : list) {
			doctorOrderLabTests.add(new LabTest(new Long(objects[0].toString()), objects[1].toString()));
		}
		
		doctorOrder.setLabTests(doctorOrderLabTests);
		
		
		User modifiedBy = (User) this.genericService.find(User.class, doctorOrder.getModifiedBy());
		if (modifiedBy != null) {
			doctorOrder.setModifiedByName(modifiedBy.getLastName() + " " + modifiedBy.getFirstName());
		}
		
		
		return doctorOrder;
	}

	private int deleteRemovedProdcuts(DoctorOrder doctorOrder) {
		List<Product> selectedProducts = doctorOrder.getProducts();
		List<Long> selectedProductIds = selectedProducts.stream()
                .map(Product::getId).collect(Collectors.toList());
		
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("PS.DOCTOR_ORDER_ID = ", "doctorOrderId", doctorOrder.getId() + "", "Long"));
		paramTupleList.add(Quartet.with("PSP.PRODUCT_ID NOT IN ", "selectedProductIds", selectedProductIds.toString().substring(1, selectedProductIds.toString().length() - 1) + "", "List"));
		String queryStr =  "DELETE FROM PATIENT_SALE_PRODUCT WHERE PATIENT_SALE_PRODUCT_ID IN (\r\n" + 
					"			SELECT PSP_ID FROM (\r\n" + 
					"				SELECT PSP.PATIENT_SALE_PRODUCT_ID AS PSP_ID FROM PATIENT_SALE_PRODUCT PSP, PATIENT_SALE PS\r\n" + 
					"				WHERE PSP.PATIENT_SALE_ID = PS.PATIENT_SALE_ID \r\n" + 
					"				AND PS.DOCTOR_ORDER_ID = :doctorOrderId \r\n" + 
					"				AND PSP.PRODUCT_ID NOT IN (:selectedProductIds)\r\n" + 
					"			) AS PSP2\r\n" + 
					"		) ";
		Integer totalDeleted = genericService.deleteNativeByCriteria(queryStr, paramTupleList);
		
		LOGGER.info(String.format("Number of products deleted %d ", totalDeleted));
		
		return totalDeleted;
	}
	
	
	private int deleteRemovedInvestigationLabTests(DoctorOrder doctorOrder) {
		List<LabTest> selectedLabTests = doctorOrder.getLabTests();
		List<Long> selectedLabTestIds = selectedLabTests.stream()
                .map(LabTest::getId).collect(Collectors.toList());
		
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("I.DOCTOR_ORDER_ID = ", "doctorOrderId", doctorOrder.getId() + "", "Long"));
		paramTupleList.add(Quartet.with("IT.LAB_TEST_ID NOT IN ", "selectedLabTestIds", selectedLabTestIds.toString().substring(1, selectedLabTestIds.toString().length() - 1) + "", "List"));
		String queryStr =  "DELETE FROM INVESTIGATION_TEST WHERE INVESTIGATION_TEST_ID IN (\r\n" + 
					"			SELECT IT_ID FROM (\r\n" + 
					"				SELECT IT.INVESTIGATION_TEST_ID AS IT_ID FROM INVESTIGATION_TEST IT, INVESTIGATION I\r\n" + 
					"				WHERE IT.INVESTIGATION_ID = I.INVESTIGATION_ID \r\n" + 
					"				AND I.DOCTOR_ORDER_ID = :doctorOrderId \r\n" + 
					"				AND I.LAB_TEST_ID NOT IN (:selectedLabTestIds)\r\n" + 
					"			) AS IT2\r\n" + 
					"		) ";
		Integer totalDeleted = genericService.deleteNativeByCriteria(queryStr, paramTupleList);
		
		LOGGER.info(String.format("Number of investigation tests deleted %d ", totalDeleted));
		
		return totalDeleted;
	}
	
	private int deleteRemovedLabTests(DoctorOrder doctorOrder) {
		List<LabTest> selectedLabTests = doctorOrder.getLabTests();
		List<Long> selectedLabTestIds = selectedLabTests.stream()
                .map(LabTest::getId).collect(Collectors.toList());
		
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("I.DOCTOR_ORDER_ID = ", "doctorOrderId", doctorOrder.getId() + "", "Long"));
		paramTupleList.add(Quartet.with("IT.LAB_TEST_ID NOT IN ", "selectedLabTestIds", selectedLabTestIds.toString().substring(1, selectedLabTestIds.toString().length() - 1) + "", "List"));
		String queryStr =  "DELETE FROM INVESTIGATION WHERE INVESTIGATION_ID IN (\r\n" + 
					"			SELECT I_ID FROM (\r\n" + 
					"				SELECT I.INVESTIGATION_ID AS I_ID FROM INVESTIGATION I\r\n" + 
					"				WHERE I.DOCTOR_ORDER_ID = :doctorOrderId \r\n" + 
					"				AND I.LAB_TEST_ID NOT IN (:selectedLabTestIds)\r\n" + 
					"			) AS I2\r\n" + 
					"		) ";
		Integer totalDeleted = genericService.deleteNativeByCriteria(queryStr, paramTupleList);
		
		LOGGER.info(String.format("Number of investigation deleted %d ", totalDeleted));
		
		
		
		return totalDeleted;
	}
	
	
	private Pair<List<Long>, List<Long>> getRemovedAndNewProductIds(DoctorOrder doctorOrder) {
		List<Product> selectedProducts = doctorOrder.getProducts();
		List<Long> selectedProductIds = selectedProducts.stream().map(Product::getId).collect(Collectors.toList());
		
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("PS.DOCTOR_ORDER_ID = ", ":doctorOrderId", doctorOrder.getId() + "", "Long"));
		String queryStr =  "SELECT PSP.PRODUCT_ID, PSP.PATIENT_SALE_PRODUCT_ID \r\n" + 
				"FROM PATIENT_SALE_PRODUCT PSP, PATIENT_SALE PS\r\n" + 
				"WHERE PSP.PATIENT_SALE_ID = PS.PATIENT_SALE_ID \r\n";
		
		List<Object[]> list = genericService.getNativeByCriteria(queryStr, paramTupleList, " ", " ");
		List<Long> existingProductIds = new ArrayList<Long>();
		
		for (Object[] objects : list) {
			existingProductIds.add(new Long(objects[0].toString()));
		}
		
		
		return Pair.with(new ArrayList<Long>(CollectionUtils.subtract(existingProductIds, selectedProductIds)), 
				new ArrayList<Long>(CollectionUtils.subtract(selectedProductIds, existingProductIds)));
	}
	
	private Pair<List<Long>, List<Long>> getRemovedAndNewLabTestIds(DoctorOrder doctorOrder) {
		List<LabTest> selectedLabTests = doctorOrder.getLabTests();
		List<Long> selectedLabTestIds = selectedLabTests.stream().map(LabTest::getId).collect(Collectors.toList());
		
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("I.DOCTOR_ORDER_ID = ", ":doctorOrderId", doctorOrder.getId() + "", "Long"));
		String queryStr =  "SELECT I.LAB_TEST_ID, I.INVESTIGATION_ID \r\n" + 
				"FROM INVESTIGATION I\r\n" + 
				"WHERE 1 = 1 \r\n";
		
		List<Object[]> list = genericService.getNativeByCriteria(queryStr, paramTupleList, " ", " ");
		List<Long> existingLabTestIds = new ArrayList<Long>();
		
		for (Object[] objects : list) {
			existingLabTestIds.add(new Long(objects[0].toString()));
		}
		
		
		return Pair.with(new ArrayList<Long>(CollectionUtils.subtract(existingLabTestIds, selectedLabTestIds)), 
				new ArrayList<Long>(CollectionUtils.subtract(selectedLabTestIds, existingLabTestIds)));
	}


}
