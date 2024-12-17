package com.enam.supershop.onlineStore.homepage;

import com.enam.supershop.utils.FontManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class CategoryTemplate {
    @FXML private HBox categoryItem;
    @FXML private Label categoryIcon;
    @FXML private Label categoryName;

    public void setContent(String fontFamily, String unicode, String labelText) {
        if (unicode != null && fontFamily != null) {
            categoryIcon.setFont(FontManager.getFont(fontFamily, 20));
            categoryIcon.setText(unicode);
        }
        categoryName.setText(labelText);
    }
}
