package org.yearup.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yearup.exception.DataAccessException;
import org.yearup.exception.InvalidInputException;
import org.yearup.models.*;
import org.yearup.repository.OrderItemRepository;
import org.yearup.repository.OrderRepository;

import java.time.LocalDate;


@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartService shoppingCartService;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                        ShoppingCartService shoppingCartService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.shoppingCartService = shoppingCartService;
        this.productService = productService;
    }
        @Transactional
        public Order checkout( int userId, String address, String city, String state, Long zipCode,
        double shippingAmount)
        {

            // 1. Validate input
            if (userId <= 0) {
                throw new InvalidInputException("User ID must be a positive number");
            }
            if (address == null || address.isBlank()) {
                throw new InvalidInputException("Address is required");
            }
            if (city == null || city.isBlank()) {
                throw new InvalidInputException("City is required");
            }
            if (state == null || state.isBlank()) {
                throw new InvalidInputException("State is required");
            }
            if (zipCode == null || zipCode <= 0) {
                throw new InvalidInputException("Zip code must be a positive number");
            }
            if (shippingAmount < 0) {
                throw new InvalidInputException("Shipping amount cannot be negative");
            }

            try {
                // 2. Get the user's shopping cart
                ShoppingCart cart = shoppingCartService.getByUserId(userId);

                if (cart.getItems().isEmpty()) {
                    throw new InvalidInputException("Cannot checkout with an empty cart");
                }

                // 3. Create the Order entity
                Order order = new Order();
                order.setUserId(userId);
                order.setDate(LocalDate.now());
                order.setAddress(address);
                order.setCity(city);
                order.setState(state);
                order.setZipCode(zipCode);
                order.setShippingAmount(shippingAmount);

                // 4. Save the Order (auto-generates orderId)
                Order savedOrder = orderRepository.save(order);

                // 5. Create OrderItems from each ShoppingCartItem
                for (ShoppingCartItem cartItem : cart.getItems().values()) {
                    Product product = cartItem.getProduct();

                    OrderItem orderItem = new OrderItem(
                            savedOrder.getOrderId(),
                            product.getProductId(),
                            cartItem.getQuantity(),
                            product.getPrice(),
                            cartItem.getDiscountPercent()
                    );

                    orderItemRepository.save(orderItem);
                }

                // 6. Clear the shopping cart
                shoppingCartService.clearCart(userId);

                // 7. Return the created Order
                return savedOrder;

            } catch (InvalidInputException e) {
                throw e;
            } catch (Exception e) {
                throw new DataAccessException("Failed to process checkout", e);
            }
        }

}
