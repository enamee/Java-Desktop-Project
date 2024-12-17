package com.enam.supershop.onlineStore.cart;

import com.enam.supershop.currentUser.Info;
import com.enam.supershop.onlineStore.SSLCommerz;
import com.enam.supershop.utils.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewController {
    @FXML VBox itemContainer;
    @FXML  Label subtotal;
    @FXML Button payBtn;

    @FXML ComboBox<String> divisionComboBox;
    @FXML ComboBox<String> districtComboBox;
    @FXML ComboBox<String> upazilaComboBox;
    @FXML ComboBox<String> unionComboBox;

    private Map<String, String> divisionMap = new HashMap<>();
    private Map<String, String> districtMap = new HashMap<>();
    private Map<String, String> upazilaMap = new HashMap<>();
    private Map<String, String> unionMap = new HashMap<>();

    public void initialize() {
        itemContainer.requestFocus();
        loadDivisions();

        // Add listener for changes in divisionComboBox
        divisionComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String divisionId = divisionMap.get(newVal);
                if (divisionId != null) {
                    loadDistricts(divisionId);
                }
            }
        });

        // Add listener for changes in districtComboBox
        districtComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String districtId = districtMap.get(newVal);
                if (districtId != null) {
                    loadUpazilas(districtId);
                }
            }
        });

        // Add listener for changes in upazilaComboBox
        upazilaComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String upazilaId = upazilaMap.get(newVal);
                if (upazilaId != null) {
                    loadUnions(upazilaId);
                }
            }
        });

        // Loading products into the cart
        for (Product product : Info.cartList) {
            FXMLLoader itemFxml = new FXMLLoader(getClass().getResource("/com/enam/supershop/onlineStore/cart/item.fxml"));
            HBox item = null;
            try {
                item = itemFxml.load();
                Item item1 = itemFxml.getController();
                item1.setInfo(product.imageURL, product.productName, product.price, product.unit, product.discount, product.quantity);
                itemContainer.getChildren().add(item);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        subtotal.setText("$" + Info.totalPrice);
    }

    private void loadDivisions() {
        String url = "https://bdapi.vercel.app/api/v.1/division";
        fetchData(url, "division", divisionComboBox, divisionMap);
    }

    private void loadDistricts(String divisionId) {
        String url = "https://bdapi.vercel.app/api/v.1/district/" + divisionId;
        fetchData(url, "district", districtComboBox, districtMap);
    }

    private void loadUpazilas(String districtId) {
        String url = "https://bdapi.vercel.app/api/v.1/upazilla/" + districtId;
        fetchData(url, "upazilla", upazilaComboBox, upazilaMap);
    }

    private void loadUnions(String upazilaId) {
        String url = "https://bdapi.vercel.app/api/v.1/union/" + upazilaId;
        fetchData(url, "union", unionComboBox, unionMap);
    }

    private void fetchData(String url, String type, ComboBox<String> comboBox, Map<String, String> dataMap) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    JSONObject responseObject = new JSONObject(response.body());
                    if (responseObject.getInt("status") == 200 && responseObject.getBoolean("success")) {
                        JSONArray dataArray = responseObject.getJSONArray("data");
                        List<String> dataList = new ArrayList<>();

                        dataMap.clear();
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObject = dataArray.getJSONObject(i);
                            String id = dataObject.getString("id");
                            String name = dataObject.getString("name");
                            dataList.add(name);
                            dataMap.put(name, id);
                        }

                        // Update ComboBox on the JavaFX Application Thread
                        javafx.application.Platform.runLater(() -> {
                            comboBox.getItems().clear();
                            comboBox.getItems().addAll(dataList);
                        });
                    }
                    return null;
                })
                .exceptionally(e -> {
                    e.printStackTrace(); // Handle any errors
                    return null;
                });
    }

    // Handle payment redirection
    public void goToPayment() throws IOException {
        SSLCommerz.totalAmount = Info.totalPrice;
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/enam/supershop/onlineStore/sslcommerz.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
