package com.example.inventorymanagementsystem.controller;

import com.example.inventorymanagementsystem.model.Inventory;
import com.example.inventorymanagementsystem.model.Part;
import com.example.inventorymanagementsystem.model.Product;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ConcurrentModificationException;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private Button exitBtn;

    @FXML
    private TableColumn<Part, Integer> partIdCol;

    @FXML
    private TableColumn<Part, Integer> partInvLvlCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Double> partPriceCol;

    @FXML
    private TextField partSearchTxt;

    @FXML
    private TableView<Part> partTableView;

    @FXML
    private TableColumn<Product, Integer> productIdCol;

    @FXML
    private TableColumn<Product, Integer> productInvLvlCol;

    @FXML
    private TableColumn<Product, String> productNameCol;

    @FXML
    private TableColumn<Product, Double> productPriceCol;

    @FXML
    private TextField productSearchTxt;

    @FXML
    private TableView<Product> productTableView;

    public boolean deletePart(int id) {
        for(Part part: Inventory.getAllParts()) {
            if (part.getId() == id) {
                return Inventory.getAllParts().remove(part);
            }
        }
        return false;
    }

    public boolean deleteProduct(int id) {
        for(Product product: Inventory.getAllProducts()) {
            if (product.getId() == id) {
                return Inventory.getAllProducts().remove(product);
            }
        }
        return false;
    }

    @FXML
    void onActionDeletePart(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Confirm that you want to delete the selected part.");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && (result.get() == ButtonType.OK)) {
            Part part = partTableView.getSelectionModel().getSelectedItem();
            deletePart(part.getId());
        }
    }

    @FXML
    void onActionDeleteProduct(ActionEvent event) {
        if(productTableView.getSelectionModel().getSelectedItem().getAllAssociatedParts().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Confirm that you want to delete the selected product.");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && (result.get() == ButtonType.OK)) {
                Product product = productTableView.getSelectionModel().getSelectedItem();
                deleteProduct(product.getId());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please remove all parts associated with product before deleting.");
            alert.showAndWait();
        }
    }

    @FXML
    void onActionDisplayAddPartMenu(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/com/example/inventorymanagementsystem/AddPartMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionDisplayAddProductMenu(ActionEvent event) throws IOException {

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/com/example/inventorymanagementsystem/AddProductMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionDisplayModifyPartMenu(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/inventorymanagementsystem/ModifyPartMenu.fxml"));
        loader.load();

        ModifyPartController modifyPartController = loader.getController();
        modifyPartController.sendPart(partTableView.getSelectionModel().getSelectedItem());

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionDisplayModifyProductMenu(ActionEvent event) throws IOException {

        Product product = productTableView.getSelectionModel().getSelectedItem();
        System.out.println(product.getAllAssociatedParts());

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/inventorymanagementsystem/ModifyProductMenu.fxml"));
        loader.load();

        ModifyProductController modifyProductController = loader.getController();
        modifyProductController.sendProduct(productTableView.getSelectionModel().getSelectedItem());

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionExit(ActionEvent event) {
        System.exit(0);
    }

    public void receivePart(int id) {
        try {
            for (Part part : Inventory.getAllParts()) {
                if (id == part.getId())
                    Inventory.getAllParts().remove(part);
            }
        }
        catch(ConcurrentModificationException e) {
            System.out.println("Concurrent Modification Exception");
        }
    }

    public void receiveProduct(int id) {
        try {
            for (Product product : Inventory.getAllProducts()) {
                if (id == product.getId())
                    Inventory.getAllProducts().remove(product);
            }
        }
        catch(ConcurrentModificationException e) {
            System.out.println("Concurrent Modification Exception");
        }
    }

    public ObservableList<Part> filterParts(String text) {

        if(!(Inventory.getAllFilteredParts().isEmpty()))
            Inventory.getAllFilteredParts().clear();

        for(Part part : Inventory.getAllParts()) {
            if(part.getName().contains(text) || Integer.toString(part.getId()).contains(text))
                Inventory.getAllFilteredParts().add(part);
        }
        return Inventory.getAllFilteredParts();
    }

    @FXML
    void onActionSearchParts(ActionEvent event) {

        if(partSearchTxt.getText().isEmpty()) {
            partTableView.setItems(Inventory.getAllParts());
        }
        else if(filterParts(partSearchTxt.getText()).isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("There is no part with that id or name.");
            alert.showAndWait();
            partTableView.setItems(Inventory.getAllParts());
        }
        else {
            filterParts(partSearchTxt.getText());

            partTableView.setItems(Inventory.getAllFilteredParts());

            partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            partInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
            partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        }
    }

    public ObservableList<Product> filterProducts(String text) {

        if(!(Inventory.getAllFilteredProducts().isEmpty()))
            Inventory.getAllFilteredProducts().clear();

        for(Product product : Inventory.getAllProducts()) {
            if(product.getName().contains(text) || Integer.toString(product.getId()).contains(text))
                Inventory.getAllFilteredProducts().add(product);
        }
        return Inventory.getAllFilteredProducts();
    }

    @FXML
    void onActionSearchProducts(ActionEvent event) {
        if(productSearchTxt.getText().isEmpty()) {
            productTableView.setItems(Inventory.getAllProducts());
        }
        else if(filterProducts(productSearchTxt.getText()).isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("There is no product with that id or name.");
            alert.showAndWait();
            productTableView.setItems(Inventory.getAllProducts());
        }
        else {
            filterProducts(productSearchTxt.getText());

            productTableView.setItems(Inventory.getAllFilteredProducts());

            productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            productInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
            productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Inventory.getAllFilteredParts().clear();
        Inventory.getAllFilteredProducts().clear();

        partTableView.setItems(Inventory.getAllParts());

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTableView.setItems(Inventory.getAllProducts());

        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
}