package org.yearup.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "orders")
public class Order {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "order_id")
private int orderId;
@Column(name = "user_id")
private int userId;
@Column(name = "date")
private LocalDateTime date;
@Column(name = "address")
private String address;
@Column(name = "city")
private String city;
@Column(name = "state")
private String state;
@Column(name = "zip")
private String zip;
@Column(name = "shipping_amount")
private double shippingAmount;

    public Order() {
    }

    public Order(int orderId, int userId, LocalDateTime date, String address, String city,
                 String state, String zip, double shippingAmount) {
        this.orderId = orderId;
        this.userId = userId;
        this.date = date;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.shippingAmount = shippingAmount;
    }

    public Order(int userId, LocalDateTime date) {
        this.userId = userId;
        this.date=date;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public double getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(double shippingAmount) {
        this.shippingAmount = shippingAmount;
    }
}
