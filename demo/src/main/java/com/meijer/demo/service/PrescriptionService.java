package com.meijer.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meijer.demo.beans.Prescription;
import com.meijer.demo.beans.PrescriptionProduct;
import com.meijer.demo.beans.Product;
import com.meijer.demo.dao.PrescriptionDao;
import com.meijer.demo.dao.PrescriptionProductDao;
import com.meijer.demo.dao.ProductDao;
import com.meijer.demo.dao.UserDao;
import com.meijer.demo.http.Response;
import com.meijer.demo.security.SecurityUtils;

@Service
@Transactional
public class PrescriptionService {

	@Autowired
	PrescriptionDao prescriptionDao;

	@Autowired
	ProductDao productDao;
	
	@Autowired
	PrescriptionProductDao prescriptionProductDao;
	
	@Autowired
	UserDao userDao;
	
//	@Autowired
//	OrderProcessService orderProcessService;

	public Response addPrescription(Prescription prescription, Authentication authentication) {
		try {
			List<PrescriptionProduct> purchases = prescription.getPurchases();
			purchases.forEach((prescriptionProduct) -> {
				Product product = (Product) productDao.findById(prescriptionProduct.getProduct().getId()).get();
				prescriptionProduct.setProduct(product);
				prescriptionProduct.setPrescription(prescription);
			});
			prescription.setUser(userDao.findByUsername(authentication.getName()));
			prescriptionDao.save(prescription);
			// send to kafka for further processing
//			order.setPurchases(purchases);
//			orderProcessService.processOrder(order);
			return new Response(true);
		} catch (Exception e) {
			return new Response(false);
		}
	}

	public Response editPrescription(Prescription prescription) {
		try {
			Prescription p = (Prescription) prescriptionDao.findById(prescription.getId()).get();
			List<PrescriptionProduct> purchasesToRemove = p.getPurchases();
			
			List<PrescriptionProduct> purchases = prescription.getPurchases();
			// handled update and add products in order
			purchases.forEach((prescriptionProduct) -> {
				Product product = (Product) productDao.findById(prescriptionProduct.getProduct().getId()).get();
				prescriptionProduct.setProduct(product);
				prescriptionProduct.setPrescription(p);
			});
			
			// handle remove products in order
			if(purchases.size() > 0) {
				purchasesToRemove = purchasesToRemove.stream().filter((prescriptionProduct) -> {
					return !purchases.contains(prescriptionProduct);
				}).collect(Collectors.toList());
			}
			
			p.setPurchases(purchases);
			prescriptionDao.save(p);
			
			deletePrescriptionProducts(purchasesToRemove);	
						
			return new Response(true);
		} catch (Exception e) {
			System.out.println(e);
			return new Response(false);
		}
	}
	
	// TODO: move to OrderProductService
	public void deletePrescriptionProducts(List<PrescriptionProduct> purchases) {
		prescriptionProductDao.deleteAll(purchases);	
	}
	
	public List<Prescription> getPrescriptions(Authentication authentication) {
//		if(SecurityUtils.isAdmin(authentication.getAuthorities())) {
			return (List<Prescription>)prescriptionDao.findAll();
//		} else {
//			return prescriptionDao.findByUserId(userDao.findByUsername(authentication.getName()).getId());
//		}
	}
	
	@Cacheable("slowTestResults")
	public String slowTest(String x) {
		try {
			Thread.sleep(3000);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return new String(x.toLowerCase());
	}

}
