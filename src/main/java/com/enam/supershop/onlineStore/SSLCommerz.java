package com.enam.supershop.onlineStore;

import com.enam.supershop.currentUser.Info;
import com.enam.supershop.utils.DBConnection;
import com.enam.supershop.utils.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class SSLCommerz {

    @FXML private StackPane root;
    @FXML private WebView webView;

    public static float totalAmount = 1300;

    private static final String STORE_ID = "allgo675d65afe0ac3"; // Replace with your test Store ID
    private static final String STORE_PASSWORD = "allgo675d65afe0ac3@ssl"; // Replace with your test Password
    private static final String TRANSACTION_URL = "https://sandbox.sslcommerz.com/gwprocess/v4/api.php";

    public void initialize() {
        handleProceedToCheckout();
    }

    private String createPaymentSession() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            Map<String, String> params = new HashMap<>();
            params.put("store_id", STORE_ID);
            params.put("store_passwd", STORE_PASSWORD);
            params.put("total_amount", String.valueOf(totalAmount));  // Use dynamic totalAmount
            params.put("currency", "BDT");  // Set to BDT for Bangladesh
            params.put("tran_id", "TEST_" + System.currentTimeMillis());
            params.put("success_url", "http://127.0.0.1:1000/success_done");
            params.put("fail_url", "http://127.0.0.1:1000/faildone");
            params.put("cancel_url", "http://127.0.0.1:1000/canceldone");
            params.put("cus_name", "Test Customer");
            params.put("cus_email", "test@example.com");
            params.put("cus_add1", "Test Address");
            params.put("cus_city", "Dhaka");
            params.put("cus_postcode", "1207");
            params.put("cus_country", "Bangladesh");
            params.put("cus_phone", "01700000000");
            params.put("shipping_method", "NO");
            params.put("product_name", "ride");
            params.put("product_category", "service");
            params.put("product_profile", "general");

            String form = params.entrySet().stream()
                    .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" +
                            URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TRANSACTION_URL))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseJson = response.body();
            System.out.println(responseJson);

            if (responseJson.contains("\"GatewayPageURL\"")) {
                int startIndex = responseJson.indexOf("\"GatewayPageURL\":\"") + 18;
                int endIndex = responseJson.indexOf("\"", startIndex);
                return responseJson.substring(startIndex, endIndex).replace("\\/", "/");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void handleProceedToCheckout() {
        try {
            String paymentUrl = createPaymentSession();
            if (paymentUrl != null) {
                webView.getEngine().load(paymentUrl);
            } else {
                showAlert("Failed", "Failed to create payment request.");
                return;
            }
            webView.getEngine().locationProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.contains("success_done")) {
                    webView.getEngine().loadContent("");  // Clear the WebView content
                    showSuccessAlert("Payment completed successfully!");
                    navigateToHomepage();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while processing the payment.");
        }
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);  // Change alert type to INFORMATION
        alert.setTitle("Payment Completed");
        alert.setHeaderText(null);  // Remove the header text
        alert.setContentText(message);  // Display the success message
        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void navigateToHomepage() {
        Connection conn = DBConnection.databaseConn;
        String table = Info.username;
        String query = "INSERT into " + table + " (product, productID, quantity, price) VALUES (?, ?, ?, ?)";
        for (Product product : Info.cartList) {
            try {
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, product.productName);
                ps.setInt(2, product.productID);
                ps.setFloat(3, product.quantity);
                ps.setFloat(4, product.price*product.quantity);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        String query2 = "INSERT into sold (userID, customerUsername, amountTaka) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps1 = conn.prepareStatement(query2);
            ps1.setInt(1, Info.userID);
            ps1.setString(2, Info.username);
            ps1.setFloat(3, totalAmount);
            ps1.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Info.cartList.clear();
        Info.totalPrice = 0;
        try {
            Parent dashboardRoot = FXMLLoader.load(getClass().getResource("/com/enam/supershop/onlineStore/homepage/view.fxml"));
            Scene dashboardScene = new Scene(dashboardRoot);
            Stage stage = (Stage) webView.getScene().getWindow();
            stage.close();
            stage = new Stage();
            stage.setScene(dashboardScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to navigate to Homepage. Please try again.");
        }
    }

}


