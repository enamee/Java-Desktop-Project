package com.enam.supershop.utils;

public class Product {
    public String imageURL;
    public String productName;
    public int productID;
    public int quantity;
    public String unit;
    public float price;
    public float discount;

    // Constructor
    public Product(String imageURL, String productName, int productID, int quantity, String unit, float price, float discount) {
        this.imageURL = imageURL;
        this.productName = productName;
        this.productID = productID;
        this.quantity = quantity;
        this.unit = unit;
        this.price = price;
        this.discount = discount;
    }

    // Getters and setters
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName + '\'' +
                ", productID=" + productID +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}

