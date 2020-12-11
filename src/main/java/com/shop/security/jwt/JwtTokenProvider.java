package com.shop.security.jwt;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.shop.entity.form.CustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {

	private final String JWT_SECRET = "com.web";

	private final long JWT_EXPIRATION = 3600000L;

	private final long JWT_EXPIRATION_FW = 900000L;

	public String generateToken(CustomUserDetails customUserDetails) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + JWT_EXPIRATION);

		return Jwts.builder().setSubject(customUserDetails.getCustomer().getUsername()).setIssuedAt(now)
				.setExpiration(expiry).signWith(SignatureAlgorithm.HS512, JWT_SECRET).compact();
	}

	public String generateTokenFW(String email) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + JWT_EXPIRATION_FW);

		return Jwts.builder().setSubject(email).setIssuedAt(now).setExpiration(expiry)
				.signWith(SignatureAlgorithm.HS512, JWT_SECRET).compact();
	}

	public String getUserNameFromJWT(String token) {
		Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
			return true;
		} catch (MalformedJwtException e) {
			log.error("MalformedJwtException");
		} catch (ExpiredJwtException e) {
			log.error("MalformedJwtException");
		} catch (UnsupportedJwtException e) {
			log.error("UnsupportedJwtException");
		} catch (IllegalArgumentException e) {
			log.error("IllegalArgumentException");
		}
		return false;
	}
}
