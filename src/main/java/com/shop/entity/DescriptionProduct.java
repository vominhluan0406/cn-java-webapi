package com.shop.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shop.dto.DescriptionDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "descriptions")
@JsonIgnoreProperties({ "product", "id" })
public class DescriptionProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "product_id", referencedColumnName = "id")
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Product product;

	@Column(name = "cpu")
	private String cpu;

	@Column(name = "ram")
	private String ram;

	@Column(name = "os")
	private String os;

	@Column(name = "screensize")
	private String screen_size;

	@Column(name = "battery")
	private String battery;

	@Column(name = "memory")
	private String memory;

	@Column(name = "color")
	private String color;

	@Column(name = "introduction")
	private String introduction;

	public void dtoTo(DescriptionDTO descriptionDTO, Product product) {
		this.setBattery(descriptionDTO.getBattery());
		this.setColor(descriptionDTO.getColor());
		this.setCpu(descriptionDTO.getCpu());
		this.setIntroduction(descriptionDTO.getIntroduction());
		this.setMemory(descriptionDTO.getMemory());
		this.setOs(descriptionDTO.getOs());
		this.setRam(descriptionDTO.getRam());
		this.setScreen_size(descriptionDTO.getScreen_size());

		this.product = product;
	}

	public DescriptionProduct(DescriptionDTO descriptionDTO, Product product) {
		this.setBattery(descriptionDTO.getBattery());
		this.setColor(descriptionDTO.getColor());
		this.setCpu(descriptionDTO.getCpu());
		this.setIntroduction(descriptionDTO.getIntroduction());
		this.setMemory(descriptionDTO.getMemory());
		this.setOs(descriptionDTO.getOs());
		this.setRam(descriptionDTO.getRam());
		this.setScreen_size(descriptionDTO.getScreen_size());

		this.product = product;
	}

}
