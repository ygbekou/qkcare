package com.qkcare.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qkcare.model.BaseEntity;
import com.qkcare.model.ContactUsMessage;
import com.qkcare.service.ContactUsMessageService;


@RestController
@RequestMapping(value="/service/ContactUsMessage")
@CrossOrigin
public class ContactUsMessageController extends BaseController {

		@Autowired
		@Qualifier("contactUsMessageService")
		ContactUsMessageService contactUsMessageService;
	
	
		@RequestMapping(value="/save",method = RequestMethod.POST)
		public BaseEntity save(@RequestBody ContactUsMessage contactUsMessage) {				
			this.contactUsMessageService.save(contactUsMessage);			
			return contactUsMessage;
		}
		
}
