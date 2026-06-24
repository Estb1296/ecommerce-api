package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Order;
import org.yearup.service.OrderService;

@RestController
@RequestMapping("order")
@CrossOrigin(origins = "*")
public class OrderController {
private OrderService orderService;


}
