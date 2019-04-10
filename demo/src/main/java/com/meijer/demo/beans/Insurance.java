package com.meijer.demo.beans;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table (name="INSURANCE")
public class Insurance {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PRODUCT_SEQ_GEN")
	@SequenceGenerator(name = "PRODUCT_SEQ_GEN", sequenceName = "PRODUCT_SEQ", allocationSize = 1)
	private int id;
	
	@Column
	private String name;
	
	@OneToOne(mappedBy = "insurance", cascade = CascadeType.ALL)
	private InsuranceDetail insuranceDetail;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InsuranceDetail getInsuranceDetail() {
		return insuranceDetail;
	}

	public void setInsuranceDetail(InsuranceDetail insuranceDetail) {
		this.insuranceDetail = insuranceDetail;
	}

	@Override
	public String toString() {
		return "Insurance [id=" + id + ", name=" + name + ", insuranceDetail=" + insuranceDetail + "]";
	}
	
	

}
