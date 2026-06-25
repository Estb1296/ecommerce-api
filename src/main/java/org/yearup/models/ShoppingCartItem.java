package org.yearup.models;

public class ShoppingCartItem
{
    private int productId;
    private String productName;
    private double price;
    private int quantity = 1;
    private double discount = 0;

    public ShoppingCartItem(int productId, String productName, double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public double getLineTotal() {
        double subTotal = this.price * this.quantity;
        double discountAmount = subTotal * discount;
        return subTotal - discountAmount;
    }
}
