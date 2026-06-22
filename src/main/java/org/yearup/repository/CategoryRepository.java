package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yearup.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>
{
  //  List<Category>findByCategoryNameContainingIgnoreCase(String categoryName); not in use but might be utilized for future features
    boolean existsByNameIgnoreCase(String name);
}
