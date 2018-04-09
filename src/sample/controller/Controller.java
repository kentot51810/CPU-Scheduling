package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    //---------------------------------------------FXML Instance Variable---------------------------------------
    @FXML private MenuItem quit;
    @FXML private CheckMenuItem enableGanttChart;
    @FXML private TextField quantum_TF;
    @FXML private TextField burstTime_TF;
    @FXML private Button addButton;
    @FXML private TableView<?> burstTable;
    @FXML private TableColumn<?, ?> processName_TableColumn;
    @FXML private TableColumn<?, ?> burstTime_TableColumn;
    @FXML private Button deleteButton;
    @FXML private HBox ganttChartContainer;
    @FXML private Label avg_waiting_time;
    @FXML private Label avg_turnaround_time;
    @FXML private Label quantum_label;



    @FXML
    void addButtonOnAction(ActionEvent event) {

    }

    @FXML
    void burstTime_TFOnKeyPressed(KeyEvent event) {

    }

    @FXML
    void deleteProcessButton(ActionEvent event) {

    }

    @FXML
    void ganttChartMenuValidation(ActionEvent event) {

    }

    @FXML
    void quitMenuButton(ActionEvent event) {

    }

    @FXML
    void toggleGanttChart(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
