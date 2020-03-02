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

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * JwtTokenFilterConfigurer - JwtTokenFilterConfigurer.java
 *
 * @author Rogelio Reyo Cachu
 * @version 1.0.0
 * @since 9/06/2018
 */
public class JwtTokenFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	/** The jwt token provider. */
	private JwtTokenProvider jwtTokenProvider;

	/**
	 * Instantiates a new jwt token filter configurer.
	 *
	 * @param jwtTokenProvider
	 *            the jwt token provider
	 */
	public JwtTokenFilterConfigurer(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	/**
	 * Configure.
	 *
	 * @param http
	 *            the http
	 * @throws Exception
	 *             the exception
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		JwtTokenFilter customFilter = new JwtTokenFilter(jwtTokenProvider);
		http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
	}

}
