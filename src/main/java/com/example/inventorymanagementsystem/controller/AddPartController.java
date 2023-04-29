package com.example.inventorymanagementsystem.controller;

import com.example.inventorymanagementsystem.model.InHouse;
import com.example.inventorymanagementsystem.model.Inventory;
import com.example.inventorymanagementsystem.model.Outsourced;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddPartController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private ToggleGroup sourceTG;

    @FXML
    private Button cancelBtn;

    @FXML
    private Label companyNameLbl;

    @FXML
    private TextField companyNameTxt;

    @FXML
    private RadioButton inHouseRBtn;

    @FXML
    private TextField invTxt;

    @FXML
    private Label machineIdLbl;

    @FXML
    private TextField machineIdTxt;

    @FXML
    private TextField maxTxt;

    @FXML
    private TextField minTxt;

    @FXML
    private TextField nameTxt;

    @FXML
    private RadioButton outsourcedRBtn;

    @FXML
    private TextField priceTxt;

    @FXML
    private Button saveBtn;

    public static int partCounter = 1;

    @FXML
    void inHouseRBtnSelected(ActionEvent event) throws IOException {
        companyNameLbl.setOpacity(0);

        companyNameTxt.setOpacity(0);
        companyNameTxt.setEditable(false);

        machineIdLbl.setOpacity(1);

        machineIdTxt.setOpacity(1);
        machineIdTxt.setEditable(true);
    }

    @FXML
    void outsourcedRBtnSelected(ActionEvent event) throws IOException {
        machineIdLbl.setOpacity(0);

        machineIdTxt.setOpacity(0);
        machineIdTxt.setEditable(false);

        companyNameLbl.setOpacity(1);

        companyNameTxt.setOpacity(1);
        companyNameTxt.setEditable(true);
    }

    @FXML
    void onActionDisplayMainMenu(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/com/example/inventorymanagementsystem/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionSave(ActionEvent event) throws IOException {
        try {
            int id = partCounter;
            String name = nameTxt.getText();
            int stock = Integer.parseInt(invTxt.getText());
            double price = Double.parseDouble(priceTxt.getText());
            int max = Integer.parseInt(maxTxt.getText());
            int min = Integer.parseInt(minTxt.getText());
            int machineId;
            String companyName;

            if (inHouseRBtn.isSelected() && (max > min) && (min <= stock) && (max >= stock)) {
                machineId = Integer.parseInt(machineIdTxt.getText());
                Inventory.addPart(new InHouse(id, name, price, stock, min, max, machineId));
                partCounter++;

                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/com/example/inventorymanagementsystem/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            } else if (outsourcedRBtn.isSelected() && (max > min) && (min <= stock) && (max >= stock)) {
                companyName = companyNameTxt.getText();
                Inventory.addPart(new Outsourced(id, name, price, stock, min, max, companyName));
                partCounter++;

                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inHouseRBtn.setSelected(true);

        companyNameLbl.setOpacity(0);

        companyNameTxt.setOpacity(0);
        companyNameTxt.setEditable(false);
    }
}