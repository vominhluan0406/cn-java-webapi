package com.shop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "vouchers")
@Data
public class Voucher {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "name")
	private String name;

	@Column(name = "start")
	private String start;

	@Column(name = "end")
	private String end;

	@Column(name = "code", unique = true)
	private String voucher;

	@Column(name = "discount_percent")
	private int discount_percent;

}
