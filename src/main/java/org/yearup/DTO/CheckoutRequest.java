package org.yearup.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckoutRequest {
    private String address;
    private String city;
    private String state;
    @JsonProperty("zip")
    private String zipCode;
    private double shippingAmount;

    // Getters & Setters
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public double getShippingAmount() { return shippingAmount; }
    public void setShippingAmount(double shippingAmount) { this.shippingAmount = shippingAmount; }
}