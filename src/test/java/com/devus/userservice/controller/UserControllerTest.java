package com.devus.userservice.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.devus.userservice.controller.dto.ReqUserSaveDTO;
import com.devus.userservice.model.User;
import com.google.gson.Gson;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
public class UserControllerTest {
	private MockMvc mockMvc;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(documentationConfiguration(restDocumentation)).build();
	}

	@Test
	public void getUserTest() throws Exception {
		this.mockMvc.perform(get("/api/user").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andDo(print())
				.andDo(document("get-user"));
	}

	@Test
	public void saveUserTest() throws Exception {
		Calendar cal = Calendar.getInstance();
	    Date d = new Date(cal.getTimeInMillis());
	    
	    ReqUserSaveDTO dto = ReqUserSaveDTO.builder()
				.email("test@test.com")
				.name("테스터")
				.pwd("1234")
				.introduction("안녕하세요")
				.gitUrl("www.github.com")
				.webSiteUrl("www.blog.com")
				.groupName("test group")
				.pictureUrl("/images/test.jpg")
				.build();
        Gson gson = new Gson();
        String content = gson.toJson(dto);
        
		this.mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andDo(print())
				.andDo(document("save-user"));
	}
}