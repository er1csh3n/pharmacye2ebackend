package com.meijer.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meijer.demo.beans.PrescriptionProduct;

public interface PrescriptionProductDao extends JpaRepository<PrescriptionProduct, Integer>{

}
