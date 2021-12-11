package com.campigoto.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campigoto.dscatalog.dto.CategoryDTO;
import com.campigoto.dscatalog.entities.Category;
import com.campigoto.dscatalog.repositories.CategoryRepository;
import com.campigoto.dscatalog.services.exceptions.DatabaseException;
import com.campigoto.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAllPaged(Pageable pageable) {
		Page<Category> list = repository.findAll(pageable);
		return list.map(x -> new CategoryDTO(x));
		  
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException ("Entity not found !"));
		
		return new CategoryDTO(entity);
	}
	
	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setname(dto.getName());
		entity = repository.save(entity);
				
		return new CategoryDTO(entity);
	}

	@Transactional
	public @Valid CategoryDTO update(Long id, @Valid CategoryDTO dto) {
		try {
			Category entity = repository.getById(id);
			entity.setname(dto.getName());
			entity = repository.save(entity);
			return new CategoryDTO(entity);
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

	
	
}
