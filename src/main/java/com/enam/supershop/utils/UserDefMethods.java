package com.enam.supershop.utils;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class UserDefMethods {
    public static void checkFieldStatus(TextField textField, Label status, String fieldType) {
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if(newText.trim().isEmpty()) {
                status.setText(fieldType + " cannot be empty!");
                status.setManaged(true);
                status.setVisible(true);
            } else {
                status.setVisible(false);
                status.setManaged(false);
            }
        });
    }

    public static void hideLabel(Label label) {
        label.setVisible(false);
        label.setManaged(false);
    }

    public static void showLabel(Label label) {
        label.setVisible(true);
        label.setManaged(true);
    }

    public static void makeLabelNonExistent(Label label) {
        label.setText("");
        label.setVisible(false);
        label.setManaged(false);
    }

    public static void makeLabelExistent(Label label, String s) {
        label.setText(s);
        label.setManaged(true);
        label.setVisible(true);
    }

}
