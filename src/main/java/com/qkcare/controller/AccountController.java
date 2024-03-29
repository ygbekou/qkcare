package com.qkcare.controller;


import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.qkcare.model.Invoice;
import com.qkcare.model.InvoiceAccount;
import com.qkcare.service.AccountService;
import com.qkcare.util.Constants;


@RestController
@RequestMapping(value="/service/account")
@CrossOrigin
public class AccountController extends BaseController {
	
		private static final Logger LOGGER = Logger.getLogger(AccountController.class);
	
		@Autowired 
		@Qualifier("accountService")
		AccountService accountService;
				
		@RequestMapping(value="/invoice/save",method = RequestMethod.POST)
		public BaseEntity save(@RequestBody GenericDto dto) throws JsonParseException, 
		JsonMappingException, IOException, ClassNotFoundException {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			BaseEntity obj = (BaseEntity) mapper.readValue(dto.getJson().replaceAll("'", "\"").replaceAll("/", "\\/"),
					Class.forName(Constants.PACKAGE_NAME + "Invoice"));
			accountService.save((Invoice)obj);
			
			for (InvoiceAccount ia : ((Invoice)obj).getInvoiceAccounts()) {
				ia.setInvoice(null);
			}
			
			return obj;
		}
		
		
}
