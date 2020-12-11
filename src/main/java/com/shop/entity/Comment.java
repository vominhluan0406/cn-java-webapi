package com.shop.entity;

import javax.persistence.CascadeType;
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

@Entity
@Data
@Table(name = "comments")
@JsonIgnoreProperties({ "product" })
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@JoinColumn(name = "customer_id", referencedColumnName = "id")
	@JsonIgnoreProperties({ "username", "password", "address", "phone_number", "gender", "role" })
	private Customer customer;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "product_id", referencedColumnName = "id")
	private Product product;

	@Column(name = "message")
	private String message;

	@Column(name = "date")
	private String date;

	@Column(name = "rate") // Đánh giá 1,2,3,4,5 sao
	private int rate;

	@Column(name = "bought") // Comment của người mua hàng hay chưa
	private boolean bought;

}
