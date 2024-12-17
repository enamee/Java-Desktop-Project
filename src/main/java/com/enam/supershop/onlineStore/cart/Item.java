package com.enam.supershop.onlineStore.cart;

import com.enam.supershop.currentUser.Info;
import com.enam.supershop.utils.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

import static com.enam.supershop.utils.UserDefMethods.*;

public class Item {
    @FXML private Button remove;
    @FXML private Button increase;
    @FXML private Button decrease;
    @FXML private ImageView image;
    @FXML private Label name;
    @FXML private Label price;
    @FXML private Label unit;
    @FXML private Label discountedPrice;
    @FXML private Label quantity;
    @FXML private Label subtotal;

    private float itemPrice;
    private float itemDiscount = 0;
    private int itemQuantity;

    public void initialize() {
        makeLabelNonExistent(discountedPrice);

        // Add event handlers for buttons
        increase.setOnAction(event -> increaseQuantity());
        decrease.setOnAction(event -> decreaseQuantity());
        remove.setOnAction(event -> removeItem());
    }

    public void setInfo(String imageURL, String name, float price, String unit, float discount, int quantity) {
        image.setImage(new Image(imageURL));
        this.name.setText(name);
        this.price.setText(String.valueOf(price));
        this.unit.setText(unit);
        this.itemPrice = price;
        this.itemDiscount = discount;
        this.itemQuantity = quantity;
        this.quantity.setText(String.valueOf(quantity));

        if (discount == 0) {
            subtotal.setText(String.valueOf(price * quantity));
        } else {
            this.price.setStyle("-fx-strikethrough: true");
            float disPrice = price - price * discount / 100;
            makeLabelExistent(discountedPrice, String.valueOf(disPrice));
            subtotal.setText(String.valueOf(disPrice * quantity));
        }
    }

    private void updateSubtotal(boolean addRem) {
        float effectivePrice = itemDiscount == 0 ? itemPrice : itemPrice - itemPrice * itemDiscount / 100;
        if (addRem) {
            subtotal.setText(String.format("%.2f", Float.parseFloat(subtotal.getText()) + effectivePrice));
            Info.totalPrice += effectivePrice;
        } else {
            subtotal.setText(String.format("%.2f", Float.parseFloat(subtotal.getText()) - effectivePrice));
            Info.totalPrice -= effectivePrice;
        }
        Stage stage = (Stage) remove.getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/com/enam/supershop/onlineStore/cart/view.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    private void increaseQuantity() {
        itemQuantity++;
        // Iterate through the cartList to find the corresponding product
//        for (Product product : Info.cartList) {
//            if (product.productName.equals(name)) { // Assuming you have a way to identify the product
//                product.setQuantity(product.getQuantity() + 1); // Increase the product quantity
//                break; // Exit the loop once the product is found and updated
//            }
//        }
        quantity.setText(String.valueOf(itemQuantity));
        updateSubtotal(true);
    }

    private void decreaseQuantity() {
        if (itemQuantity > 1) {
            itemQuantity--;
//            for (Product product : Info.cartList) {
//                if (product.productName.equals(name)) { // Assuming you have a way to identify the product
//                    product.setQuantity(product.getQuantity() - 1); // Increase the product quantity
//                    break; // Exit the loop once the product is found and updated
//                }
//            }
            quantity.setText(String.valueOf(itemQuantity));
            updateSubtotal(false);
        }
    }

    private void removeItem() {
        // Logic to remove this item from the cart, typically by notifying the parent container
        System.out.println("Item removed: " + name.getText());
        // Example: You can call a method in the parent controller to handle this
        // ParentController.removeItem(this);
    }
}
