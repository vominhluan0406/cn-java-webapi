package com.shop.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@Table(name = "users")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Customer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8034274775365214252L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	// Username dùng email
	@Column(name = "username", nullable = false, unique = true)
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "first_name")
	private String first_name;

	@Column(name = "last_name")
	private String last_name;

	@Column(name = "address")
	private String address;

	@Column(name = "phone")
	private String phone_number;

	@Column(name = "gender")
	private int gender;

	@Column(name = "verified")
	private boolean verified;

	@ManyToOne
	@JoinColumn(name = "role")
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Role role;

}
