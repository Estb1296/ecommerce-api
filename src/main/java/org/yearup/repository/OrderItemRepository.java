package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yearup.models.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {
}
