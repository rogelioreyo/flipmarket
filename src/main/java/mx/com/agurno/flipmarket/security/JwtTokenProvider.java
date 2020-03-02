/****************************************************************************
 * 
 * Copyright (C) CEMEX S.A.B de C.V 2018, Inc - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * 
 * Proprietary and confidential.
 * 
 * Written by Rogelio Reyo Cachu, 9/06/2018
 * 
 * We keep our License Statement under regular review and reserve the right 
 * to modify this License Statement from time to time.
 * 
 * Should you have any questions or comments about any of the above, 
 * please contact ethos@cemex.com for assistance or visit www.cemex.com 
 * if you need additional information or have any questions.
 * 
 ****************************************************************************/
package mx.com.agurno.flipmarket.security;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import mx.com.agurno.flipmarket.entity.UserRole;
import mx.com.agurno.flipmarket.exception.CustomException;

/**
 * JwtTokenProvider - JwtTokenProvider.java
 *
 * @author Rogelio Reyo Cachu
 * @version 1.0.0
 * @since 9/06/2018
 */
@Component
public class JwtTokenProvider implements AuthenticationProvider {

	/** The secret key. */
	@Value("${security.jwt.token.secret-key:secret-key}")
	private String secretKey;

	/** The validity in months. */
	@Value("${security.jwt.token.expire-length:1}")
	private int validityInMonths = 1; // 1h

	/** The validity in milliseconds. */
	@Value("${security.jwt.token.expire-length:3600000}")
	private long validityInMilliseconds = 3600000; // 1h

	/** The my user details. */
	@Autowired
	private MsgUserDetails myUserDetails;

	/**
	 * Inits the.
	 */
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	/**
	 * Creates the token.
	 *
	 * @param username
	 *            the username
	 * @param roles
	 *            the roles
	 * @return the string
	 */
	public String createToken(String username, Set<UserRole> roles) {

		Claims claims = Jwts.claims().setSubject(username);
		List<String> rolesList = new ArrayList<String>();
		for(UserRole role: roles) {
			rolesList.add(role.getAuthority());
		}
		claims.put("auth", rolesList);
		Date now = new Date();
		Date validity = DateUtils.addMonths(now, validityInMonths);
		return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(validity)
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}

	/**
	 * Gets the authentication.
	 *
	 * @param token
	 *            the token
	 * @return the authentication
	 */
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	/**
	 * Gets the username.
	 *
	 * @param token
	 *            the token
	 * @return the username
	 */
	public String getUsername(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	/**
	 * Resolve token.
	 *
	 * @param req
	 *            the req
	 * @return the string
	 */
	public String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}

	/**
	 * Validate token.
	 *
	 * @param token
	 *            the token
	 * @return true, if successful
	 */
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			throw new CustomException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Authenticate.
	 *
	 * @param authentication
	 *            the authentication
	 * @return the authentication
	 * @throws AuthenticationException
	 *             the authentication exception
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UserDetails userDetails = myUserDetails.loadUserByUsername(authentication.getName().toLowerCase());
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	/**
	 * Supports.
	 *
	 * @param authentication
	 *            the authentication
	 * @return true, if successful
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

}
