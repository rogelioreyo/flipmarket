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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * User - User.java
 *
 * @author Rogelio Reyo Cachu
 * @version 1.0.0
 * @since 9/06/2018
 */
@Entity
@Table(name = "usercatalog")
public class User {

	/** The user id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long userId;

	/** The username. */
	@Size(min = 4, max = 255, message = "Minimum username length: 4 characters")
	@Column(name = "username", unique = true, nullable = false)
	@JsonProperty(value = "userAccount")
	private String username;

	/** The email. */
	@Column(name = "email", unique = true, nullable = false)
	private String email;

	/** The first name. */
	@Column(name = "first_name")
	private String firstName;

	/** The last name. */
	@Column(name = "last_name")
	private String lastName;

	/** The phone number. */
	@Column(name = "phone_number")
    @JsonIgnore
	private String phoneNumber;

	/** The status. */
	@Column(name = "status")
	private String status;

	/** The user type. */
	@Column(name = "user_type")
	private String userType;

	/** The user position. */
	@Column(name = "user_position")
	private String userPosition;

	/** The allow information share. */
	@Column(name = "allow_information_share")
	private Integer allowInformationShare;

	/** The allow email updates. */
	@Column(name = "allow_email_updates")
	private Integer allowEmailUpdates;

	/** The full name. */
	@Column(name = "full_name")
	private String fullName;

	/** The full name. */
	@Column(name = "azure_id")
	private String azureId;

	/** The id environment target. */
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "id_consumer_application", nullable = false, updatable = true, insertable = true)
//    private ConsumerApplication consumerApplication;

	/** The password. */
	@Size(min = 8, message = "Minimum password length: 8 characters")
    @JsonIgnore
	private String password;

	/** The roles. */
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
	private Set<UserRole> roles = new HashSet<UserRole>();

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserPosition() {
		return userPosition;
	}

	public void setUserPosition(String userPosition) {
		this.userPosition = userPosition;
	}

	public Integer getAllowInformationShare() {
		return allowInformationShare;
	}

	public void setAllowInformationShare(Integer allowInformationShare) {
		this.allowInformationShare = allowInformationShare;
	}

	public Integer getAllowEmailUpdates() {
		return allowEmailUpdates;
	}

	public void setAllowEmailUpdates(Integer allowEmailUpdates) {
		this.allowEmailUpdates = allowEmailUpdates;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getAzureId() {
		return azureId;
	}

	public void setAzureId(String azureId) {
		this.azureId = azureId;
	}

//	public ConsumerApplication getConsumerApplication() {
//		return consumerApplication;
//	}
//
//	public void setConsumerApplication(ConsumerApplication consumerApplication) {
//		this.consumerApplication = consumerApplication;
//	}


}
