package com.shop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({ "id", "hibernateLazyInitializer", "handler" })
public class Role {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;

	@Column(name = "name")
	private String name;

	public Role(int id) throws NotFoundException {
		this.id = id;
		if (id == 1) {
			this.name = "ADMIN";
		} else if (id == 2) {
			this.name = "USER";
		} else {
			throw new NotFoundException("Khong tim thay Role");
		}
	}

}
