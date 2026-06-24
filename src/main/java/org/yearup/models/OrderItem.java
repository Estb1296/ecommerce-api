package org.yearup.models;

import jakarta.persistence.*;
@Entity
@Table(name = "order_line_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_line_item_id")
    private int orderLineItemId;

    @Column(name = "order_id")
    private int orderId;

    @Column(name = "product_id")
    private int productId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "sales_price")
    private double salesPrice;  // Price at time of purchase

    @Column(name = "discount")
    private double discount = 0;

    public OrderItem() {}

    public OrderItem(int orderId, int productId, int quantity, double salesPrice, double discount) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.salesPrice = salesPrice;
        this.discount = discount;
    }

    public OrderItem(int orderId, int productId, int quantity) {
        this.orderId=orderId;
        this.productId = productId;
        this.quantity=quantity;
    }

    // Getters & Setters
    public int getOrderLineItemId() { return orderLineItemId; }
    public void setOrderLineItemId(int orderLineItemId) { this.orderLineItemId = orderLineItemId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getSalesPrice() {
        return salesPrice;
    }
    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public double getDiscountPercent() { return discount; }
    public void setDiscountPercent(double discountPercent) { this.discount = discountPercent; }
}
