package com.qkcare.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.javatuples.Quartet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.qkcare.model.BaseEntity;
import com.qkcare.model.Company;
import com.qkcare.model.ContactUsMessage;

@Service(value = "contactUsMessageService")
public class ContactUsMessageServiceImpl implements ContactUsMessageService {

	@Autowired
	GenericService genericService;

	@Autowired
	@Qualifier("myMailSender")
	MyMailSender mailSender;

	@Transactional
	public BaseEntity save(ContactUsMessage contactUsMessage) {

		ContactUsMessage saveMessage = (ContactUsMessage) genericService.save(contactUsMessage);

		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();

		Company company = this.genericService.getCompany("EN");

		String emailMessage = "Name :    " + contactUsMessage.getName() + "\n" + "Phone Number:    "
				+ contactUsMessage.getPhone() + "\n\n" + contactUsMessage.getMessage();
		try {
			mailSender.sendMail(contactUsMessage.getEmail(), company.getToEmail().split(","),
					"Contact Message from " + contactUsMessage.getName(), emailMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return saveMessage;

	}

}
