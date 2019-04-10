package com.meijer.demo.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.meijer.demo.beans.Product;
import com.meijer.demo.dao.ProductDao;
import com.meijer.demo.http.Response;
import com.meijer.demo.service.ProductService;

@RestController
public class ProductController {

	@Autowired
	ProductDao productDao;

	@Autowired
	ProductService productService;

//	@GetMapping(value = "/products", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@GetMapping(value = "/products")
	public List<Product> getProducts(@RequestParam(value = "page", required = false) Optional<Integer> page, 
			@RequestParam(value = "size", required = false) Optional<Integer> size) {
		if (page.isPresent() && size.isPresent()) {
			return productService.getProductsByPageAndSize(page.get(), size.get());
		} else {
			return productService.getProducts();
		}
	}
	
	@GetMapping("/list-products")
	public List<Product> listProducts() {
		return productService.getProducts();
	}

	@GetMapping("/products/{id}")
	public Product getProduct(@PathVariable int id) {
		return productService.getProduct(id);
	}

	@GetMapping(value = "/products_pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> getProductsInPDF() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=products.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(productService.getProductsInPDF()));
	}
	
	@PostMapping("/products")
	public Response addProduct(@Valid @RequestBody Product product) {
		return productService.saveProduct(product);
	}
	
	@PutMapping("/products")
	public Response updateProduct(@RequestBody Product product) {
		return productService.updateProduct(product);
	}
	
	@DeleteMapping("/products/{id}")
	public Response deleteProduct(@PathVariable int id) {
		return productService.deleteProduct(id);
	}
}
