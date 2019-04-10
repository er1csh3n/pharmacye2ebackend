package com.meijer.demo.dao;

import java.util.List;

import org.springframework.cache.annotation.CachePut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.meijer.demo.beans.Product;

//@RestResource(path="products", rel="products")
public interface ProductDao extends JpaRepository<Product, Integer> {
	
	public List<Product> findByBrand(String brand);
	
	@Query("select max(p.price) from Product p")
	public int getMaxPrice();
	
	@Query("select p from Product p where p.stock > :stock")
	public List<Product> getProducstWithStock(@Param("stock") int stock);
	
	@Override
	@CachePut(value = "products", key = "#result.id")
	Product save(Product p);
}
