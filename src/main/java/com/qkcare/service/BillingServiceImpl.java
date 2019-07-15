package com.qkcare.service;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.javatuples.Quartet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qkcare.model.Admission;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.Bill;
import com.qkcare.model.BillPayment;
import com.qkcare.model.BillService;
import com.qkcare.model.DoctorOrder;
import com.qkcare.model.Employee;
import com.qkcare.model.InvestigationTest;
import com.qkcare.model.PackageService;
import com.qkcare.model.Payment;
import com.qkcare.model.Visit;
import com.qkcare.model.stocks.PatientSaleProduct;

@Service(value="billingService")
public class BillingServiceImpl  implements BillingService {
	
	@Autowired
	GenericService genericService;
	
	@Transactional
	public BaseEntity save(com.qkcare.model.Package pckage) {
		
		BaseEntity toReturn = this.genericService.save(pckage);
		
		for (PackageService ps : pckage.getPackageServices()) {
			ps.setPckage((com.qkcare.model.Package)toReturn);
			this.genericService.save(ps);
		}
		
		return toReturn;
	}
	
	@Transactional
	public BaseEntity save(Bill bill) {
		
		BaseEntity toReturn = this.genericService.save(bill);
		
		if (bill.getBillServices() != null) {
			for (BillService bs : bill.getBillServices()) {
				bs.setBill((Bill)toReturn);
				this.genericService.save(bs);
			}
		}
		
		if (bill.getBillPayments() != null) {
			for (BillPayment bp : bill.getBillPayments()) {
				if (bp.getAmount() != null && bp.getAmount() > 0) {
					bp.setBill((Bill)toReturn);
					this.genericService.save(bp);
				}
			}
		}
		
		return toReturn;
	}
	
	
	public BaseEntity findBill(Class cl, Long key, String itemLabel, Long itemId) {
		Bill bill = null;
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		if (key != null) {
			bill = (Bill) this.genericService.find(cl, key);
		} else {
			paramTupleList.add(Quartet.with("e." + itemLabel.toLowerCase() + ".id = ", "itemId", itemId + "", "Long"));
			String queryStr =  "SELECT e FROM Bill e WHERE 1 = 1"; 
			List<BaseEntity> bills = genericService.getByCriteria(queryStr, paramTupleList, null);
			if (bills.size() > 0) {
				bill = (Bill) bills.get(0);
			}
		}
		
		if (bill == null) {
			return bill;
		}
		
		paramTupleList.clear();
		paramTupleList.add(Quartet.with("e.bill.id = ", "billId", bill.getId() + "", "Long"));
		String queryStr =  "SELECT e FROM BillService e WHERE 1 = 1";
		List<BaseEntity> services = genericService.getByCriteria(queryStr, paramTupleList, null);
		List<BillService> billServices = new ArrayList<BillService>();
		
		for (BaseEntity entity : services) {
			BillService billService = (BillService)entity;
			billService.setBill(null);
			billServices.add(billService);
		}
		bill.setBillServices(billServices);
		
		
		paramTupleList.clear();
		paramTupleList.add(Quartet.with("e.bill.id = ", "billId", bill.getId() + "", "Long"));
		queryStr =  "SELECT e FROM BillPayment e WHERE 1 = 1";
		List<BaseEntity> payments = genericService.getByCriteria(queryStr, paramTupleList, null);
		List<BillPayment> billPayments = new ArrayList<BillPayment>();
		
		for (BaseEntity entity : payments) {
			BillPayment billPayment = (BillPayment)entity;
			billPayment.setBill(null);
			billPayments.add(billPayment);
			bill.addPayment(billPayment.getAmount());
		}
		bill.setBillPayments(billPayments);
		
		return bill;
		
	}
	
	public BaseEntity findBillInitial(String itemLabel, String itemNumber) {
		Bill bill = new Bill();
		Date serviceDate = null;
		Employee doctor = null;
		
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("e.id = ", "itemId", itemNumber + "", "Long"));
		String queryStr =  "SELECT e FROM " + itemLabel + " e WHERE 1 = 1";
		List<BaseEntity> items = genericService.getByCriteria(queryStr, paramTupleList, null);
		
		if (items.size() > 0) {
			if ("visit".equalsIgnoreCase(itemLabel.toUpperCase()))
				bill.setVisit((Visit)items.get(0));
			else 
				bill.setAdmission((Admission)items.get(0));
		} else {
			return bill;
		}
		Long packageId = null;
		if ("visit".equalsIgnoreCase(itemLabel) &&  bill.getVisit().getPckage() != null) {
			packageId = bill.getVisit().getPckage().getId();
			serviceDate = bill.getVisit().getVisitDatetime();
			doctor = bill.getVisit().getDoctor();
		}
		else if ("admission".equalsIgnoreCase(itemLabel) &&  bill.getAdmission().getPckage() != null) {
			packageId = bill.getAdmission().getPckage().getId();
			serviceDate = bill.getAdmission().getAdmissionDatetime();
			doctor = bill.getAdmission().getDoctorAssignment().getDoctor();
		}
		
		paramTupleList.clear();
		
		if (packageId != null) {
			paramTupleList.add(Quartet.with("e.pckage.id = ", "packageId", packageId + "", "Long"));
			queryStr =  "SELECT e FROM PackageService e WHERE 1 = 1";
			List<BaseEntity> packageServices = genericService.getByCriteria(queryStr, paramTupleList, null);
			
			for (BaseEntity entity : packageServices) {
				BillService billService = new BillService();
				billService.setServiceDate(serviceDate);
				billService.setService(((PackageService)entity).getService());
				billService.setQuantity(((PackageService)entity).getQuantity());
				billService.setUnitAmount(((PackageService)entity).getRate());
				billService.setTotalAmount(billService.getQuantity() * billService.getUnitAmount());
				billService.setDiscountAmount(0d);
				billService.setDiscountPercentage(0d);
				billService.setNetAmount(billService.getQuantity() * billService.getUnitAmount());
				billService.setDoctor(bill.getVisit().getDoctor());
				bill.addBillService(billService);
			}
		}
		
		
		paramTupleList.clear();
		paramTupleList.add(Quartet.with("e.patientSale." + itemLabel.toLowerCase() + ".id = ", "itemId", itemNumber + "", "Long"));
		paramTupleList.add(Quartet.with("e.patientSale.status = ", "status", "0", "Integer"));
		queryStr =  "SELECT e FROM PatientSaleProduct e WHERE 1 = 1";
		List<BaseEntity> patientSaleProducts = genericService.getByCriteria(queryStr, paramTupleList, null);
		
		com.qkcare.model.Service defaultMedicineService = (com.qkcare.model.Service) this.genericService.find(com.qkcare.model.Service.class, 1L);
		
		for (BaseEntity entity : patientSaleProducts) {
			BillService billService = new BillService();
			PatientSaleProduct patientSaleProduct = (PatientSaleProduct)entity;
			billService.setServiceDate(patientSaleProduct.getPatientSale().getSaleDatetime());
			billService.setService(defaultMedicineService);
			billService.setQuantity(patientSaleProduct.getQuantity());
			billService.setUnitAmount(patientSaleProduct.getUnitPrice());
			billService.setDescription(patientSaleProduct.getProduct().getName());
			billService.setTotalAmount(billService.getQuantity() * billService.getUnitAmount());
			billService.setDiscountAmount(0d);
			billService.setDiscountPercentage(0d);
			billService.setNetAmount(billService.getQuantity() * billService.getUnitAmount());
			
			doctor = patientSaleProduct.getPatientSale().getDoctorOrder() != null 
					? patientSaleProduct.getPatientSale().getDoctorOrder().getDoctor() : doctor;
					
			billService.setDoctor(bill.getAdmission().getDoctorAssignment().getDoctor());
			bill.addBillService(billService);
		}
	
		
		paramTupleList.clear();
		paramTupleList.add(Quartet.with("e.investigation." + itemLabel.toLowerCase() + ".id = ", "itemId", itemNumber + "", "Long"));
		paramTupleList.add(Quartet.with("e.investigation.status = ", "status", "0", "Integer"));
		queryStr =  "SELECT e FROM InvestigationTest e WHERE 1 = 1";
		List<BaseEntity> investigationTests = genericService.getByCriteria(queryStr, paramTupleList, null);
		
		com.qkcare.model.Service defaultInvestigationService = (com.qkcare.model.Service) this.genericService.find(com.qkcare.model.Service.class, 2L);
		
		for (BaseEntity entity : investigationTests) {
			InvestigationTest investigationTest = (InvestigationTest)entity;
			BillService billService = new BillService();
			billService.setServiceDate(investigationTest.getInvestigation().getInvestigationDatetime());
			billService.setService(defaultInvestigationService);
			//billService.setQuantity(((InvestigationTest)entity).getQuantity());
			//billService.setUnitAmount(((InvestigationTest)entity).getUnitPrice());
			billService.setQuantity(1);
			billService.setUnitAmount(100d);
			billService.setDescription(investigationTest.getLabTest().getName());
			billService.setTotalAmount(billService.getQuantity() * billService.getUnitAmount());
			billService.setDiscountAmount(0d);
			billService.setDiscountPercentage(0d);
			billService.setNetAmount(billService.getQuantity() * billService.getUnitAmount());
			
			doctor = investigationTest.getInvestigation().getDoctorOrder() != null 
					? investigationTest.getInvestigation().getDoctorOrder().getDoctor() : doctor;
						
			billService.setDoctor(doctor);
			bill.addBillService(billService);
		}
	
		
		
		return bill;
		
	}
	
	public BaseEntity findPackage(Class cl, Long key) {
		com.qkcare.model.Package packge = (com.qkcare.model.Package) this.genericService.find(cl, key);
		
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("e.pckage.id = ", "packageId", key + "", "Long"));
		String queryStr =  "SELECT e FROM PackageService e WHERE 1 = 1";
		List<BaseEntity> services = genericService.getByCriteria(queryStr, paramTupleList, null);
		List<PackageService> packageServices = new ArrayList<PackageService>();
		
		for (BaseEntity entity : services) {
			PackageService packageService = (PackageService)entity;
			packageService.setPckage(null);
			packageServices.add(packageService);
		}
		packge.setPackageServices(packageServices);
		
		
		return packge;
		
	}
	
	@Transactional
	public BaseEntity save(BillPayment billPayment) {
		BillPayment existingBillPayment = null;
		Bill bill = null;
		Double existingAmount = null;
		
		if (billPayment.getId() != null) {
			existingBillPayment = (BillPayment) this.genericService.find(BillPayment.class, billPayment.getId());
			existingAmount = existingBillPayment.getAmount();
		}
		BaseEntity toReturn = this.genericService.save(billPayment);
		
		if (existingBillPayment != null && billPayment.getAmount() != existingAmount) {
			bill = (Bill) this.genericService.find(Bill.class, billPayment.getBill().getId());
			bill.removePayment(existingBillPayment.getAmount());
			bill.addPayment(billPayment.getAmount());
			this.genericService.save(bill);
		}
		
		return toReturn;
	}
}
