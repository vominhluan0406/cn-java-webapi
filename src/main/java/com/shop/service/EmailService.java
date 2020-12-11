package com.shop.service;

import javax.mail.MessagingException;

public interface EmailService {

	void sendForgetPassword(String email) throws MessagingException;

	void sendMailVerify(String email) throws MessagingException;
}
