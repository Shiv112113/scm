package com.scm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.scm.services.EmailService;

@SpringBootTest
class ScmApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private EmailService emailService;

	@Test
	void sendEmailTest() {
		emailService.sendEmail("shiv112113@gmail.com",
							"Testing Email Service 2",
							"This is a Test Message for testing Email Services");
	}

}
