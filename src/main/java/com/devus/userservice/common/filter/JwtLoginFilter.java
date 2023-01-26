package com.devus.userservice.common.filter;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.devus.userservice.model.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 로그인 성공시 JWT 생성
 * @author user
 *
 */
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {
	public JwtLoginFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) {
		try {
			User user = (User) authResult.getPrincipal();
			String username = user.getUsername(); // 아이디(학번)

			// JWT 생성
			Algorithm algorithm = Algorithm.HMAC256("secret");
			String accessToken = JWT.create()
									.withIssuer("issuer")
									.withSubject(username)
									.sign(algorithm);

			response.getWriter().write(accessToken);
		} catch (JWTCreationException | IOException exception) {
			exception.printStackTrace();
		}
	}
}