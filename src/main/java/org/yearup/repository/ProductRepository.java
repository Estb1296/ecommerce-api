package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.yearup.DTO.ProductSearchDto;
import org.yearup.DTO.ProductDetailDto;
import org.yearup.models.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Keep the old one (for backward compatibility while migrating)
    List<Product> findByCategoryId(int categoryId);

    // ✅ NEW: Search with DTOs (database-level filtering)
    @Query("SELECT new org.yearup.DTO.ProductSearchDto(" +
            "p.productId, p.name, p.price, p.subCategory, p.imageUrl) " +
            "FROM Product p WHERE " +
            "(:categoryId IS NULL OR p.categoryId = :categoryId) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:subCategory IS NULL OR LOWER(p.subCategory) = LOWER(:subCategory))")
    List<ProductSearchDto> searchWithDto(
            @Param("categoryId") Integer categoryId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("subCategory") String subCategory
    );

    // ✅ NEW: Get full product detail as DTO (only when needed)
    @Query("SELECT new org.yearup.DTO.ProductDetailDto(" +
            "p.productId, p.name, p.price, p.categoryId, p.description, " +
            "p.subCategory, p.isFeatured, p.imageUrl, p.stock) " +
            "FROM Product p WHERE p.productId = :productId")
    Optional<ProductDetailDto> findDetailById(@Param("productId") int productId);
}