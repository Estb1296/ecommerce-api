package org.yearup.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.yearup.DTO.ProductDetailDto;
import org.yearup.DTO.ProductSearchDto;
import org.yearup.models.Product;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(scripts = "classpath:test-insert-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)  // ✅ BEFORE_TEST_CLASS
@Transactional
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void getById_shouldReturn_theCorrectProduct() {
        // arrange
        int productId = 1;

        // act
        Product actual = productRepository.findById(productId).orElse(null);

        // assert
        assertNotNull(actual, "Because product 1 should exist in the test database.");
        assertEquals(499.99, actual.getPrice(), 0.001, "Because I tried to get product 1 from the database.");
    }

    // ✅ NEW TESTS FOR DTOs

    @Test
    public void searchWithDto_noCriteria_shouldReturn_allProducts() {
        // arrange
        Integer categoryId = null;
        Double minPrice = null;
        Double maxPrice = null;
        String subCategory = null;

        // act
        List<ProductSearchDto> results = productRepository.searchWithDto(categoryId, minPrice, maxPrice, subCategory);

        // assert
        assertNotNull(results, "Because search should not return null.");
        assertFalse(results.isEmpty(), "Because there should be products in the test database.");
        assertEquals(12, results.size(), "Because test data should have 12 products.");
    }

    @Test
    public void searchWithDto_filterByCategory_shouldReturn_onlyProductsInCategory() {
        Integer categoryId = 1;
        Double minPrice = null;
        Double maxPrice = null;
        String subCategory = null;

        List<ProductSearchDto> results = productRepository.searchWithDto(categoryId, minPrice, maxPrice, subCategory);
        System.out.println("Category 1 Results: " + results.size());
        for (ProductSearchDto p : results) {
            System.out.println("  - ID: " + p.getProductId() + ", Name: " + p.getName());
        }

        assertNotNull(results);
        assertEquals(3, results.size(), "Because category 1 should have 3 products (Smartphone, Laptop, Headphones)");
        assertTrue(results.stream().allMatch(p -> p.getProductId() >= 1 && p.getProductId() <= 3),
                "Because all results should have productId 1, 2, or 3 from category 1.");
    }

    @Test
    public void searchWithDto_filterByPrice_shouldReturn_productsInPriceRange() {
        // arrange
        Integer categoryId = null;
        Double minPrice = 100.0;
        Double maxPrice = 500.0;
        String subCategory = null;

        // act
        List<ProductSearchDto> results = productRepository.searchWithDto(categoryId, minPrice, maxPrice, subCategory);

        // assert
        assertNotNull(results, "Because search should not return null.");
        assertTrue(results.stream().allMatch(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice),
                "Because all prices should be between 100 and 500.");
    }

    @Test
    public void searchWithDto_filterBySubCategory_shouldReturn_onlyMatchingSubCategory() {
        // arrange
        Integer categoryId = null;
        Double minPrice = null;
        Double maxPrice = null;
        String subCategory = "red";

        // act
        List<ProductSearchDto> results = productRepository.searchWithDto(categoryId, minPrice, maxPrice, subCategory);

        // assert
        assertNotNull(results, "Because search should not return null.");
        assertTrue(results.stream().allMatch(p -> p.getSubCategory().equalsIgnoreCase("red")),
                "Because all results should have subcategory 'red'.");
    }

    @Test
    public void searchWithDto_multipleCriteria_shouldReturn_filteredResults() {
        // arrange
        Integer categoryId = 1;
        Double minPrice = 100.0;
        Double maxPrice = 600.0;
        String subCategory = null;

        // act
        List<ProductSearchDto> results = productRepository.searchWithDto(categoryId, minPrice, maxPrice, subCategory);

        // assert
        assertNotNull(results, "Because search should not return null.");
        assertTrue(results.stream().allMatch(p ->
                        p.getPrice() >= minPrice && p.getPrice() <= maxPrice),
                "Because all prices should be in range.");
    }

    @Test
    public void searchWithDto_shouldReturn_dtoWithCorrectFields() {
        // arrange
        Integer categoryId = 1;
        Double minPrice = null;
        Double maxPrice = null;
        String subCategory = null;

        // act
        List<ProductSearchDto> results = productRepository.searchWithDto(categoryId, minPrice, maxPrice, subCategory);

        // assert
        assertNotNull(results, "Because search should not return null.");
        assertFalse(results.isEmpty(), "Because category 1 should have products.");

        ProductSearchDto first = results.get(0);
        assertNotNull(first.getProductId(), "Because productId should be set.");
        assertNotNull(first.getName(), "Because name should be set.");
        assertNotNull(first.getPrice(), "Because price should be set.");
        // imageUrl and subCategory can be null
    }

    @Test
    public void findDetailById_shouldReturn_fullProductDetail() {
        // arrange
        int productId = 1;

        // act
        Optional<ProductDetailDto> result = productRepository.findDetailById(productId);

        // assert
        assertTrue(result.isPresent(), "Because product 1 should exist.");
        ProductDetailDto detail = result.get();
        assertEquals(1, detail.getProductId(), "Because productId should match.");
        assertEquals(499.99, detail.getPrice(), 0.001, "Because price should match.");
        assertNotNull(detail.getName(), "Because name should be set.");
        assertNotNull(detail.getDescription(), "Because description should be set.");
    }

    @Test
    public void findDetailById_nonExistent_shouldReturn_empty() {
        // arrange
        int productId = 999;

        // act
        Optional<ProductDetailDto> result = productRepository.findDetailById(productId);

        // assert
        assertTrue(result.isEmpty(), "Because product 999 should not exist.");
    }
}
