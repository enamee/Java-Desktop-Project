package com.enam.supershop.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class AdminController {
    @FXML private BorderPane root;
    @FXML private HBox header;
    @FXML private ImageView logo;
    @FXML private Button btn;

    public void initialize() {
        root.requestFocus();
    }
}
