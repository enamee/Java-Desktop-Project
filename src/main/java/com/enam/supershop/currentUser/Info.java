package com.enam.supershop.currentUser;

import com.enam.supershop.utils.Product;

import java.util.ArrayList;
import java.util.List;

public class Info {
    public static String username;
    public static int userID;
    protected static String email;
    public static List<Product> cartList = new ArrayList<>();
    public static float totalPrice = 0;
    public static void setInfo(String u, int uid, String e) {
        username = u;
        email = e;
        userID = uid;
    }

    // Add product to cart or increase quantity if already present
    public static void addToCartList(Product product) {
        for (Product p : cartList) {
            if (p.getProductID() == product.getProductID()) {
                // If product already exists in cart, increase quantity
                p.setQuantity(p.getQuantity() + 1);
                return;
            }
        }
        // If product not in cart, add it with quantity 1
        product.setQuantity(1);
        cartList.add(product);
    }

    // Remove product from cart by product ID
    public static void removeFromCartList(int productID) {
        cartList.removeIf(product -> product.getProductID() == productID);
    }

    // Get all products in the cart
    public static List<Product> getCartList() {
        return cartList;
    }

    // Clear cart after purchase
    public static void clearCartList() {
        cartList.clear();
    }
}