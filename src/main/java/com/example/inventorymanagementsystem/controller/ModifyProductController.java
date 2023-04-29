package com.example.inventorymanagementsystem.controller;

import com.example.inventorymanagementsystem.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyProductController implements Initializable {

    Stage stage;
    Parent scene;

    /**
     * An element that is part of the GUI.
     */
    @FXML
    private TableView<Part> allPartsTableView;

    /**
     * An element that is part of the GUI.
     */
    @FXML
    private TableColumn<Part, Integer> idTable1Col;

    /**
     * An element that is part of the GUI.
     */
    @FXML
    private TableColumn<Part, Integer> idTable2Col;

    /**
     * An element that is part of the GUI.
     */
    @FXML
    private TextField idTxt;

    /**
     * An element that is part of the GUI.
     */
    @FXML
    private TableColumn<Part, Integer> invLvlTable1Col;

    /**
     * An element that is part of the GUI.
     */
    @FXML
    private TableColumn<Part, Integer> invLvlTable2Col;

    /**
     * An element that is part of the GUI.
     */
    @FXML
    private TextField invTxt;

    /**
     * An element that is part of the GUI.
     */
    @FXML
    private TextField maxTxt;

    /**
     * An element that is part of the GUI.
     */
    @FXML
    private TextField minTxt;

    /**
     * An element that is part of the GUI.
     */
    @FXML
    private TableColumn<Part, String> nameTable1Col;

    /**
     * An element that is part of the GUI.
     */
    @FXML
    private TableColumn<Part, String> nameTable2Col;

    /**
     * An element that is part of the GUI.
     */
    @FXML
    private TextField nameTxt;

    /**
     * An element that is part of the GUI.
     */
    @FXML
    private TableColumn<Part, Double> priceTable1Col;

    /**
     * An element that is part of the GUI.
     */
    @FXML
    private TableColumn<Part, Double> priceTable2Col;

    /**
     * An element that is part of the GUI.
     */
    @FXML
    private TextField priceTxt;

    /**
     * An element that is part of the GUI.
     */
    @FXML
    private TableView<Part> requiredPartsTableView;

    /**
     * An element that is part of the GUI.
     */
    @FXML
    private TextField searchTxt;

    /**
     * An observable array list that stores the required parts for a product.
     */
    public static ObservableList<Part> requiredParts = FXCollections.observableArrayList();

    /**
     * A getter for the requiredParts observable array list.
     * @return
     */
    public static ObservableList<Part> getAllRequiredParts() {
        return requiredParts;
    }

    /**
     * An event that adds a part to the product when the button is pressed.
     * @param event
     */
    @FXML
    void onActionAddPart(ActionEvent event) {
        Part part = allPartsTableView.getSelectionModel().getSelectedItem();
        requiredParts.add(part);

        requiredPartsTableView.setItems(getAllRequiredParts());

        idTable2Col.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameTable2Col.setCellValueFactory(new PropertyValueFactory<>("name"));
        invLvlTable2Col.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceTable2Col.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * A method that deletes the selected part from the product.
     * @param id
     * @return
     */
    public boolean deletePart(int id) {
        for(Part part: getAllRequiredParts()) {
            if (part.getId() == id) {
                return getAllRequiredParts().remove(part);
            }
        }
        return false;
    }

    /**
     * An event that displays the main menu when the cancel button is pressed.
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionDisplayMainMenu(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/com/example/inventorymanagementsystem/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * An event that removes a part from the product when the button is pressed.
     * @param event
     */
    @FXML
    void onActionRemovePart(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Confirm that you want to delete the selected part.");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && (result.get() == ButtonType.OK)) {

            Part part = requiredPartsTableView.getSelectionModel().getSelectedItem();
            deletePart(part.getId());

            requiredPartsTableView.setItems(getAllRequiredParts());

            idTable2Col.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameTable2Col.setCellValueFactory(new PropertyValueFactory<>("name"));
            invLvlTable2Col.setCellValueFactory(new PropertyValueFactory<>("stock"));
            priceTable2Col.setCellValueFactory(new PropertyValueFactory<>("price"));
        }
    }

    /**
     * An event that saves the product after the button is pressed.
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionSave(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/inventorymanagementsystem/MainMenu.fxml"));
            loader.load();

            MainController mainController = loader.getController();
            mainController.receiveProduct(Integer.parseInt(idTxt.getText()));

            int id = Integer.parseInt(idTxt.getText());
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

                requiredParts.clear();

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
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

    /**
     * A method that sends the product information to the main menu.
     * @param product
     */
    public void sendProduct(Product product){
        idTxt.setText(String.valueOf(product.getId()));
        nameTxt.setText(product.getName());
        priceTxt.setText(String.valueOf(product.getPrice()));
        maxTxt.setText(String.valueOf(product.getMax()));
        minTxt.setText(String.valueOf(product.getMin()));
        invTxt.setText(String.valueOf(product.getStock()));

        requiredParts = product.getAllAssociatedParts();
        requiredPartsTableView.setItems(product.getAllAssociatedParts());

        idTable2Col.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameTable2Col.setCellValueFactory(new PropertyValueFactory<>("name"));
        invLvlTable2Col.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceTable2Col.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * A method that filters the parts based on id or name.
     * @param text
     * @return
     */
    public ObservableList<Part> filterParts(String text) {

        if(!(Inventory.getAllFilteredParts().isEmpty()))
            Inventory.getAllFilteredParts().clear();

        for(Part part : Inventory.getAllParts()) {
            if(part.getName().contains(text) || Integer.toString(part.getId()).contains(text))
                Inventory.getAllFilteredParts().add(part);
        }
        return Inventory.getAllFilteredParts();
    }

    /**
     * An event that searches for a part with the associated input when the enter button is pressed.
     * @param event
     */
    @FXML
    void onActionSearch(ActionEvent event) {
        if(searchTxt.getText().isEmpty())
            allPartsTableView.setItems(Inventory.getAllParts());
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

            idTable1Col.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameTable1Col.setCellValueFactory(new PropertyValueFactory<>("name"));
            invLvlTable1Col.setCellValueFactory(new PropertyValueFactory<>("stock"));
            priceTable1Col.setCellValueFactory(new PropertyValueFactory<>("price"));
        }
    }

    /**
     * The code that is run when the view is initially displayed.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Inventory.getAllFilteredParts().clear();
        Inventory.getAllFilteredProducts().clear();

        allPartsTableView.setItems(Inventory.getAllParts());

        idTable1Col.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameTable1Col.setCellValueFactory(new PropertyValueFactory<>("name"));
        invLvlTable1Col.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceTable1Col.setCellValueFactory(new PropertyValueFactory<>("price"));

        requiredPartsTableView.setItems(getAllRequiredParts());

        idTable2Col.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameTable2Col.setCellValueFactory(new PropertyValueFactory<>("name"));
        invLvlTable2Col.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceTable2Col.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}