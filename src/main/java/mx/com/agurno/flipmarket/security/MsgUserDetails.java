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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import mx.com.agurno.flipmarket.entity.User;
import mx.com.agurno.flipmarket.entity.UserRole;
import mx.com.agurno.flipmarket.repository.UserRepository;



/**
 * MsgUserDetails - MsgUserDetails.java
 *
 * @author Rogelio Reyo Cachu
 * @version 1.0.0
 * @since 9/06/2018
 */
@Service
public class MsgUserDetails implements UserDetailsService {

	/** The user repository. */
	@Autowired
	private UserRepository userRepository;

	/**
	 * Load user by username.
	 *
	 * @param username
	 *            the username
	 * @return the user details
	 * @throws UsernameNotFoundException
	 *             the username not found exception
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final User user = userRepository.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("User '" + username + "' not found");
		}
		return new org.springframework.security.core.userdetails.User(username, user.getPassword(), true, true, true, true, new ArrayList<UserRole>(user.getRoles()));
	}

}
