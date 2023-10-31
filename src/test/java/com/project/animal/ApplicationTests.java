package com.project.animal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
class ApplicationTests {
	@Mock
//	TestController controller;
	@InjectMocks
//	TestService service;

	MockMvc mockMvc;

	@BeforeEach
	void setUp() throws Exception {
//		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@DisplayName("기본 테스트")
	@Test
	void contextLoads() {


	}
}
