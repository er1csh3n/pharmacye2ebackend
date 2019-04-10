package com.meijer.demo.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.meijer.demo.beans.Prescription;
import com.meijer.demo.dao.PrescriptionDao;
import com.meijer.demo.http.Response;
import com.meijer.demo.service.PrescriptionService;

@RestController
public class PrescriptionController {
	
	@Autowired
	PrescriptionDao prescriptionDao;
	
	@Autowired
	PrescriptionService prescriptionService;
	
//	@Autowired
//	PrescriptionsReportProducer prescriptionsReportProducer;

	@GetMapping("/prescriptions")
//	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
	public List<Prescription> getPrescriptions(Authentication authentication) {
		System.out.println("GetPrescriptions is invoking!");
		return prescriptionService.getPrescriptions(authentication);
	}
	
	@GetMapping("/prescriptions/{id}")
	public Prescription getPrescription(@PathVariable int id) {
		return prescriptionDao.findById(id).get();
	}
	
	public void printPrescriptions() {
		System.out.println(Arrays.asList((List<Prescription>) prescriptionDao.findAll()));
	}
	
	@PostMapping("/prescriptions")
//	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
	public Response postPrescriptions(@RequestBody Prescription prescription, Authentication authentication) {
		return prescriptionService.addPrescription(prescription, authentication);
	}
	
	@PutMapping("/prescriptions")
//	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
	public Response putPrescriptions(@RequestBody Prescription prescription) {
		return prescriptionService.editPrescription(prescription);
	}
	
//	@GetMapping("/prescriptions_report")
//	public void getPrescriptionsReport(Authentication authentication) {
//		prescriptionsReportProducer.sendPrescriptionsForReport(authentication, getPrescriptions(authentication));
//	}
	
}
