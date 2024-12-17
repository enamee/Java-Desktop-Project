module com.enam.supershop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.web;
    requires java.net.http;
    requires org.json;


    opens com.enam.supershop to javafx.fxml;
    exports com.enam.supershop;
    exports com.enam.supershop.onlineStore.homepage;
    opens com.enam.supershop.onlineStore.homepage to javafx.fxml;
    exports com.enam.supershop.onlineStore.categoryPage;
    opens com.enam.supershop.onlineStore.categoryPage to javafx.fxml;
    exports com.enam.supershop.onlineStore;
    opens com.enam.supershop.onlineStore to javafx.fxml;
    exports com.enam.supershop.admin;
    opens com.enam.supershop.admin to javafx.fxml;
    exports com.enam.supershop.utils;
    opens com.enam.supershop.utils to javafx.fxml;
    exports com.enam.supershop.currentUser;
    opens com.enam.supershop.currentUser to javafx.fxml;
    exports com.enam.supershop.init;
    opens com.enam.supershop.init to javafx.fxml;
    exports com.enam.supershop.onlineStore.cart;
    opens com.enam.supershop.onlineStore.cart to javafx.fxml;
    opens com.enam.supershop.inventory to javafx.fxml;
    exports com.enam.supershop.inventory;
}