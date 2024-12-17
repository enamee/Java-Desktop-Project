package com.enam.supershop.utils;

public class ProductClass {
    private int id;
    private String name;
    private double price;
    private int count;
    private double discount;
    private String imageUrl;
    private String category;
    private String manufacturer;

    public ProductClass(int id, String name, double price, int count, double discount, String manufacturer, String imageUrl, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.discount = discount;
        this.manufacturer = manufacturer;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getCount() { return count; }
    public double getDiscount() { return discount; }
    public String getManufacturer() { return manufacturer; }
    public String getImageUrl() { return imageUrl; }
    public String getCategory() { return category; }
}
