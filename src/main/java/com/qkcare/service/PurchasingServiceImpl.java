package com.qkcare.service;


import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.javatuples.Quartet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qkcare.model.BaseEntity;
import com.qkcare.model.Product;
import com.qkcare.model.stocks.PatientSale;
import com.qkcare.model.stocks.PatientSaleProduct;
import com.qkcare.model.stocks.PatientSaleStatus;
import com.qkcare.model.stocks.PurchaseOrder;
import com.qkcare.model.stocks.PurchaseOrderProduct;
import com.qkcare.model.stocks.PurchaseOrderStatus;
import com.qkcare.model.stocks.SaleReturn;
import com.qkcare.model.stocks.SaleReturnProduct;

@Service(value="purchasingService")
public class PurchasingServiceImpl  implements PurchasingService {
	
	@Autowired
	GenericService genericService;
	
	@Autowired
	BillingService billingService;
	
	@Transactional
	public BaseEntity save(PurchaseOrder purchaseOrder) {
		
		if (purchaseOrder.getDiscount() == null) {
			purchaseOrder.setDiscount(0d);
		}
		
		BaseEntity toReturn = this.genericService.save(purchaseOrder);
		
		List<PurchaseOrderProduct> removedPops = new ArrayList<>();
		List<PurchaseOrderProduct> newPops = new ArrayList<>();
		Integer leftQty = 0;
		
		for (PurchaseOrderProduct pop : purchaseOrder.getPurchaseOrderProducts()) {
			if (pop.getStatus() == 9) {
				this.genericService.delete(PurchaseOrderProduct.class, Arrays.asList(new Long[]{pop.getId()}));
				removedPops.add(pop);
				
			} else {
				leftQty += pop.getQuantity() - (pop.getReceivedQuantity() != null ? pop.getReceivedQuantity() : 0);
				if (pop.getReceivedQuantity() != null && pop.getReceivedQuantity() > 0 && pop.getStatus() == 0) {
					
					if (pop.getQuantity() > pop.getReceivedQuantity()) {
						PurchaseOrderProduct popCopy = pop.clone(pop);
						newPops.add(popCopy);
						popCopy.setPurchaseOrder((PurchaseOrder)toReturn);
						this.genericService.save(popCopy);
						pop.setQuantity(pop.getReceivedQuantity());
					}
					
					pop.setReceivedDatetime(new Timestamp(new Date().getTime()));
					pop.setStatus(1);
				}
				pop.setPurchaseOrder((PurchaseOrder)toReturn);
				this.genericService.save(pop);
				
				if (pop.getReceivedQuantity() != null) {
					// Increase product available qty
					Product product = (Product) this.genericService.find(Product.class, pop.getProduct().getId());
					product.setQuantityInStock(product.getQuantityInStock() + pop.getReceivedQuantity());
					this.genericService.save(product);
				}
			}
		}
		
		purchaseOrder.getPurchaseOrderProducts().addAll(newPops);
		purchaseOrder.getPurchaseOrderProducts().removeAll(removedPops);
		purchaseOrder.decreaseAmount(removedPops);
		
		Long purchaseOrderStatusId = purchaseOrder.getPurchaseOrderStatus().getId();
		if (purchaseOrderStatusId == 3L || purchaseOrderStatusId == 4L) {
			purchaseOrderStatusId = leftQty > 0 ? 4L : 5L;
		}
		purchaseOrder.setPurchaseOrderStatus((PurchaseOrderStatus)this.genericService.find(PurchaseOrderStatus.class, purchaseOrderStatusId));
		
		
		toReturn = this.genericService.save(purchaseOrder);
		
		return toReturn;
	}
	
	@Transactional
	public BaseEntity save(PatientSaleProduct patientSaleProduct) {
		
		BaseEntity toReturn = this.genericService.save(patientSaleProduct);
		
		if (patientSaleProduct.getStatus() == 4) {
			patientSaleProduct.setVisit(patientSaleProduct.getPatientSale().getVisit());
			patientSaleProduct.setAdmission(patientSaleProduct.getPatientSale().getAdmission());
			this.billingService.save(patientSaleProduct);
		}
		
		return toReturn;
	}
	
	
	
	public BaseEntity findPurchaseOrder(Class cl, Long key) {
		PurchaseOrder purchaseOrder = (PurchaseOrder) this.genericService.find(cl, key);
		
		if (purchaseOrder != null) {
			List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
			paramTupleList.add(Quartet.with("e.purchaseOrder.id = ", "purchaseOrderId", key + "", "Long"));
			String queryStr =  "SELECT e FROM PurchaseOrderProduct e WHERE 1 = 1";
			List<BaseEntity> purchaseOrders = genericService.getByCriteria(queryStr, paramTupleList, null);
			List<PurchaseOrderProduct> purchaseOrderProducts = new ArrayList<PurchaseOrderProduct>();
			
			for (BaseEntity entity : purchaseOrders) {
				PurchaseOrderProduct purchaseOrderProduct = (PurchaseOrderProduct)entity;
				purchaseOrderProduct.setPurchaseOrder(null);
				purchaseOrderProducts.add(purchaseOrderProduct);
			}
			purchaseOrder.setPurchaseOrderProducts(purchaseOrderProducts);
		}
		return purchaseOrder;
		
	}
	
//	@Transactional
//	public BaseEntity save(ReceiveOrder receiveOrder) {
//		
//		BaseEntity toReturn = this.genericService.save(receiveOrder);
//		
//		for (ReceiveOrderProduct rop : receiveOrder.getReceiveOrderProducts()) {
//			rop.setReceiveOrder((ReceiveOrder)toReturn);
//			
//			if (rop.getId() == null) {
//				this.genericService.save(rop);
//				
//				if (receiveOrder.getStatus() == 2) {
//					Product product = (Product) this.genericService.find(Product.class, rop.getProduct().getId());
//					product.setQuantityInStock(product.getQuantityInStock() + rop.getQuantity());
//					this.genericService.save(product);
//				}
//			}
//		}
//		
//		return toReturn;
//	}
//	
//	public List<ReceiveOrder> findInitialReceiveOrder(Class cl, Long purchaseOrderId) throws NumberFormatException, ParseException {
//		String queryString = "SELECT PO.PURCHASE_ORDER_ID, PO.PURCHASE_ORDER_DATE, PO.SUPPLIER_ID, SP.NAME AS SP_NAME, POP.PRODUCT_ID, "
//							+ "P.NAME AS P_NAME, POP.QUANTITY, ROP.QUANTITY AS R_QUANTITY, RO.RECEIVE_ORDER_ID, "
//							+ "ROP.RECEIVE_ORDER_PRODUCT_ID "
//							+ "FROM PURCHASE_ORDER PO "
//							+ "JOIN PURCHASE_ORDER_PRODUCT POP ON PO.PURCHASE_ORDER_ID = POP.PURCHASE_ORDER_ID "
//							+ "LEFT OUTER JOIN RECEIVE_ORDER RO ON PO.PURCHASE_ORDER_ID = RO.PURCHASE_ORDER_ID "
//							+ "LEFT OUTER JOIN RECEIVE_ORDER_PRODUCT ROP ON RO.RECEIVE_ORDER_ID = ROP.RECEIVE_ORDER_ID "
//									+ "AND ROP.PRODUCT_ID = POP.PRODUCT_ID "
//							+ "JOIN SUPPLIER SP ON PO.SUPPLIER_ID = SP.SUPPLIER_ID "
//							+ "JOIN PRODUCT P ON POP.PRODUCT_ID = P.PRODUCT_ID "
//							+ "WHERE 1 = 1 "
//							;
//		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
//		paramTupleList.add(Quartet.with("PO.PURCHASE_ORDER_ID = ", "purchaseOrderId", purchaseOrderId + "", "Long"));
//		List<Object[]> list = this.genericService.getNativeByCriteria(queryString, paramTupleList, 
//				" ORDER BY P.NAME ", " ");
//		
//		List<ReceiveOrder> receiveOrders = new ArrayList<ReceiveOrder>();
//		List<ReceiveOrderProduct> receiveOrderProducts = new ArrayList<ReceiveOrderProduct>();
//		
//		DateFormat format = new SimpleDateFormat("yyyyMMdd");
//		
//		Map<Product, Integer> productQtyMap =  new HashMap<>();
//		ReceiveOrder receiveOrder = null;
//		
//		for (Object[] objects : list) {
//			Product product = new Product(new Long(objects[4].toString()), String.valueOf(objects[5]));
//			Integer qtyOrdered = Integer.valueOf(objects[6].toString());
//			Integer qtyReceived = objects[7] != null ? Integer.valueOf(objects[7].toString()) : 0;
//			
//			if (qtyReceived > 0) {
//				receiveOrder = new ReceiveOrder();
//				receiveOrder.setId(objects[7] != null ? new Long(objects[7].toString()) : null);
//				receiveOrder.setPurchaseOrder(new PurchaseOrder(new Long(objects[0].toString()), format.parse(objects[1].toString()), 
//						new Long(objects[2].toString()), String.valueOf(objects[3])));
//				receiveOrderProducts.add(new ReceiveOrderProduct(objects[8] != null ? new Long(objects[8].toString()) : null, null, 
//						product, productQtyMap.getOrDefault(product, qtyOrdered), qtyReceived));
//				receiveOrder.setReceiveOrderProducts(receiveOrderProducts);
//				receiveOrders.add(receiveOrder);
//			}
//			
//			productQtyMap.put(product, productQtyMap.getOrDefault(product, qtyOrdered) -  qtyReceived);
//		}
//		
//		for (Map.Entry<Product, Integer> productQtyEntry : productQtyMap.entrySet()) {
//			if (productQtyEntry.getValue() > 0) {
//				receiveOrder = new ReceiveOrder();
//				receiveOrder.setPurchaseOrder(new PurchaseOrder(purchaseOrderId, null, null, null));
//				receiveOrderProducts.add(new ReceiveOrderProduct(null, null, productQtyEntry.getKey(), productQtyEntry.getValue(), 0));
//				
//				receiveOrder.setReceiveOrderProducts(receiveOrderProducts);
//				
//				receiveOrders.add(receiveOrder);
//			}
//	    }
//		
//		
//		return receiveOrders;
//		
//	}
//	
//	public BaseEntity findReceiveOrder(Class cl, Long key) {
//		ReceiveOrder receiveOrder = (ReceiveOrder) this.genericService.find(cl, key);
//		
//		if (receiveOrder != null) {
//			List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
//			paramTupleList.add(Quartet.with("e.receiveOrder.id = ", "receiveOrderId", key + "", "Long"));
//			String queryStr =  "SELECT e FROM ReceiveOrderProduct e WHERE 1 = 1";
//			List<BaseEntity> receiveOrders = genericService.getByCriteria(queryStr, paramTupleList, null);
//			List<ReceiveOrderProduct> receiveOrderProducts = new ArrayList<ReceiveOrderProduct>();
//			
//			for (BaseEntity entity : receiveOrders) {
//				ReceiveOrderProduct receiveOrderProduct = (ReceiveOrderProduct)entity;
//				receiveOrderProduct.setReceiveOrder(null);
//				receiveOrderProducts.add(receiveOrderProduct);
//			}
//			receiveOrder.setReceiveOrderProducts(receiveOrderProducts);
//		}
//		return receiveOrder;
//		
//	}
//	
	
	
	// Product Sales 
	@Transactional
	public BaseEntity save(PatientSale patientSale) {
		
		if (patientSale.getDiscount() == null) {
			patientSale.setDiscount(0d);
		}
		
		BaseEntity toReturn = this.genericService.save(patientSale);
		
		List<PatientSaleProduct> removedPsps = new ArrayList<>();
		List<PatientSaleProduct> newPsps = new ArrayList<>();
		Integer leftQty = 0;
		
		for (PatientSaleProduct psp : patientSale.getPatientSaleProducts()) {
			if (psp.getStatus() == 9) {
				this.genericService.delete(PatientSaleProduct.class, Arrays.asList(new Long[]{psp.getId()}));
				removedPsps.add(psp);
				
			} else {
				leftQty += psp.getQuantity() - (psp.getDeliveryQuantity() != null ? psp.getDeliveryQuantity() : 0);
				if (psp.getDeliveryQuantity() != null && psp.getDeliveryQuantity() > 0 && psp.getStatus() == 0) {
					
					if (psp.getQuantity() > psp.getDeliveryQuantity()) {
						PatientSaleProduct pspCopy = psp.clone(psp);
						newPsps.add(pspCopy);
						pspCopy.setPatientSale((PatientSale)toReturn);
						this.genericService.save(pspCopy);
						psp.setQuantity(psp.getDeliveryQuantity());
					}
					
					if (psp.getDeliveryQuantity() != null) {
						// Decrease product available qty
						Product product = (Product) this.genericService.find(Product.class, psp.getProduct().getId());
						product.setQuantityInStock(product.getQuantityInStock() - psp.getDeliveryQuantity());
						this.genericService.save(product);
					}
					
					psp.setDeliveryDatetime(new Timestamp(new Date().getTime()));
					psp.setStatus(1);
				}
				psp.setPatientSale((PatientSale)toReturn);
				this.genericService.save(psp);
				
				
			}
		}
		
		patientSale.getPatientSaleProducts().addAll(newPsps);
		patientSale.getPatientSaleProducts().removeAll(removedPsps);
		patientSale.decreaseAmount(removedPsps);
		
		Long patientSaleStatusId = patientSale.getPatientSaleStatus().getId();
		if (patientSaleStatusId == 3L || patientSaleStatusId == 4L) {
			patientSaleStatusId = leftQty > 0 ? 4L : 5L;
		}
		patientSale.setPatientSaleStatus((PatientSaleStatus)this.genericService.find(PatientSaleStatus.class, patientSaleStatusId));
		
		
		toReturn = this.genericService.save(patientSale);
		
		return toReturn;
	}
	
	
	public BaseEntity findPatientSale(Class cl, Long key) {
		PatientSale patientSale = (PatientSale) this.genericService.find(cl, key);
		
		if (patientSale != null) {
			List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
			paramTupleList.add(Quartet.with("e.patientSale.id = ", "patientSaleId", key + "", "Long"));
			String queryStr =  "SELECT e FROM PatientSaleProduct e WHERE 1 = 1";
			List<BaseEntity> saleProducts = genericService.getByCriteria(queryStr, paramTupleList, null);
			List<PatientSaleProduct> patientSaleProducts = new ArrayList<PatientSaleProduct>();
			
			for (BaseEntity entity : saleProducts) {
				PatientSaleProduct patientSaleProduct = (PatientSaleProduct)entity;
				patientSaleProduct.setPatientSale(null);
				patientSaleProducts.add(patientSaleProduct);
			}
			patientSale.setPatientSaleProducts(patientSaleProducts);
		}
		return patientSale;
		
	}
	
	// Sale Return
	@Transactional
	public BaseEntity save(SaleReturn saleReturn) {
		
		BaseEntity toReturn = this.genericService.save(saleReturn);
		
		for (SaleReturnProduct srp : saleReturn.getSaleReturnProducts()) {
			srp.setSaleReturn((SaleReturn)toReturn);
			this.genericService.save(srp);
			
			if (saleReturn.getStatus() == 2) {
				Product product = (Product) this.genericService.find(Product.class, srp.getProduct().getId());
				product.setQuantityInStock(product.getQuantityInStock() + srp.getQuantity());
				this.genericService.save(product);
			}
		}
		
		return toReturn;
	}

	public BaseEntity findInitialSaleReturn(Class cl, Long patientSaleId) throws NumberFormatException, ParseException {
		String queryString = "SELECT PS.PATIENT_SALE_ID, PS.SALE_DATETIME, PSP.PRODUCT_ID, "
							+ "P.NAME AS P_NAME, P.PRICE AS P_PRICE, PSP.QUANTITY AS ORIGINAL_QUANTITY, "
							+ "IFNULL(SUM(SRP.QUANTITY), 0) AS QUANTITY "
							+ "FROM PATIENT_SALE PS "
							+ "JOIN PATIENT_SALE_PRODUCT PSP ON PS.PATIENT_SALE_ID = PSP.PATIENT_SALE_ID "
							+ "LEFT OUTER JOIN SALE_RETURN SR ON PS.PATIENT_SALE_ID = SR.PATIENT_SALE_ID "
							+ "LEFT OUTER JOIN SALE_RETURN_PRODUCT SRP ON SR.SALE_RETURN_ID = SRP.SALE_RETURN_ID "
							+ "JOIN PRODUCT P ON PSP.PRODUCT_ID = P.PRODUCT_ID "
							+ "WHERE 1 = 1 "
							;
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("PS.PATIENT_SALE_ID = ", "patientSaleId", patientSaleId + "", "Long"));
		List<Object[]> list = this.genericService.getNativeByCriteria(queryString, paramTupleList, 
				" ORDER BY P.NAME ", " GROUP BY PS.PATIENT_SALE_ID, PSP.PRODUCT_ID ");
		
		SaleReturn saleReturn = new SaleReturn();
		List<SaleReturnProduct> saleReturnProducts = new ArrayList<SaleReturnProduct>();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 

		for (Object[] objects : list) {
			saleReturn.setPatientSale(new PatientSale(new Long(objects[0].toString()), 
					Timestamp.valueOf(LocalDateTime.parse(objects[1].toString().substring(0, objects[1].toString().length() - 2), formatter))));
			saleReturnProducts.add(new SaleReturnProduct(null, null, 
					new Product(new Long(objects[2].toString()), String.valueOf(objects[3]), Double.valueOf(objects[4].toString())), 
					Integer.valueOf(objects[5].toString()), Integer.valueOf(objects[6].toString()), Double.valueOf(objects[4].toString())
					));
		}
		saleReturn.setSaleReturnProducts(saleReturnProducts);
		
		return saleReturn;
		
	}
	
	public BaseEntity findSaleReturn(Class cl, Long key) {
		SaleReturn saleReturn = (SaleReturn) this.genericService.find(cl, key);
		
		if (saleReturn != null) {
			List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
			paramTupleList.add(Quartet.with("e.saleReturn.id = ", "saleReturnId", key + "", "Long"));
			String queryStr =  "SELECT e FROM SaleReturnProduct e WHERE 1 = 1";
			List<BaseEntity> saleReturns = genericService.getByCriteria(queryStr, paramTupleList, null);
			List<SaleReturnProduct> saleReturnProducts = new ArrayList<SaleReturnProduct>();
			
			for (BaseEntity entity : saleReturns) {
				SaleReturnProduct saleReturnProduct = (SaleReturnProduct)entity;
				saleReturnProduct.setSaleReturn(null);
				saleReturnProducts.add(saleReturnProduct);
			}
			saleReturn.setSaleReturnProducts(saleReturnProducts);
		}
		return saleReturn;
		
	}
}
