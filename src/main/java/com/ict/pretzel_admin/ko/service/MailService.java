package com.ict.pretzel_admin.ko.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ict.pretzel_admin.ko.vo.MailHandler;

@Service
public class MailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	public void sendEmail(String randomNumber, String toMail) {
		try {
			MailHandler sendMail = new MailHandler(mailSender);
			//	메일 제목
			sendMail.setSubject("Pretzel 비밀번호 초기화 메일입니다.");
			//	메일 내용
			sendMail.setText(""
                    + "<table><tbody>"
                    + "<tr><td><h2>Pretzel 비밀번호가 변경되었습니다.</h2></td></tr>"
                    + "<tr><td><font size='20px'>비밀번호 : "+ randomNumber +"</font></td></tr>"
                    + "</tbody></table>");
			//	받는 사람
			sendMail.setTO(toMail);
			//	보내기
			sendMail.send();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
}
