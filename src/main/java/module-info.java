module com.example.klinventorywgu {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.inventorymanagementsystem to javafx.fxml;
    exports com.example.inventorymanagementsystem;
    exports com.example.inventorymanagementsystem.controller;
    opens com.example.inventorymanagementsystem.controller to javafx.fxml;
    opens com.example.inventorymanagementsystem.model to javafx.base;
}