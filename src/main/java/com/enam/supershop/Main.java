package com.enam.supershop;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        Font.loadFont(getClass().getResourceAsStream("/fonts/OpenSans-Regular.ttf"), 14);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/enam/supershop/init/signIn.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/enam/supershop/onlineStore/categoryPage/view.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/enam/supershop/inventory/homepage.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
//        stage.setTitle("Sign In / Sign Up");
//        stage.setHeight(600);
//        stage.setWidth(900);
//        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) { launch(); }
}