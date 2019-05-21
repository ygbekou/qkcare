package com.qkcare.service;

import org.springframework.stereotype.Service;

import com.qkcare.model.BaseEntity;
import com.qkcare.model.ContactUsMessage;

@Service(value="contactUsMessageService")
public interface ContactUsMessageService {
	
	public BaseEntity save(ContactUsMessage contactUsMessage);
}
