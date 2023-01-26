package com.devus.userservice.common.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.devus.userservice.service.AuthService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtDecodeFilter extends OncePerRequestFilter {
	private final AuthService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header = request.getHeader("Authorization");
		if (header != null && header.startsWith("Bearer ")) {
			try {
				String accessToken = header.substring(7);

				// JWT 해석
				Algorithm algorithm = Algorithm.HMAC256("secret");
				JWTVerifier verifier = JWT.require(algorithm).withIssuer("issuer").build();
				DecodedJWT jwt = verifier.verify(accessToken);
				String username = jwt.getSubject(); // 아이디(학번)

				User user = (User) userDetailsService.loadUserByUsername(username);
				Authentication authenticationToken = new UsernamePasswordAuthenticationToken(user, null,
						user.getAuthorities());

				SecurityContextHolder.getContext().setAuthentication(authenticationToken);

			} catch (JWTVerificationException exception) {
				exception.printStackTrace();
			}
		}
		filterChain.doFilter(request, response);
	}
}