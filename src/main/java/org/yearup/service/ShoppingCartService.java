package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.exception.DataAccessException;

import org.yearup.exception.InvalidInputException;
import org.yearup.exception.ResourceNotFoundException;
import org.yearup.models.CartItem;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.ShoppingCartRepository;


import java.util.List;

@Service
public class ShoppingCartService
{
    // a shopping cart is built from cart rows plus a product lookup for each row
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService)
    {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }

    public ShoppingCart getByUserId(int userId)
    {
        // load the user's cart rows, look up each product, and build the ShoppingCart
        if (userId <= 0) {
            throw new InvalidInputException("User ID must be a positive number");
        }
        List<CartItem> cartItems = shoppingCartRepository.findByUserId(userId);
        ShoppingCart shoppingCart = new ShoppingCart();
        for (CartItem cartItem : cartItems) {
            Product product = productService.getById(cartItem.getProductId());

            if (product != null) {
                ShoppingCartItem item = new ShoppingCartItem();
                item.setProduct(product);
                item.setQuantity(cartItem.getQuantity());

                shoppingCart.add(item);  // Adds to the Map<productId, ShoppingCartItem>
            }
        }

        return shoppingCart;
    }

    // add additional methods here
    public CartItem getProductFromCart(int userId,int productId){
        if (userId <= 0) {
            throw new InvalidInputException("User ID must be a positive number");
        }
        if(productId<=0){
            throw new InvalidInputException("Product ID must be a positive number");
        }
        CartItem foundItem = shoppingCartRepository.findByUserIdAndProductId(userId,productId);
        if (foundItem == null) {
            throw new ResourceNotFoundException(
                    "Product " + productId + " not found in cart for user " + userId
            );
        }

        return foundItem;

    }
    public void clearCart(int userId) {
        if (userId <= 0) {
            throw new InvalidInputException("User ID must be a positive number");
        }

        // Verify cart exists
        getByUserId(userId);

        // Delete entire cart
        shoppingCartRepository.deleteByUserId(userId);
    }

    public void deleteProductFromCart(int userId, int productId) {
        if (userId <= 0) {
            throw new InvalidInputException("User ID must be a positive number");
        }
        if (productId <= 0) {
            throw new InvalidInputException("Product ID must be a positive number");
        }

        // Verify product exists in cart
        getProductFromCart(userId, productId);

        // Delete just this product
        shoppingCartRepository.deleteByUserIdAndProductId(userId, productId);
    }

    public ShoppingCart updateItemQuantity(int userId, int productId, int newQuantity) {
        if (userId <= 0 || productId <= 0) {
            throw new InvalidInputException("IDs must be positive numbers");
        }
        if (newQuantity <= 0) {
            throw new InvalidInputException("Quantity must be greater than 0");
        }

        try {
            // Update the quantity for just this specific row in the database
            shoppingCartRepository.updateQuantity(userId, productId, newQuantity);

            //  Return the freshly updated cart structure
            return getByUserId(userId);
        } catch (Exception e) {
            throw new DataAccessException("Failed to update item quantity", e);
        }
    }

public ShoppingCart create(int userId, ShoppingCart shoppingCart) {
    if (userId <= 0) {
        throw new InvalidInputException("User ID must be a positive number");
    }

    if (shoppingCart == null || shoppingCart.getItems().isEmpty()) {
        throw new InvalidInputException("Shopping cart cannot be empty");
    }

    try {
        // Convert ShoppingCartItems to CartItems and save
        for (ShoppingCartItem item : shoppingCart.getItems().values()) {
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(item.getProductId());
            cartItem.setQuantity(item.getQuantity());

            shoppingCartRepository.save(cartItem);
        }

        // Return the cart (now it's persisted)
        return getByUserId(userId);

    } catch (Exception e) {
        throw new DataAccessException("Failed to save shopping cart", e);
    }
}
    // Add a single item to cart (instead of replacing whole cart)
    public ShoppingCart addToCart(int userId, ShoppingCartItem item) {
        if (userId <= 0) {
            throw new InvalidInputException("User ID must be a positive number");
        }

        if (item == null || item.getProductId() <= 0) {
            throw new InvalidInputException("Invalid item");
        }

        try {
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(item.getProductId());
            cartItem.setQuantity(item.getQuantity());
            shoppingCartRepository.save(cartItem);
            return getByUserId(userId);
        } catch (Exception e) {
            throw new DataAccessException("Failed to add to cart", e);
        }
    }

}

