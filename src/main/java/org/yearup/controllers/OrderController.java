package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.yearup.DTO.CheckoutRequest;
import org.yearup.models.Order;
import org.yearup.service.OrderService;

@RestController
@RequestMapping("order")
@CrossOrigin(origins = "*")
public class OrderController {
private OrderService orderService;
@PostMapping("/checkout")
@PreAuthorize("isAuthenticated()")
public ResponseEntity<Order> checkout(
        @AuthenticationPrincipal(expression = "name") String username,
        @RequestBody CheckoutRequest request) {

    int userId = Integer.parseInt(username);

    Order createdOrder = orderService.checkout(
            userId,
            request.getAddress(),
            request.getCity(),
            request.getState(),
            request.getZipCode(),
            request.getShippingAmount()
    );

    return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
}
}
