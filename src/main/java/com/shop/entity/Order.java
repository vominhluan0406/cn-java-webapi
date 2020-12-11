package com.shop.entity;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "discount")
	private BigDecimal discount;

	@Column(name = "total")
	private BigDecimal total;

	@Column(name = "shipping_address")
	private String shipping_address;

	@Column(name = "date")
	private String date;

	@Column(name = "note")
	private String note;

	@Column(name = "payment_method")
	private String payment_method;

	@ManyToOne
	@JoinColumn(name = "status", referencedColumnName = "id")
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Status status;

	@ManyToOne
	@JoinColumn(name = "customer_id", referencedColumnName = "id")
	@JsonIgnoreProperties({ "password","favorite_list" })
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Customer customer;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Set<OrderDetail> details;

	public Order(BigDecimal total, BigDecimal discount, String shipping_address, String date, Status status,
			String note, Customer customer, String payment_method) {
		this.total = total;
		this.discount = discount;
		this.shipping_address = shipping_address;
		this.date = date;
		this.status = status;
		this.customer = customer;
		this.note = note;
		this.payment_method = payment_method;
	}
}
