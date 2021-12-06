package com.campigoto.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campigoto.dscatalog.dto.CategoryDTO;
import com.campigoto.dscatalog.dto.ProductDTO;
import com.campigoto.dscatalog.entities.Category;
import com.campigoto.dscatalog.entities.Product;
import com.campigoto.dscatalog.repositories.CategoryRepository;
import com.campigoto.dscatalog.repositories.ProductRepository;
import com.campigoto.dscatalog.services.exceptions.DatabaseException;
import com.campigoto.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
		
	@Autowired
	private CategoryRepository  categoryRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
		Page<Product> list = repository.findAll(pageRequest);
		return list.map(x -> new ProductDTO(x));
		  
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException ("Entity not found !"));
		
		return new  ProductDTO(entity, entity.getCategories()) ;
	}
	
	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
				
		return new ProductDTO(entity);
	}

	@Transactional
	public @Valid ProductDTO update(Long id, @Valid ProductDTO dto) {
		try {
			Product entity = repository.getById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ProductDTO(entity);
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
			
		}
	}

	
	public void delete(Long id) {
	
		try {
			repository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e ) {
			throw new ResourceNotFoundException("Id not found " + id);
			} 
		catch (DataIntegrityViolationException e ){
			throw new DatabaseException("Integrity violation");
					
		}
		
	}
	private void copyDtoToEntity(ProductDTO dto, Product entity) {
			entity.setName(dto.getName());
			entity.setDescription(dto.getDescription());
			entity.setDate(dto.getDate());
			entity.setDescription(dto.getDescription());
			entity.setImgURL(dto.getImg_URL());
			
			entity.getCategories().clear();
			for (CategoryDTO catDto : dto.getCategories()) {
				Category category = categoryRepository.getById(catDto.getId());
				entity.getCategories().add(category);
			}
	}

	
	
}
