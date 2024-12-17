package com.enam.supershop.inventory;

//import com.enam.supershop.dataVisualize.ProfitDataFetcher;
//import com.enam.supershop.dataVisualize.ProfitProcessor;
import com.enam.supershop.datavisualise.ProfitDataFetcher;
import com.enam.supershop.datavisualise.ProfitProcessor;
import com.enam.supershop.utils.DBConnection;
import com.enam.supershop.utils.Product;
import com.enam.supershop.utils.ProductClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class Homepage {

    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TableView<ProductClass> tableView;
    @FXML private TableColumn<ProductClass, Integer> idColumn;
    @FXML private TableColumn<ProductClass, String> nameColumn;
    @FXML private TableColumn<ProductClass, Double> priceColumn;
    @FXML private TableColumn<ProductClass, Integer> countColumn;
    @FXML private TableColumn<ProductClass, Double> discountColumn;
    @FXML private TableColumn<ProductClass, String> imageUrlColumn;
    @FXML private TextField productIdField;
    @FXML private TextField productNameField;
    @FXML private TextField priceField;
    @FXML private TextField manufacturerField;
    @FXML private TextField imageUrlField;
    @FXML private ChoiceBox<String> categoryChoice;
    @FXML private ImageView imageView;
    @FXML private HBox addProductPage;
    @FXML private HBox dashboardPage;

    private ObservableList<ProductClass> productList = FXCollections.observableArrayList();

    public void goDashboard() throws SQLException {
//        addProductPage.setVisible(false);
//        dashboardPage.setVisible(true);
        ProfitDataFetcher fetcher = new ProfitDataFetcher();
        List<ProfitDataFetcher.ProfitData> profitDataList = fetcher.getProfitDataForToday();

        // Process data
        ProfitProcessor processor = new ProfitProcessor();
        Map<Integer, Double> hourlyProfit = processor.calculateProfitPerHour(profitDataList);

        // Create the X and Y axes
        NumberAxis xAxis = new NumberAxis(0, 23, 1);
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Hour of the Day");
        yAxis.setLabel("Profit (Taka)");

        // Create the LineChart
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Profit per Hour for Today");

        // Create a series for the data
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Profit");

        // Add data to the series
        for (Map.Entry<Integer, Double> entry : hourlyProfit.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Add the series to the chart
        lineChart.getData().add(series);

        // Set up the scene and stage
        Scene scene = new Scene(lineChart, 800, 600);
        Stage stage = new Stage();
        stage.setTitle("Profit Graph");
        stage.setScene(scene);
        stage.show();
    }

    public void goAddProduct() throws SQLException {
        dashboardPage.setVisible(false);
        addProductPage.setVisible(true);
    }

    public void initialize() {
        // ObservableList to store category names
        ObservableList<String> categoryList = FXCollections.observableArrayList();

        // Add "Select All" as the first option
        categoryList.add("Select All");

        // Fetch product categories from the database
        Connection conn = DBConnection.databaseConn;
        try {
            String query = "SELECT * FROM categories";
            String query2 = "SELECT * FROM products";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            Statement stmt2 = conn.createStatement();
            ResultSet rs2 = stmt2.executeQuery(query2);

            while (rs.next()) {
                categoryList.add(rs.getString("name"));
                categoryChoice.getItems().add(rs.getString("name"));
            }
            while (rs2.next()) {
                productList.add(new ProductClass(
                        rs2.getInt("id"),
                        rs2.getString("name"),
                        rs2.getDouble("price"),
                        rs2.getInt("itemCount"),
                        rs2.getDouble("discount"),
                        rs2.getString("manufacturer"),
                        rs2.getString("imageURL"),
                        rs2.getString("category")
                ));
            }
            tableView.setItems(productList);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database connection failed!");
        }

        // Set the items in the ComboBox
        categoryComboBox.setItems(categoryList);

        // Optional: Set a default value
        categoryComboBox.setValue("Select All");

        // Set up TableView columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        discountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));
        imageUrlColumn.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));

        // Add event listener to ComboBox
        categoryComboBox.setOnAction(event -> loadDataByCategory(categoryComboBox.getValue()));


        // Row Selection Listener
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection);
            }
        });

    }

    private void loadDataByCategory(String category) {
        productList.clear();
        Connection connection = DBConnection.databaseConn;
        try {
            String query = "SELECT * FROM products WHERE category = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, category);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                productList.add(new ProductClass(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("itemCount"),
                        resultSet.getDouble("discount"),
                        resultSet.getString("manufacturer"),
                        resultSet.getString("imageURL"),
                        resultSet.getString("category")
                ));
            }
            tableView.setItems(productList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void populateFields(ProductClass product) {
        imageView.setImage(new Image(product.getImageUrl()));
        productIdField.setText(String.valueOf(product.getId()));
        productNameField.setText(product.getName());
        priceField.setText(String.valueOf(product.getPrice()));
        manufacturerField.setText(product.getManufacturer());
        categoryChoice.setValue(product.getCategory());
        imageUrlField.setText(product.getImageUrl());
    }

    @FXML
    private void resetFields() {
        productIdField.clear();
        productNameField.clear();
        priceField.clear();
        manufacturerField.clear();
        categoryChoice.setValue(null);
        imageView.setImage(null);
        imageUrlField.clear();
    }

    @FXML
    private void addProduct() {
        Connection connection = DBConnection.databaseConn;
        try {
            String query = "INSERT INTO products (name, price, itemCount, discount, manufacturer, imageURL, category) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, productNameField.getText());
            ps.setDouble(2, Double.parseDouble(priceField.getText()));
            ps.setInt(3, Integer.parseInt("100")); // Item count example
            ps.setDouble(4, 0.0); // Discount example
            ps.setString(5, manufacturerField.getText());
            ps.setString(6, imageUrlField.getText());
            ps.setString(7, categoryChoice.getValue());

            ps.executeUpdate();
            loadAllProducts();
            resetFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateProduct() {
        ProductClass selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        Connection connection = DBConnection.databaseConn;
        try {
            String query = "UPDATE products SET name=?, price=?, manufacture=?, imageURL=?, category=? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, productNameField.getText());
            ps.setDouble(2, Double.parseDouble(priceField.getText()));
            ps.setString(3, manufacturerField.getText());
            ps.setString(4, imageUrlField.getText());
            ps.setString(5, categoryChoice.getValue());
            ps.setInt(6, selected.getId());

            ps.executeUpdate();
            loadAllProducts();
            resetFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteProduct() {
        ProductClass selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        Connection connection = DBConnection.databaseConn;
        try {
            String query = "DELETE FROM products WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, selected.getId());
            ps.executeUpdate();

            loadAllProducts();
            resetFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void loadAllProducts() {
        productList.clear();
        Connection connection = DBConnection.databaseConn;
        try {
            String query = "SELECT * FROM products";
            ResultSet rs = connection.createStatement().executeQuery(query);
            while (rs.next()) {
                productList.add(new ProductClass(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("itemCount"),
                        rs.getDouble("discount"),
                        rs.getString("manufacturer"),
                        rs.getString("imageURL"),
                        rs.getString("category")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
