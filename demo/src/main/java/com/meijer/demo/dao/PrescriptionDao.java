package com.meijer.demo.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.meijer.demo.beans.Prescription;


public interface PrescriptionDao extends CrudRepository<Prescription, Integer>{

	public List<Prescription> findByUserId(int id);
}
