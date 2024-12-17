package com.enam.supershop.onlineStore.categoryPage;

import com.enam.supershop.currentUser.Info;
import com.enam.supershop.utils.Product;
import com.enam.supershop.utils.UserDefMethods;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProductCard {
    @FXML private ImageView image;
    @FXML private Label name;
    @FXML private Label quantity;
    @FXML private Label price;
    @FXML private Label discountedPrice;
    @FXML private Label rating;
    @FXML private Label ratingCount;
    @FXML private Button addToCartBtn;
    @FXML private Label id;
    private float discount = 0;
    private String imageURL;

    public void initialize() {
        UserDefMethods.makeLabelNonExistent(id);
        UserDefMethods.makeLabelNonExistent(discountedPrice);
    }

    public void setContent(String imagePath, int id, String name, String quantity, float price, float rating, int ratingCount, float discount) {
        if (imagePath != null) {
            imageURL = imagePath;
            Image image = new Image(imagePath);
            this.image.setImage(image);
        }
        this.id.setText(String.valueOf(id));
        this.name.setText(name);
        this.quantity.setText(quantity);
        this.price.setText(String.valueOf(price));
        this.rating.setText(String.valueOf(rating));
        this.ratingCount.setText(String.valueOf(ratingCount));
        this.discount = discount;
        float disPrice = price - price*discount;
        discountedPrice.setText(String.valueOf(disPrice));
        if (discount != 0) {
            this.price.setStyle("-fx-strikethrough: true");
            UserDefMethods.makeLabelExistent(discountedPrice, String.valueOf(disPrice));
        }
    }

    public void addProductToCart() {
        Info.addToCartList(new Product(imageURL, name.getText(), Integer.parseInt(id.getText()), 1, quantity.getText(), Float.parseFloat(price.getText()), discount));
        Info.totalPrice += Float.parseFloat(discountedPrice.getText());
    }
}
