package com.qkcare.controller;


import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qkcare.domain.GenericDto;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.stocks.PatientSale;
import com.qkcare.model.stocks.PatientSaleProduct;
import com.qkcare.model.stocks.PurchaseOrder;
import com.qkcare.model.stocks.PurchaseOrderProduct;
import com.qkcare.model.stocks.ReceiveOrder;
import com.qkcare.model.stocks.ReceiveOrderProduct;
import com.qkcare.model.stocks.SaleReturn;
import com.qkcare.model.stocks.SaleReturnProduct;
import com.qkcare.service.GenericService;
import com.qkcare.service.PurchasingService;


@RestController
@RequestMapping(value="/service/purchasing")
@CrossOrigin
public class PurchasingController extends BaseController {
	
		private static final Logger LOGGER = Logger.getLogger(PurchasingController.class);
	
		@Autowired 
		@Qualifier("purchasingService")
		PurchasingService purchasingService;
		
		@Autowired
		@Qualifier("genericService")
		GenericService genericService;
		
		@RequestMapping(value="/purchaseOrder/save",method = RequestMethod.POST)
		public BaseEntity savePurchaseOrder(@RequestBody GenericDto dto) throws JsonParseException, 
		JsonMappingException, IOException, ClassNotFoundException {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			BaseEntity obj = (BaseEntity) mapper.readValue(dto.getJson()
					.replaceAll("'", "\"").replaceAll("/", "\\/"),
					this.getClass("com.qkcare.model.stocks.PurchaseOrder"));
			purchasingService.save((PurchaseOrder)obj);
			
			for (PurchaseOrderProduct pop : ((PurchaseOrder)obj).getPurchaseOrderProducts()) {
				pop.setPurchaseOrder(null);
			}
			
			return obj;
		}
		
		@RequestMapping(value="purchaseOrder/{id}",method = RequestMethod.GET)
		public BaseEntity getPurchaseOrder(@PathVariable("id") Long id) throws ClassNotFoundException {
			BaseEntity result = purchasingService.findPurchaseOrder(
					this.getClass("com.qkcare.model.stocks.PurchaseOrder"), id);
			
			return result != null ? result : new PurchaseOrder();
		}
		
		@RequestMapping(value="/receiveOrder/save",method = RequestMethod.POST)
		public BaseEntity saveReceiveOrder(@RequestBody GenericDto dto) throws JsonParseException, 
		JsonMappingException, IOException, ClassNotFoundException {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			BaseEntity obj = (BaseEntity) mapper.readValue(dto.getJson()
					.replaceAll("'", "\"").replaceAll("/", "\\/"),
					this.getClass("com.qkcare.model.stocks.ReceiveOrder"));
			purchasingService.save((ReceiveOrder)obj);
			
			for (ReceiveOrderProduct rop : ((ReceiveOrder)obj).getReceiveOrderProducts()) {
				rop.setReceiveOrder(null);
			}
			
			return obj;
		}
		
		@RequestMapping(value="purchaseOrder/newReceiveOrder/{id}",method = RequestMethod.GET)
		public List<ReceiveOrder> getInitialReceive(@PathVariable("id") Long id) 
				throws ClassNotFoundException, NumberFormatException, ParseException {
			List<ReceiveOrder> results = purchasingService.findInitialReceiveOrder(
					this.getClass("com.qkcare.model.stocks.PurchaseOrder"), id);
			
			return results != null ? results : Collections.EMPTY_LIST;
		}
		
		@RequestMapping(value="receiveOrder/{id}",method = RequestMethod.GET)
		public BaseEntity getReceiveOrder(@PathVariable("id") Long id) throws ClassNotFoundException {
			BaseEntity result = purchasingService.findReceiveOrder(
					this.getClass("com.qkcare.model.stocks.ReceiveOrder"), id);
			
			return result != null ? result : new ReceiveOrder();
		}

		@RequestMapping(value="/patientSale/save",method = RequestMethod.POST)
		public BaseEntity savePatientSale(@RequestBody GenericDto dto) throws JsonParseException, 
		JsonMappingException, IOException, ClassNotFoundException {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			BaseEntity obj = (BaseEntity) mapper.readValue(dto.getJson()
					.replaceAll("'", "\"").replaceAll("/", "\\/"),
					this.getClass("com.qkcare.model.stocks.PatientSale"));
			purchasingService.save((PatientSale)obj);
			
			for (PatientSaleProduct psp : ((PatientSale)obj).getPatientSaleProducts()) {
				psp.setPatientSale(null);
			}
			
			return obj;
		}
		
		@RequestMapping(value="/patientSaleProduct/save",method = RequestMethod.POST)
		public BaseEntity savePatientSale(@RequestBody PatientSaleProduct patientSaleProduct) {

			BaseEntity obj = purchasingService.save(patientSaleProduct);
			
			return obj;
		}
		
		@RequestMapping(value="patientSale/{id}",method = RequestMethod.GET)
		public BaseEntity getPatientSale(@PathVariable("id") Long id) throws ClassNotFoundException {
			BaseEntity result = purchasingService.findPatientSale(
					this.getClass("com.qkcare.model.stocks.PatientSale"), id);
			
			return result != null ? result : new PatientSale();
		}
		
		@RequestMapping(value="/saleReturn/save",method = RequestMethod.POST)
		public BaseEntity saveSaleReturn(@RequestBody GenericDto dto) throws JsonParseException, 
		JsonMappingException, IOException, ClassNotFoundException {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			BaseEntity obj = (BaseEntity) mapper.readValue(dto.getJson()
					.replaceAll("'", "\"").replaceAll("/", "\\/"),
					this.getClass("com.qkcare.model.stocks.SaleReturn"));
			purchasingService.save((SaleReturn)obj);
			
			for (SaleReturnProduct psp : ((SaleReturn)obj).getSaleReturnProducts()) {
				psp.setSaleReturn(null);
			}
			
			return obj;
		}
		
		@RequestMapping(value="patientSale/newSaleReturn/{id}",method = RequestMethod.GET)
		public BaseEntity getInitialReturn(@PathVariable("id") Long id) 
				throws ClassNotFoundException, NumberFormatException, ParseException {
			BaseEntity result = purchasingService.findInitialSaleReturn(
					this.getClass("com.qkcare.model.stocks.PatientSale"), id);
			
			return result != null ? result : new PatientSale();
		}
		
		@RequestMapping(value="saleReturn/{id}",method = RequestMethod.GET)
		public BaseEntity getSaleReturn(@PathVariable("id") Long id) throws ClassNotFoundException {
			BaseEntity result = purchasingService.findSaleReturn(
					this.getClass("com.qkcare.model.stocks.SaleReturn"), id);
			
			return result != null ? result : new SaleReturn();
		}
		
}
