package org.yearup.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.yearup.exception.DataAccessException;
import org.yearup.exception.DuplicateResourceException;
import org.yearup.exception.InvalidInputException;
import org.yearup.exception.ResourceNotFoundException;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.repository.CategoryRepository;

import java.util.List;


@Service
public class CategoryService
{
    private final CategoryRepository categoryRepository;
    private final ProductService productService;

    public CategoryService(CategoryRepository categoryRepository,ProductService productService)
    {
        this.categoryRepository = categoryRepository;
        this.productService = productService;
    }

    public List<Category> getAllCategories()
    {

        return categoryRepository.findAll();
        // get all categories
    }

    public Category getById(int categoryId)
    {
        // get category by id
        if (categoryId <= 0) {
            throw new InvalidInputException("Category ID must be a positive number");
        }
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

    }
    @Transactional
    public Category create(Category category)
    {
        if (category.getName() == null || category.getName().isBlank()) {
            throw new InvalidInputException("Category name cannot be empty");
        }

        //Checking through the list of names that may be a match
        if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new DuplicateResourceException(
                    "Category with name '" + category.getName() + "' already exists"
            );
        }

        try {
            category.setCategoryId(0);
            return categoryRepository.save(category);

        } catch (Exception e) {

            throw new DataAccessException("Failed to save category", e);

        }

    }

    public Category update(int categoryId, Category category)
    {
        // update category and return the updated category
        if (category.getName() == null || category.getName().isBlank()) {

            throw new InvalidInputException("Category name cannot be empty");

        }

        Category existing = getById(categoryId);

        if (!existing.getName().equalsIgnoreCase(category.getName()) &&
                categoryRepository.existsByNameIgnoreCase(category.getName())) {

            throw new DuplicateResourceException(
                    "Category with name '" + category.getName() + "' already exists"
            );

        }

        existing.setName(category.getName());
        existing.setDescription(category.getDescription());

        try {

            return categoryRepository.save(existing);

        } catch (Exception e) {

            throw new DataAccessException("Failed to update category", e);

        }

    }

    public void delete(int categoryId)
    {
        // delete category
        getById(categoryId);  // Throws ResourceNotFound exception if not found
        categoryRepository.deleteById(categoryId);

    }
    public List<Product> getProductsByCategory(int categoryId) {
        if (categoryId <= 0) {
            throw new InvalidInputException("Category ID must be a positive number");
        }

        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException(
                    "Category not found with id: " + categoryId
            );
        }

        return productService.getProductsByCategory(categoryId);
    }
}
