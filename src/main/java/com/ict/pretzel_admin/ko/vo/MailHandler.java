package com.ict.pretzel_admin.ko.vo;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.internet.MimeMessage;

//	MailService 에서 사용할 클래스
public class MailHandler {
	
	private JavaMailSender mailSender;
	private MimeMessage message;
	private MimeMessageHelper messageHelper;
	
	public MailHandler(JavaMailSender mailSender) throws Exception {
		this.mailSender = mailSender;
		message = this.mailSender.createMimeMessage();
		//	true 는 멀티파트 메세지를 사용가능 (이미지 들어갈 수 있다.)
		messageHelper = new MimeMessageHelper(message, true, "utf-8");
		
		//	단순한 텍스트 메시지만 사용
		//messageHelper = new MimeMessageHelper(message, "utf-8");
	
	}
	
	//	제목
	public void setSubject(String subject) throws Exception {
		messageHelper.setSubject(subject);
	}
	
	//	내용
	public void setText(String text) throws Exception {
		messageHelper.setText(text, true);
	}
	
	//	보낸 사람의 이메일과 이름
	public void setFrom(String email, String name) throws Exception {
		messageHelper.setFrom(email, name);
	}
	
	//	받는 이메일
	public void setTO(String email) throws Exception {
		messageHelper.setTo(email);
	}
	
	//	보내기
	public void send() {
		mailSender.send(message);
	}

}
