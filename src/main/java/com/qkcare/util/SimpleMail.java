package com.qkcare.util;

import java.security.Security;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SimpleMail {

	public static void sendMail(String subject, String body, String sender, String recipients, String mailHost,
			final String mailUser, final String mailPassword, boolean bcc) throws Exception {

		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", mailHost);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.timeout", "10000");
		props.put("mail.smtp.connectiontimeout", "10000");
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.put("mail.mime.charset", "UTF-8");
		props.setProperty("mail.smtp.quitwait", "false");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailUser, mailPassword);
			}
		});

		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(sender));
		message.setSubject(subject);
		message.setContent(body, "text/html; charset=UTF-8");
		if (!bcc) {
			if (recipients.indexOf(',') > 0)
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
			else
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
		} else {
			if (recipients.indexOf(',') > 0)
				message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(recipients));
			else
				message.setRecipient(Message.RecipientType.BCC, new InternetAddress(recipients));
		}
		Transport.send(message);
	}
	
	public static Session getSession(final String mailHost, final String mailUser, final String mailPassword) {
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", mailHost);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.timeout", "10000");
		props.put("mail.smtp.connectiontimeout", "10000");
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.put("mail.mime.charset", "UTF-8");
		props.setProperty("mail.smtp.quitwait", "false");
		
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailUser, mailPassword);
			}
		});
		
		return session;
	}
	
	public static void sendMail(Transport transport, Session session, String subject, String body, String sender, String recipients,
			boolean bcc) throws Exception {

		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(sender));
		message.setSubject(subject);
		message.setContent(body, "text/html; charset=UTF-8");
		if (!bcc) {
			if (recipients.indexOf(',') > 0)
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
			else
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
		} else {
			if (recipients.indexOf(',') > 0)
				message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(recipients));
			else
				message.setRecipient(Message.RecipientType.BCC, new InternetAddress(recipients));
		}
		transport.send(message);

	}

	public static void main(String args[]) throws Exception {
		SimpleMail.sendMail("test", template, "panawe@gmail.com", "panawe@gmail.com,panawe@yahoo.fr", "smtp.gmail.com",
				"panawe@gmail.com", "***",false);

	}

	public static String template = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" /><title>IPNET EXPERTS SA</title><style type=\"text/css\" media=\"screen\">/* Force Hotmail to display emails at full width */ .ExternalClass { display: block !important; width: 100%; } /* Force Hotmail to display normal line spacing */ .ExternalClass, .ExternalClass p, .ExternalClass span, .ExternalClass font, .ExternalClass td, .ExternalClass div { line-height: 100%; } body, p, h1, h2, h3, h4, h5, h6 { margin: 0; padding: 0; } body, p, td { font-family: Arial, Helvetica, sans-serif; font-size: 15px; color: #333333; line-height: 1.5em; } h1 { font-size: 24px; font-weight: normal; line-height: 24px; } body, p { margin-bottom: 0; -webkit-text-size-adjust: none; -ms-text-size-adjust: none; } img { line-height: 100%; outline: none; text-decoration: none; -ms-interpolation-mode: bicubic; } a img { border: none; } .background { background-color: #333333; } table.background { margin: 0; padding: 0; width: 100% !important; } .block-img { display: block; line-height: 0; } a { color: white; text-decoration: none; } a, a:link { color: #2A5DB0; text-decoration: underline; } table td { border-collapse: collapse; } td { vertical-align: top; text-align: left; } .wrap { width: 600px; } .wrap-cell { padding-top: 30px; padding-bottom: 30px; } .header-cell, .body-cell, .footer-cell { padding-left: 20px; padding-right: 20px; } .header-cell { background-color: #eeeeee; font-size: 24px; color: #ffffff; } .body-cell { background-color: #ffffff; padding-top: 30px; padding-bottom: 34px; } .footer-cell { background-color: #eeeeee; text-align: center; font-size: 13px; padding-top: 30px; padding-bottom: 30px; } .card { width: 400px; margin: 0 auto; } .data-heading { text-align: right; padding: 10px; background-color: #ffffff; font-weight: bold; } .data-value { text-align: left; padding: 10px; background-color: #ffffff; } .force-full-width { width: 100% !important; }</style><style type=\"text/css\" media=\"only screen and (max-width: 600px)\">@media only screen and (max-width: 600px) { body[class*=\"background\"], table[class*=\"background\"], td[class*=\"background\"] { background: #eeeeee !important; } table[class=\"card\"] { width: auto !important; } td[class=\"data-heading\"], td[class=\"data-value\"] { display: block !important; } td[class=\"data-heading\"] { text-align: left !important; padding: 10px 10px 0; } table[class=\"wrap\"] { width: 100% !important; } td[class=\"wrap-cell\"] { padding-top: 0 !important; padding-bottom: 0 !important; } }</style></head><body bgcolor=\"#eeeeee\"><center><table cellpadding=\"0\" cellspacing=\"0\" class=\"force-full-width\"><tr><td height=\"60\" valign=\"top\" class=\"header-cell\"><img width=\"196\" height=\"60\" src=\"http://www.ipnetexperts.com/images/logo.png\" alt=\"logo\"></td></tr><tr><td valign=\"top\" class=\"body-cell\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" bgcolor=\"#ffffff\"><tr><td valign=\"top\" style=\"padding-bottom:20px; background-color:#ffffff;\">Hi Bob,<br><br>We would like you to know that your order has shipped! To view your order or make any changes please <a href=\"#\">click here.</a></td></tr><tr><td><table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" bgcolor=\"#ffffff\"><tr><td align=\"center\" style=\"padding:20px 0;\"><center><table cellspacing=\"0\" cellpadding=\"0\" class=\"card\"><tr><td style=\"background-color:green; text-align:center; padding:10px; color:white; \">Shipping Details</td></tr><tr><td style=\"border:1px solid green;\"><table cellspacing=\"0\" cellpadding=\"20\" width=\"100%\"><tr><td><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" bgcolor=\"#ffffff\"><tr><td width=\"150\" class=\"data-heading\">Shipping Date:</td><td class=\"data-value\">January 4th, 2014</td></tr><tr><td width=\"150\" class=\"data-heading\">Shipping Address:</td><td class=\"data-value\">Lavender St<br>Victoria, BC<br>V8P 2W2</td></tr></table></td></tr></table></td></tr></table></center></td></tr></table> </td></tr><tr><td style=\"padding-top:20px;background-color:#ffffff;\">Thank you!<br>Your Awesome Co team</td></tr></table></td></tr><tr><td valign=\"top\" class=\"footer-cell\"> 	IPNET EXPERTS SA<br>  	TOUR IPNET, Agbalepedo, Lossossime <br>05 BP 507 Lome - Togo, +228 22 51 77 77</td></tr></table></center></body></html>";

}