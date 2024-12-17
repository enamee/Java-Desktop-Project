package com.enam.supershop.onlineStore.homepage;

import com.enam.supershop.utils.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewController {
    @FXML private VBox categoryRoot;
    @FXML private VBox sidebar;
    @FXML private ToggleButton sidebarBtn;
    @FXML private HBox categoryContainer;

    public void initialize() {
        sidebar.setVisible(false);
        sidebarBtn.setOnAction(event -> {
            if (sidebarBtn.isSelected()) {
                sidebar.setVisible(true);
            } else {
                sidebar.setVisible(false);
            }
        });
        categoryRoot.requestFocus();

        Connection conn = DBConnection.databaseConn;

        try {
            String query = "SELECT * FROM categories";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String imageURL = resultSet.getString("imageURL");
                String fontFamily = resultSet.getString("fontFamily");
                String unicode = resultSet.getString("unicode");
                FXMLLoader categoryWithImageTemplate = new FXMLLoader(getClass().getResource("/com/enam/supershop/onlineStore/homepage/categoryWithImageTemplate.fxml"));
                VBox categoryWithImage = null;
                FXMLLoader categoryTemplate = new FXMLLoader(getClass().getResource("/com/enam/supershop/onlineStore/homepage/categoryTemplate.fxml"));
                HBox category = null;
                try {
                    categoryWithImage = categoryWithImageTemplate.load();
                    CategoryWithImageTemplate categoryWithImageController = categoryWithImageTemplate.getController();
                    categoryWithImageController.setContent(imageURL, name);
                    // categoryWithImage.setUserData();
                    categoryContainer.getChildren().add(categoryWithImage);

                    category = categoryTemplate.load();
                    CategoryTemplate categoryTemplateController = categoryTemplate.getController();
                    categoryTemplateController.setContent(fontFamily, unicode, name);
                    sidebar.getChildren().add(category);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void fetchData() {

    }
}
