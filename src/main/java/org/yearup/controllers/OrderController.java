package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.yearup.exception.InvalidInputException;
import org.yearup.exception.ResourceNotFoundException;
import org.yearup.models.Order;
import org.yearup.models.User;
import org.yearup.service.OrderService;
import org.yearup.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")
public class OrderController {
private final OrderService orderService;
private final UserService userService;

public OrderController(OrderService orderService,UserService userService) {
    this.orderService = orderService;
    this.userService = userService;
}
@PostMapping
@PreAuthorize("isAuthenticated()")
public ResponseEntity<Order> checkout(Principal principal) {

    int userId = getUserIdFromPrincipal(principal);
    Order createdOrder = orderService.checkout(userId);

    return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
}
private int getUserIdFromPrincipal(Principal principal) {
    if (principal==null){
        throw new InvalidInputException("Authentication required");
    }
    User user = userService.getByUserName(principal.getName());
    if (user==null) {
        throw new ResourceNotFoundException("User not found");
    }
    return user.getId();
}
}

