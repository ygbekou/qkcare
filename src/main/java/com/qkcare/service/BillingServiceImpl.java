package com.qkcare.service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qkcare.model.Admission;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.BedAssignment;
import com.qkcare.model.Bill;
import com.qkcare.model.BillPayment;
import com.qkcare.model.BillService;
import com.qkcare.model.Investigation;
import com.qkcare.model.PackageService;
import com.qkcare.model.PatientPackage;
import com.qkcare.model.PatientService;
import com.qkcare.model.Visit;
import com.qkcare.model.stocks.PatientSaleProduct;
import com.qkcare.util.DateUtil;

@Service(value="billingService")
public class BillingServiceImpl  implements BillingService {
	
	@Autowired
	GenericService genericService;
	
	@Autowired
	AdmissionService admissionService;
	
	@Autowired
	VisitService visitService;
	
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
		
		// This initial step is necessary to update the paid amount on the bill.
		bill.setPaid(0d);
		if (bill.getBillPayments() != null) {
			for (BillPayment bp : bill.getBillPayments()) {
				if (bp.getAmount() != null && bp.getAmount() > 0) {
					bill.addPayment(bp.getAmount());
				}
			}
		}
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
		
		List<BillService> billServices = new ArrayList<BillService>();
		
		Bill initialBill = null;
		if (bill.getAdmission() != null) {
			initialBill = (Bill) this.findBillInitial("admission", bill.getAdmission().getId());
		} else if (bill.getVisit() != null) {
			initialBill = (Bill) this.findBillInitial("visit", bill.getVisit().getId());
		}
		
		
		
		//this.setVisitOrAdmission(bill, itemLabel, itemId);
		paramTupleList.clear();
		paramTupleList.add(Quartet.with("e.bill.id = ", "billId", bill.getId() + "", "Long"));
		String queryStr =  "SELECT e FROM BillService e WHERE 1 = 1";
		List<BaseEntity> services = genericService.getByCriteria(queryStr, paramTupleList, null);
		
		for (BaseEntity entity : services) {
			BillService billService = (BillService)entity;
			if (initialBill.getBillServices().contains(billService)) {
				initialBill.getBillServices().remove(billService);
			}
			billService.setBill(null);
			billServices.add(billService);
		}
		
		bill.setBillServices(billServices);
		
		for (BillService bs : initialBill.getBillServices()) {
			bill.addBillService(bs);
		}
		
		
		paramTupleList.clear();
		paramTupleList.add(Quartet.with("e.bill.id = ", "billId", bill.getId() + "", "Long"));
		queryStr =  "SELECT e FROM BillPayment e WHERE 1 = 1";
		List<BaseEntity> payments = genericService.getByCriteria(queryStr, paramTupleList, null);
		List<BillPayment> billPayments = new ArrayList<BillPayment>();
		
		bill.setPaid(0d);
		for (BaseEntity entity : payments) {
			BillPayment billPayment = (BillPayment)entity;
			billPayment.setBill(null);
			billPayments.add(billPayment);
			bill.addPayment(billPayment.getAmount());
		}
		bill.setBillPayments(billPayments);
		
		return bill;
		
	}
	
	
	private BaseEntity setVisitOrAdmission(Bill bill, String itemLabel, Long itemId) {
		BaseEntity item = null;
		if ("visit".equalsIgnoreCase(itemLabel.toLowerCase())) {
			item = this.visitService.findVisit(Visit.class, itemId);
			bill.setVisit((Visit)item);
		} else {
			item = this.admissionService.findAdmission(Admission.class, itemId);
			bill.setAdmission((Admission)item);
		}
		
		return item;
	}
	
	public BaseEntity findBillInitial(String itemLabel, Long itemId) {
		
		Bill bill = new Bill();
		bill.setBillDate(new Date());
		bill.setDueDate(DateUtil.addDays(new Date(), 30));
		
		BaseEntity item = this.setVisitOrAdmission(bill, itemLabel, itemId);
		
		// Return if the no admission or visit found
		if (item == null) return bill;		
	
		// Retrieve patient services
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("e." + itemLabel + ".id = ", "itemId", itemId + "", "Long"));
		String queryStr =  "SELECT e FROM PatientService e WHERE 1 = 1";
		List<BaseEntity> patientServices = genericService.getByCriteria(queryStr, paramTupleList, null);
		
		for (BaseEntity entity : patientServices) {
			PatientService ps =(PatientService)entity;
			bill.addBillService(new BillService(ps));
		}
		
		// Retrieve patient packages
		paramTupleList.clear();
		paramTupleList.add(Quartet.with("e." + itemLabel + ".id = ", "itemId", itemId + "", "Long"));
		queryStr =  "SELECT e FROM PatientPackage e WHERE 1 = 1";
		List<BaseEntity> patientPackages = genericService.getByCriteria(queryStr, paramTupleList, null);
		
		for (BaseEntity entity : patientPackages) {
			PatientPackage pp =(PatientPackage)entity;
			bill.addBillService(new BillService(pp));
		}
		
		
		paramTupleList.clear();
		paramTupleList.add(Quartet.with("e.patientSale." + itemLabel + ".id = ", "itemId", itemId + "", "Long"));
		paramTupleList.add(Quartet.with("e.patientSale.status = ", "status", "0", "Integer"));
		queryStr =  "SELECT e FROM PatientSaleProduct e WHERE 1 = 1";
		List<BaseEntity> patientSaleProducts = genericService.getByCriteria(queryStr, paramTupleList, null);
				
		for (BaseEntity entity : patientSaleProducts) {
			PatientSaleProduct psp = (PatientSaleProduct)entity;
			bill.addBillService(new BillService(psp, bill.getDoctor()));
		}
	
		
		paramTupleList.clear();
		paramTupleList.add(Quartet.with("e." + itemLabel + ".id = ", "itemId", itemId + "", "Long"));
		paramTupleList.add(Quartet.with("e.status = ", "status", "4", "Integer"));
		queryStr =  "SELECT e FROM Investigation e WHERE 1 = 1";
		List<BaseEntity> investigations = genericService.getByCriteria(queryStr, paramTupleList, null);
		
		for (BaseEntity entity : investigations) {
			Investigation invest = (Investigation)entity;
			bill.addBillService(new BillService(invest, bill.getDoctor()));
		}
	
		
		if ("admission".equals(itemLabel)) {
			paramTupleList.clear();
			paramTupleList.add(Quartet.with("e." + itemLabel + ".id = ", "itemId", itemId + "", "Long"));
			queryStr =  "SELECT e FROM BedAssignment e WHERE 1 = 1";
			List<BaseEntity> bedAssignments = genericService.getByCriteria(queryStr, paramTupleList, null);
			
			for (BaseEntity entity : bedAssignments) {
				BedAssignment ba = (BedAssignment)entity;
				bill.addBillService(new BillService(ba, bill.getDoctor()));
			}
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
	
	@Transactional
	public Pair<String, Long> deleteBillService(Long id) {
		BillService billService = (BillService) this.genericService.find(BillService.class, id);
		Bill bill = billService.getBill();
		bill.removeBillService(billService);
		this.genericService.save(bill);
		this.genericService.delete(BillService.class, Arrays.asList(new Long[]{id}));
		String itemLabel = bill.getAdmission() != null ? "admission" : "visit";
		Long itemId =  bill.getAdmission() != null ? bill.getAdmission().getId() : bill.getVisit().getId();
		
		return Pair.with(itemLabel, itemId);
	}
}
