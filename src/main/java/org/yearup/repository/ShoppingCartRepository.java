package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yearup.models.CartItem;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<CartItem, Integer>
{
    List<CartItem> findByUserId(int userId);

    CartItem findByUserIdAndProductId(int userId, int productId);

    void deleteByUserId(int userId);

    void deleteByUserIdAndProductId(int userId, int productId);
    @Modifying
    @Transactional
    @Query("UPDATE CartItem c SET c.quantity = :quantity WHERE c.userId = :userId AND c.productId = :productId")
    void updateQuantity(@Param("userId") int userId, @Param("productId") int productId, @Param("quantity") int quantity);
}
