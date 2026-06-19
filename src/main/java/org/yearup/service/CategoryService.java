package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Category;
import org.yearup.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService
{
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository)
    {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories()
    {

        return categoryRepository.findAll();
        // get all categories
    }

    public Optional<Category> getById(int categoryId)
    {
        // get category by id
//        if (categoryId <= 0) {
//            throw new InvalidInputException("Category ID must be a positive number"); input validation for later
//        }
        Optional<Category> category = categoryRepository.findById(categoryId);
//         Instead of optional I'll be returning an object later
//        return categoryRepository.findById(categoryId)
//                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        return category;
    }

    public Category create(Category category)
    {
        // create a new category
        return null;
    }

    public Category update(int categoryId, Category category)
    {
        // update category and return the updated category
        return null;
    }

    public void delete(int categoryId)
    {
        // delete category
    }
}
