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
package mx.com.agurno.flipmarket.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mx.com.agurno.flipmarket.entity.User;


/**
 * The Interface UserRepository.
 */
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Find by username.
	 *
	 * @param username
	 *            the username
	 * @return the user
	 */
	User findByUsername(String username);

	@Query("SELECT u FROM User u, UserRole ur "
			+ "WHERE "
			+ "ur.user = u "
			+ "AND ur.name = :name ")
	Page<User> findAllByRole(@Param("name")String name, Pageable pageable);
	
	User findByEmail(String email);

}
