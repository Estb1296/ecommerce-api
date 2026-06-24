package org.yearup.service;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.yearup.exception.DataAccessException;
import org.yearup.exception.InvalidInputException;
import org.yearup.exception.ResourceNotFoundException;
import org.yearup.models.*;
import org.yearup.repository.OrderRepository;
import org.yearup.repository.OrderItemRepository;
import org.yearup.repository.ProfileRepository;

import java.time.LocalDate;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartService shoppingCartService;
    private final ProfileRepository profileRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        ShoppingCartService shoppingCartService, ProfileRepository profileRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.shoppingCartService = shoppingCartService;
        this.profileRepository = profileRepository;
    }

    @Transactional
    public Order checkout(int userId) {

        if (userId <= 0) {
            throw new InvalidInputException("User ID must be a positive number");
        }

        try {
            // 1. Get the user's shopping cart
            ShoppingCart cart = shoppingCartService.getByUserId(userId);

            if (cart.getItems().isEmpty()) {
                throw new InvalidInputException("Cannot checkout with an empty cart");
            }
            Profile profile = profileRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user: " + userId));

            // 2. Create the Order entity
            Order order = new Order(userId, LocalDate.now());
            order.setAddress(profile.getAddress());
            order.setCity(profile.getCity());
            order.setState(profile.getState());
            order.setZip(profile.getZip());

            // 3. Save the Order (auto-generates orderId)
            Order savedOrder = orderRepository.save(order);

            // 4. Create OrderItems from each ShoppingCartItem
            for (ShoppingCartItem cartItem : cart.getItems().values()) {
                OrderItem orderItem = new OrderItem(savedOrder.getOrderId(), cartItem.getProductId(),
                        cartItem.getQuantity());

                orderItemRepository.save(orderItem);
            }

            // 5. Clear the shopping cart
            shoppingCartService.clearCart(userId);

            // 6. Return the created Order
            return savedOrder;

        } catch (InvalidInputException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException("Failed to process checkout", e);
        }
    }
}