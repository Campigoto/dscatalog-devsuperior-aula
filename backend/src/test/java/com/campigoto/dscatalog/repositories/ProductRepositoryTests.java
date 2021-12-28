package com.campigoto.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.campigoto.dscatalog.entities.Product;
import com.campigoto.dscatalog.tests.Factory;

@DataJpaTest 
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repository;
	
	private long existingId = 1L;
	private long nonexistingId = 1000L;
	private long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonexistingId = 1000L;
		countTotalProducts = 25;
	}
	
	
	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		Product product = Factory.createProduct();
		product.setId(null);
		product = repository.save(product);		
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		
		repository.deleteById(existingId);
		
		Optional<Product> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
		
	}
	
	@Test
	public void deleteShouldTrhowEmptyResultDataAccessExceptionWhenIdDoesNotExists() {
		
		
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
		repository.deleteById(nonexistingId);
		
		});
		
	}
	
	@Test
	public void findByIdShouldReturnAOptionalProductNotEptyWhenIdExists() {
		
		repository.findById(existingId);
		
		Optional<Product> result = repository.findById(existingId);
		Assertions.assertTrue(result.isPresent());
	}
	
	
	@Test
	public void findByIdShouldReturnAOptionalProductEptyWhenIdNotExists() {
		
		repository.findById(nonexistingId);
		
		Optional<Product> result = repository.findById(nonexistingId);
		Assertions.assertTrue(result.isEmpty());
			}

}
