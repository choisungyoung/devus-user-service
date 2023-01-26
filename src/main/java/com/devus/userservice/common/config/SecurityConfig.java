package com.devus.userservice.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.devus.userservice.common.filter.JwtDecodeFilter;
import com.devus.userservice.common.filter.JwtLoginFilter;
import com.devus.userservice.service.AuthService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private final JwtDecodeFilter jwtDecodeFilter;
	private final AuthService userDetailsService;

	public SecurityConfig(JwtDecodeFilter jwtDecodeFilter, AuthService userDetailsService) {
		this.jwtDecodeFilter = jwtDecodeFilter;
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(userDetailsService);
		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

		JwtLoginFilter jwtLoginFilter = new JwtLoginFilter(authenticationManager);
		jwtLoginFilter.setUsernameParameter("email");
		jwtLoginFilter.setPasswordParameter("pwd");

		return http.csrf().disable()
					.formLogin().disable()
					.httpBasic().disable()
					.authorizeHttpRequests()
						.requestMatchers("/login").permitAll()
						.requestMatchers("/admin").hasAnyRole("ADMIN")
						.anyRequest().authenticated()
					.and()
					.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
					.authenticationManager(authenticationManager)
					.addFilterBefore(jwtDecodeFilter, UsernamePasswordAuthenticationFilter.class)
					.addFilterAt(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class)
					.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() { // 비밀번호 암호화
		return new BCryptPasswordEncoder();
	}
}