package com.campigoto.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.campigoto.dscatalog.dto.ProductDTO;
import com.campigoto.dscatalog.repositories.ProductRepository;
import com.campigoto.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class ProductServiceIT {
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductRepository repository;
	
	
	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;
	
	@BeforeEach
	void setup() throws Exception {
		

		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
		
	}
	
	@Test
	public void deleteShouldDeleteWhenIdExists() throws Exception{
		
		service.delete(existingId);

		Assertions.assertEquals(countTotalProducts-1, repository.count());
					
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() throws Exception{
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
					
	}
		
	@Test
	public void findAllPagedShouldReturnPagedWhenPage0Size10() {
		
        PageRequest pageRequest = PageRequest.of(0, 10);
        
        Page<ProductDTO> result = service.findAllPaged(pageRequest);

		Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
        Assertions.assertEquals(countTotalProducts, result.getTotalElements());
	}

	
	@Test
	public void findAllPagedShouldReturnEmptyPagedWhenPageDoesNotExist() {
		
        PageRequest pageRequest = PageRequest.of(50, 10);
        
        Page<ProductDTO> result = service.findAllPaged(pageRequest);

		Assertions.assertTrue(result.isEmpty());
	}


	
	@Test
	public void findAllPagedShouldReturnSortedPagedWhenSortByName() {
		
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));
        
        Page<ProductDTO> result = service.findAllPaged(pageRequest);

		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
	}

}
