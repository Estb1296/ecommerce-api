package org.yearup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ShoppingCartItem
{
    private int productId;
    private String productName;
    private double price;
    private int quantity = 1;
    private double discountPercent = 0;

    public ShoppingCartItem() {}

    public ShoppingCartItem(int productId, String productName, double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters & Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(double discountPercent) { this.discountPercent = discountPercent; }

    public double getLineTotal() {
        double subTotal = this.price * this.quantity;
        double discountAmount = subTotal * discountPercent;
        return subTotal - discountAmount;
    }
}
