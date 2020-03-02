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
package mx.com.agurno.flipmarket.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * UserRole - UserRole.java
 *
 * @author Rogelio Reyo Cachu
 * @version 1.0.0
 * @since 9/06/2018
 */
@Entity
@Table(name = "userroles")
public class UserRole implements GrantedAuthority {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/** The user. */
	@ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
	@JoinColumn(name = "user_id", nullable = true, updatable = true, insertable = true)
	private User user;

	/** The roles. */
	@Column(name = "roles")
	private Long roles;

	/** The name. */
	@Column(name = "name")
	private String name;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getRoles() {
		return roles;
	}

	public void setRoles(Long roles) {
		this.roles = roles;
	}

	@Override
	public String getAuthority() {
		return getName();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
