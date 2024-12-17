package com.enam.supershop;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class Sample {

    @FXML
    private FlowPane flowPane;

    // Define the minimum and maximum width for the VBoxes
    private static final double MIN_BOX_WIDTH = 100;
    private static final double MAX_BOX_WIDTH = 200;

    public void initialize() {
        // Add 10 VBox elements for demonstration
        for (int i = 0; i < 10; i++) {
            VBox vbox = createVBox("Box " + (i + 1));
            flowPane.getChildren().add(vbox);
        }

        // Bind the width of the VBoxes dynamically based on FlowPane's width
        flowPane.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            adjustVBoxWidths(newWidth.doubleValue());
        });
    }

    private VBox createVBox(String title) {
        VBox vbox = new VBox();
        vbox.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: #ffffff;");
        vbox.setPrefHeight(100); // Set a fixed height for simplicity

        Label label = new Label(title);
        vbox.getChildren().add(label);

        return vbox;
    }

    private void adjustVBoxWidths(double flowPaneWidth) {
        int numBoxesPerRow = (int) (flowPaneWidth / MAX_BOX_WIDTH);
        numBoxesPerRow = Math.max(numBoxesPerRow, 1); // At least one box per row

        double targetWidth = flowPaneWidth / numBoxesPerRow;
        double boxWidth = Math.min(MAX_BOX_WIDTH, Math.max(MIN_BOX_WIDTH, targetWidth));

        for (var child : flowPane.getChildren()) {
            if (child instanceof VBox vbox) {
                vbox.setMinWidth(boxWidth);
                vbox.setMaxWidth(boxWidth);
                vbox.setPrefWidth(boxWidth);
            }
        }
    }
}
