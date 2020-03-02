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

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;


/**
 * JwtTokenFilter - JwtTokenFilter.java
 *
 * @author Rogelio Reyo Cachu
 * @version 1.0.0
 * @since 9/06/2018
 */
public class JwtTokenFilter extends GenericFilterBean {

	/** The jwt token provider. */
	private JwtTokenProvider jwtTokenProvider;

	/**
	 * Instantiates a new jwt token filter.
	 *
	 * @param jwtTokenProvider
	 *            the jwt token provider
	 */
	public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	/**
	 * Do filter.
	 *
	 * @param req
	 *            the req
	 * @param res
	 *            the res
	 * @param filterChain
	 *            the filter chain
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ServletException
	 *             the servlet exception
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
			throws IOException, ServletException {

		try {
			String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
			if (token != null && jwtTokenProvider.validateToken(token)) {
				Authentication auth = token != null ? jwtTokenProvider.getAuthentication(token) : null;
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
			filterChain.doFilter(req, res);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

}
