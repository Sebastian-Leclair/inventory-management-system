package com.example.inventorymanagementsystem.controller;

import com.example.inventorymanagementsystem.model.Inventory;
import com.example.inventorymanagementsystem.model.Part;
import com.example.inventorymanagementsystem.model.Product;
import javafx.collections.FXCollections;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TableView<Part> allPartsTableView;

    @FXML
    private TextField invTxt;

    @FXML
    private TextField maxTxt;

    @FXML
    private TextField minTxt;

    @FXML
    private TextField nameTxt;

    @FXML
    private TableColumn<Part, Integer> partIdTable1Col;

    @FXML
    private TableColumn<Part, Integer> partIdTable2Col;

    @FXML
    private TableColumn<Part, Integer> partInvLvlTable1Col;

    @FXML
    private TableColumn<Part, Integer> partInvLvlTable2Col;

    @FXML
    private TableColumn<Part, String> partNameTable1Col;

    @FXML
    private TableColumn<Part, String> partNameTable2Col;

    @FXML
    private TableColumn<Part, Double> partPriceTable1Col;

    @FXML
    private TableColumn<Part, Double> partPriceTable2Col;

    @FXML
    private TextField priceTxt;

    @FXML
    private TableView<Part> requiredPartsTableView;

    @FXML
    private TextField searchTxt;

    public static ObservableList<Part> requiredParts = FXCollections.observableArrayList();

    public static ObservableList<Part> getAllRequiredParts() {
        return requiredParts;
    }

    public static int productCounter = 1;

    @FXML
    void onActionAddPart(ActionEvent event) {
        Part part = allPartsTableView.getSelectionModel().getSelectedItem();
        requiredParts.add(part);
    }

    @FXML
    void onActionDisplayMainMenu(ActionEvent event) throws IOException {

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/com/example/inventorymanagementsystem/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public boolean deletePart(int id) {
        for(Part part: getAllRequiredParts()) {
            if (part.getId() == id) {
                return getAllRequiredParts().remove(part);
            }
        }
        return false;
    }

    @FXML
    void onActionRemovePart(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Confirm that you want to delete the selected part.");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && (result.get() == ButtonType.OK)) {
            Part part = requiredPartsTableView.getSelectionModel().getSelectedItem();
            deletePart(part.getId());
        }
    }

    @FXML
    void onActionSave(ActionEvent event) throws IOException {
        try {
            int id = productCounter;
            String name = nameTxt.getText();
            int stock = Integer.parseInt(invTxt.getText());
            double price = Double.parseDouble(priceTxt.getText());
            int max = Integer.parseInt(maxTxt.getText());
            int min = Integer.parseInt(minTxt.getText());

            if((max > min) && (min <= stock) && (max >= stock)) {
                Product product = new Product(id, name, price, stock, min, max);

                Inventory.addProduct(product);

                for (Part part : getAllRequiredParts()) {
                    product.addAssociatedPart(part);
                }

                productCounter++;

                requiredParts.clear();

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/com/example/inventorymanagementsystem/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("Please make sure your minimum value is less than your maximum value and that your inventory value is within that range.");
                alert.showAndWait();
            }
        }
        catch(NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter a valid value for each text field.");
            alert.showAndWait();
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
    void onActionSearch(ActionEvent event) {
        if(searchTxt.getText().isEmpty()) {
            allPartsTableView.setItems(Inventory.getAllParts());
        }
        else if(filterParts(searchTxt.getText()).isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("There is no part with that id or name.");
            alert.showAndWait();
            allPartsTableView.setItems(Inventory.getAllParts());
        }
        else {
            filterParts(searchTxt.getText());

            allPartsTableView.setItems(Inventory.getAllFilteredParts());
        }

        partIdTable1Col.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameTable1Col.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvLvlTable1Col.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceTable1Col.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Inventory.getAllFilteredParts().clear();
        Inventory.getAllFilteredProducts().clear();

        allPartsTableView.setItems(Inventory.getAllParts());

        partIdTable1Col.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameTable1Col.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvLvlTable1Col.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceTable1Col.setCellValueFactory(new PropertyValueFactory<>("price"));

        requiredParts.clear();

        requiredPartsTableView.setItems(getAllRequiredParts());

        partIdTable2Col.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameTable2Col.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvLvlTable2Col.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceTable2Col.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}