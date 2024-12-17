package com.enam.supershop.onlineStore.categoryPage;

import com.enam.supershop.utils.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewController {
    @FXML VBox categoryRoot;
    @FXML FlowPane productContainer;
    @FXML Button goToCartBtn;

    public static String curCategory;

    public void initialize() {
        categoryRoot.requestFocus();
        Connection conn = DBConnection.databaseConn;
        try {
            String query = "SELECT * FROM products WHERE category = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, curCategory);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String quantity = resultSet.getString("quantity");
                float price = resultSet.getFloat("price");
                float rating = resultSet.getFloat("rating");
                int ratingCount = resultSet.getInt("ratingCount");
                String category = resultSet.getString("category");
                String imageURL = resultSet.getString("imageURL");
                float discount = resultSet.getFloat("discount");

                FXMLLoader productCardFxml = new FXMLLoader(getClass().getResource("/com/enam/supershop/onlineStore/categoryPage/productCard.fxml"));
                VBox productCard = null;
                try {
                    productCard = productCardFxml.load();
                    ProductCard productCard1 = productCardFxml.getController();
                    productCard1.setContent(imageURL, id, name, quantity, price, rating, ratingCount, discount);
                    productContainer.getChildren().add(productCard);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToCart() throws IOException {
        Stage stage = (Stage)goToCartBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/enam/supershop/onlineStore/cart/view.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
