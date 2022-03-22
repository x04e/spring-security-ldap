package com.x04e.springsecurityldap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.FormLoginRequestBuilder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SpringSecurityLdapApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void loginWithPassword() throws Exception {
		FormLoginRequestBuilder login = formLogin()
			.user("ben")
			.password("benspassword");

		mockMvc
			.perform(login)
			.andExpect(authenticated().withUsername("ben"));
	}

	@Test
	public void loginWithInvalidPassword() throws Exception {
		FormLoginRequestBuilder login = formLogin()
			.user("ben")
			.password("invalidpassword");

		mockMvc
			.perform(login)
			.andExpect(unauthenticated());
	}

	@Test
	public void loginWithNoCredentials() throws Exception {
		FormLoginRequestBuilder login = formLogin();

		mockMvc
			.perform(login)
			.andExpect(unauthenticated());
	}

}
