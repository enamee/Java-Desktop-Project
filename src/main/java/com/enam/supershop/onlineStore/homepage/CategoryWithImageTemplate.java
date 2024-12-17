package com.enam.supershop.onlineStore.homepage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class CategoryWithImageTemplate {
    @FXML private VBox categoryItem;
    @FXML private StackPane categoryImageContainer;
    @FXML private ImageView categoryImage;
    @FXML private Label categoryName;

    public void setContent(String imagePath, String labelText) {
        if (imagePath != null) {
            Image image = new Image(imagePath);
            categoryImage.setImage(image);
        }
        categoryName.setText(labelText);
    }
    public void goToCategoryPage() {
        com.enam.supershop.onlineStore.categoryPage.ViewController.curCategory = categoryName.getText();
        Stage stage = (Stage) categoryName.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/enam/supershop/onlineStore/categoryPage/view.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
