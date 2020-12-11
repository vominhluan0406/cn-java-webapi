package com.shop.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "name", columnDefinition = "NVARCHAR(50)", nullable = false, unique = true)
	private String name;

	@Column(name = "price")
	private BigDecimal price;

	@Column(name = "date_arrive")
	private String date_arrive;

	@Column(name = "rating")
	private double rating;

	@Column(name = "buying_times")
	private long buying_times;

	@Column(name = "stock", nullable = true)
	private long stock;

	@OneToMany(mappedBy = "product")
	@Fetch(FetchMode.SUBSELECT)
	private List<Comment> comments;

	@ManyToOne
	@JoinColumn(name = "brand", referencedColumnName = "id")
	private Brand brand;

	@ManyToOne
	@JoinColumn(name = "category", referencedColumnName = "id")
	private Categories category;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	@Fetch(FetchMode.SUBSELECT)
	private List<Image> images;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "product")
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private DescriptionProduct description;

	public Product(String name, BigDecimal price, String date, Brand brand, Categories categories, long stock) {
		this.name = name;
		this.price = price;
		this.date_arrive = date;
		this.brand = brand;
		this.category = categories;
		this.stock = stock;
	}

}
