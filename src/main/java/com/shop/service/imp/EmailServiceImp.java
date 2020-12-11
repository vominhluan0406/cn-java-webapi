package com.shop.service.imp;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.shop.entity.form.Mail;
import com.shop.security.jwt.JwtTokenProvider;
import com.shop.service.EmailService;

@Service
public class EmailServiceImp implements EmailService {

	@Autowired
	private JavaMailSender emailSender;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private SpringTemplateEngine springTemplateEngine;

	@Override
	public void sendForgetPassword(String email) throws MessagingException {
		Mail mail = new Mail();
		mail.setFrom("webxemaybarcelona@gmail.com");
		mail.setMailTo(email);
		mail.setSubject("\"Techworld Reset Password Email");

		String token = jwtTokenProvider.generateTokenFW(email);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("name", email);
		model.put("url", "https://tech-world.herokuapp.com/forget_password?token=" + token);
		mail.setProps(model);
		MimeMessage message = emailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());
		
		Context context = new Context();
		context.setVariables(mail.getProps());

		String html = springTemplateEngine.process("forget-password-template", context);
		helper.setTo(mail.getMailTo());
		helper.setText(html, true);
		helper.setSubject(mail.getSubject());
		helper.setFrom(mail.getFrom());
		emailSender.send(message);
	}

	@Override
	public void sendMailVerify(String email) throws MessagingException {
		Mail mail = new Mail();
		mail.setFrom("webxemaybarcelona@gmail.com");
		mail.setMailTo(email);
		mail.setSubject("\"Techworld Verify Email");

		String token = jwtTokenProvider.generateTokenFW(email);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("name", email);
		model.put("url", "https://tech-world.herokuapp.com/verify?token=" + token);
		mail.setProps(model);
		MimeMessage message = emailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());
		// helper.addAttachment("template-cover.png", new
		// ClassPathResource("javabydeveloper-email.PNG"));

		Context context = new Context();
		context.setVariables(mail.getProps());

		String html = springTemplateEngine.process("verify-template", context);
		helper.setTo(mail.getMailTo());
		helper.setText(html, true);
		helper.setSubject(mail.getSubject());
		helper.setFrom(mail.getFrom());
		emailSender.send(message);

	}

}
