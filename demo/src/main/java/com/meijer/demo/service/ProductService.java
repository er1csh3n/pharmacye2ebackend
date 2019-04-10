package com.meijer.demo.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.meijer.demo.beans.Product;
import com.meijer.demo.dao.ProductDao;
import com.meijer.demo.http.Response;

@Service
public class ProductService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

	@Autowired
	ServletContext context;

	@Autowired
	ProductDao productDao;

	public ByteArrayInputStream getProductsInPDF() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Document document = new Document();
		try {
			PdfPTable table = new PdfPTable(5);

			Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
			PdfPCell hcell;
			hcell = new PdfPCell(new Phrase("ID", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
			hcell = new PdfPCell(new Phrase("Name", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
			hcell = new PdfPCell(new Phrase("Brand", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
			hcell = new PdfPCell(new Phrase("Price", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
			hcell = new PdfPCell(new Phrase("Stock", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			for (Product p : productDao.findAll()) {
				PdfPCell cell;

				cell = new PdfPCell(new Phrase(Integer.toString(p.getId())));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(p.getName()));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(p.getBrand()));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(Integer.toString(p.getPrice())));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(Integer.toString(p.getStock())));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);
			}

			PdfWriter.getInstance(document, out);
			document.open();
			document.add(table);
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			document.close();
		}
		return new ByteArrayInputStream(out.toByteArray());
	}

	// add products list into cache
	@Cacheable("productsList")
	public List<Product> getProducts() {
		return productDao.findAll();
	}
	
	public List<Product> getProductsByPageAndSize(int page, int size) {
		return productDao.findAll(PageRequest.of(page, size)).getContent();
	}

	// remove products list from cache for updating.
	@CacheEvict(value = "productsList", allEntries = true)
	public Response saveProduct(Product product) {
		try {
			productDao.save(product);
			return new Response(true);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new Response(false);
		}
	}

	// add retrieved product into cache.
	@Cacheable(value = "products", key = "#id")
	public Product getProduct(int id) {
		return productDao.findById(id).get();
	}

	// cache moved to dao for updating purpose
	public Response updateProduct(Product product) {
		try {
			Product p = productDao.getOne(product.getId());
			p.setName(product.getName());
			p.setBrand(product.getBrand());
			p.setPrice(product.getPrice());
			p.setStock(product.getStock());
			p.setCategory(product.getCategory());
			productDao.save(p);
			return new Response(true);
		} catch (Exception e) {
			return new Response(false);
		}
	}

	// remove from cache.
	@CacheEvict(value = "products", key = "#id")
	public Response deleteProduct(int id) {
		try {
			productDao.deleteById(id);
			return new Response(true);
		} catch (Exception e) {
			return new Response(false);
		}
	}

}