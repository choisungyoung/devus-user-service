package com.devus.userservice.common.provider;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일
    
    private static final String ACCESS_TOKEN_HEADER_NAME = "Access-Token";
    private static final String REFRESH_TOKEN_HEADER_NAME = "Refresh-Token";
    
    
    private final Key accessTokenKey;
    private final Key refreshTokenKey;

    public JwtTokenProvider(@Value("${devus.jwt.secret.access-token}") 
						    String accessTokenSecret,
						    @Value("${devus.jwt.secret.refresh-token}") 
						    String refreshTokenSecret) {

    	this.accessTokenKey  = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenSecret));
    	this.refreshTokenKey  = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshTokenSecret));
    }
    
    public String buildAccessToken(Authentication authentication) {
    	return buildToken(authentication, accessTokenKey, ACCESS_TOKEN_EXPIRE_TIME);
    }
    
    public String buildRefreshToken(Authentication authentication) {
    	return buildToken(authentication, refreshTokenKey, REFRESH_TOKEN_EXPIRE_TIME);
    }
    
    public String getAccessToken(HttpServletRequest request) {
    	return request.getHeader(ACCESS_TOKEN_HEADER_NAME);
    }
    
    public String getRefeshToken(HttpServletRequest request) {
    	return request.getHeader(REFRESH_TOKEN_HEADER_NAME);
    }
    
    public boolean validateAccessToken(String token) {
    	return validateToken(accessTokenKey, token);
    }
    
    public boolean validateRefreshToken(String token) {
    	return validateToken(refreshTokenKey, token);
    }
    
    public Authentication getAuthentication(String accessToken) {
    	Claims claims = parseClaims(accessToken);
    	
    	if (!claims.containsKey(AUTHORITIES_KEY)) {
    		throw new RuntimeException("권한 정보가 없는 토큰입니다.");
    	}
    	
    	Collection<GrantedAuthority> authorities 
    			= Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
    					.map(r -> new SimpleGrantedAuthority(r))
    					.collect(Collectors.toList());
    	
    	UserDetails principal = new User(claims.getSubject(), "", authorities);
    	
    	return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }
    
    private String buildToken(Authentication authentication, Key key, long expiredTime) {
    	String authorities = authentication.getAuthorities().stream()
									.map(a -> a.getAuthority())
									.collect(Collectors.joining(","));
    	Long now = new Date().getTime();
    	Date expiredDate = new Date(now + expiredTime);
    	
    	return Jwts.builder()
	    			.setSubject(authentication.getName())
	    			.claim(AUTHORITIES_KEY, authorities)
	    			.setExpiration(expiredDate)
	    			.signWith(key, SignatureAlgorithm.HS512)
	    			.compact();
    }
    
    private boolean validateToken(Key key, String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(accessTokenKey).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
