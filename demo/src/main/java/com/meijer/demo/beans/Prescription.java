package com.meijer.demo.beans;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "PRESCRIPTION")
public class Prescription {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PRESCRIPTION_SEQ_GEN")
	@SequenceGenerator(name = "PRESCRIPTION_SEQ_GEN", sequenceName = "PRESCRIPTION_SEQ", allocationSize = 1)
	private int id;
	@Column
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date purchase_date;
	@OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<PrescriptionProduct> purchases;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	@JsonIgnore
	User user;

	public Prescription() {
		super();
	}

	public Prescription(Date purchase_date, List<PrescriptionProduct> purchases) {
		super();
		this.purchase_date = purchase_date;
		this.purchases = purchases;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getPurchase_date() {
		return purchase_date;
	}

	public void setPurchase_date(Date purchase_date) {
		this.purchase_date = purchase_date;
	}

	public List<PrescriptionProduct> getPurchases() {
		return purchases;
	}

	public void setPurchases(List<PrescriptionProduct> purchases) {
		this.purchases = purchases;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", purchase_date=" + purchase_date + ", purchases=" + purchases + "]";
	}

}
