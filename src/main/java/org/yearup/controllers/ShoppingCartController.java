package org.yearup.controllers;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.exception.InvalidInputException;
import org.yearup.exception.ResourceNotFoundException;
import org.yearup.models.CartItem;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;
import org.yearup.service.ShoppingCartService;
import org.yearup.service.UserService;

import java.security.Principal;
import java.util.List;

// convert this class to a REST controller
// only logged-in users should have access to these actions
@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "*")
public class ShoppingCartController
{
    // a shopping cart controller depends on the service layer
    private ShoppingCartService shoppingCartService;
    private UserService userService;

    public ShoppingCartController(ShoppingCartService shoppingCartService,UserService userService){
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    // each method in this controller requires a Principal object as a parameter
    @GetMapping
    public ResponseEntity<ShoppingCart> getCart(Principal principal)
    {
        // get the currently logged-in username
        int userId = getUserIdFromPrincipal(principal);

        // use the shoppingCartService to get all items in the cart and return the cart
        ShoppingCart cart = shoppingCartService.getByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15  (15 is the productId to be added)
    // return the updated cart with status 201 Created

    @PostMapping("/products/{productId}")
    public ResponseEntity<ShoppingCart> addToCart(Principal principal,
                                                  @PathVariable int productId,
                                                  @RequestBody ShoppingCartItem item) {
        // Get userId from principal
        int userId = getUserIdFromPrincipal(principal);

        // Pass all three parameters to service
        ShoppingCart cart = shoppingCartService.addToCart(userId, productId, item);
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }
    private int getUserIdFromPrincipal(Principal principal) {
        if (principal==null){
            throw new InvalidInputException("Authentication required");
        }
        User user = userService.getByUserName(principal.getName());
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        return user.getId();
    }

    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15  (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated; return the cart (200 OK)
    @PutMapping("/products/{productId}")
    public ResponseEntity<ShoppingCart> updateCart(Principal principal,
                                                   @PathVariable int productId,
                                                   @RequestBody ShoppingCartItem item) {

        int userId = getUserIdFromPrincipal(principal);

        // Pass the item quantity from the RequestBody down to the service
        ShoppingCart updated = shoppingCartService.updateItemQuantity(userId, productId, item.getQuantity());
        return ResponseEntity.ok(updated);
    }

    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart  - return the (now empty) cart so the front end can refresh it (200 OK)

    @DeleteMapping
    public ResponseEntity<ShoppingCart> clearCart(Principal principal)
    {

        int userId = getUserIdFromPrincipal(principal);

        shoppingCartService.clearCart(userId);
        ShoppingCart emptyCart = shoppingCartService.getByUserId(userId);
        return ResponseEntity.ok(emptyCart);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<ShoppingCart> deleteProductFromCart(@PathVariable int productId, Principal principal)
    {
        // 1. Unified User Parsing

        int userId = getUserIdFromPrincipal(principal);

        // 2. Clear clean service execution
        shoppingCartService.deleteProductFromCart(userId, productId);

        // 3. Return updated cart container back to frontend
        ShoppingCart updatedCart = shoppingCartService.getByUserId(userId);
        return ResponseEntity.ok(updatedCart);
    }

}
