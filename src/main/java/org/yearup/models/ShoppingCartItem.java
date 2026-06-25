package org.yearup.models;

public class ShoppingCartItem
{
    private Product product;
    private int quantity = 1;
    private double discountPercent = 0;

    // Getters & Setters

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(double discountPercent) { this.discountPercent = discountPercent; }

    public double getLineTotal() {
        double subTotal = this.product.getPrice() * this.quantity;
        double discountAmount = subTotal * discountPercent;
        return subTotal - discountAmount;
    }
}
