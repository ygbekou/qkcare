package com.qkcare.util;

import java.util.Locale;

public class Constants {
	public final static String PACKAGE_NAME = "com.qkcare.model.";
	public final static String PACKAGE_IMAGING_NAME = PACKAGE_NAME + "imaging.";
	public final static String VALIDATOR_PACKAGE_NAME = "com.qkcare.validator.";
	
	public static String DOC_FOLDER = "C:\\Development\\qkcareinterface\\src\\assets\\docs\\";
	public static String IMAGE_FOLDER = "C:\\Development\\qkcareinterface\\src\\assets\\images\\";
	public static String REPORT_RESULT_FOLDER = "C:\\Development\\qkcareinterface\\src\\assets\\reports\\";  
	public static String PIC_FOLDER="C:\\Development\\qkcareinterface\\src";
	
//	public static String DOC_FOLDER = "/var/www/html/assets/docs/";
//	public static String IMAGE_FOLDER = "/var/www/html/assets/images/";
//	public static String REPORT_RESULT_FOLDER = "/var/www/html/assets/reports/";  
//	public static String PIC_FOLDER="/var/www/html/src/";
	
	public static Locale LOCALE = new Locale( "fr", "FR" );	

	
	
	public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5*60*60;
    public static final String SIGNING_KEY = "devglan123r";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
