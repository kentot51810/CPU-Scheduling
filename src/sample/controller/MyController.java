package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import sample.customComponents.GanttChart;
import sample.model.Process;
import sample.model.RoundRobin;

import java.net.URL;
import java.util.*;

public class MyController implements Initializable {

    //------------------------------------------FXML Instance Variables-------------------------
    @FXML private Label avg_waiting_time;
    @FXML private Label avg_turnaround_time;
    @FXML private TextField quantum_textfield;
    @FXML private TextField burstTime_textfield;
    @FXML private TableView<Process> burstTable;
    @FXML private Label quantum_label;
    @FXML private HBox ganttChartContainer;
    @FXML private TableColumn processName_TableColumn;
    @FXML private TableColumn burstTime_TableColumn;

    private int quantumValue;
    private static int index = 0;
    private ObservableList<Process> processes;
    private List<String> yAxis_processName = new ArrayList<>();
    private int burstTimeValue;
    private XYChart.Series series1 = new XYChart.Series();
    private GanttChart<Number, String> chart;

    @FXML
    void addButtonOnAction(ActionEvent event) {
        listenerForBurstTime();
    }

    @FXML
    void burstTimeOnKeyPressed(KeyEvent event) {

        if (event.getCode() == KeyCode.ENTER) {
            listenerForBurstTime();
        }
    }

    @FXML
    void deleteProcessButton(ActionEvent event) {
        chart.getData().remove(index);
        if (processes.size() <= 0) {
            return;
        }
        processes.remove(processes.size() - 1);
        index--;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        quantum_textfield.textProperty().addListener((observable, oldValue, newValue) -> {
            quantumValue = attachedListener(quantum_textfield, newValue);
            quantum_label.setText("Quantum value: " + quantumValue);
        });

        burstTime_textfield.textProperty().addListener((observable, oldValue, newValue) -> {
            burstTimeValue = attachedListener(burstTime_textfield, newValue);
        });

        processes = FXCollections.observableArrayList();
        chart = setUpGanttChart();

        processName_TableColumn.setCellValueFactory(new PropertyValueFactory<>("processName"));
        burstTime_TableColumn.setCellValueFactory(new PropertyValueFactory<>("burstTime"));

        burstTable.setItems(processes);

        ganttChartContainer.getChildren().add(chart);

    }


    //--------------------------------------------Private Methods----------------------------
    private GanttChart<Number, String> setUpGanttChart() {
        final NumberAxis xAxis = new NumberAxis();
        final CategoryAxis yAxis = new CategoryAxis();

        final GanttChart<Number, String> chart = new GanttChart<>(xAxis, yAxis);

        xAxis.setLabel("Burst Time");
        xAxis.setTickLabelFill(Color.CHOCOLATE);
        xAxis.setMinorTickCount(4);

        yAxis.setLabel("Process");
        yAxis.setTickLabelFill(Color.CHOCOLATE);
        yAxis.setTickLabelGap(10);
//        yAxis.setCategories(FXCollections.observableArrayList(yAxis_processName));

        chart.setTitle("Gantt Chart");
        chart.setLegendVisible(false);
        chart.setBlockHeight(50);

        chart.prefHeight(ganttChartContainer.getHeight());
        chart.prefWidth(ganttChartContainer.getWidth());

        chart.getStylesheets().add(getClass().getResource("ganttchart.css").toExternalForm());

        return chart;
    }

    private int attachedListener(TextField textField, String newValue) throws NumberFormatException {
        if (!newValue.matches("\\d*")) {
            textField.setText(newValue.replaceAll("[^\\d]", ""));
        }
        String textFieldText = textField.getText();
        try {
            return Integer.parseInt(textFieldText);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void listenerForBurstTime() {
        if (burstTimeValue == 0){
            return;
        }

        String processName = "P" + (index + 1);
        processes.add(new Process(processName, burstTimeValue));

//        String[] statusColor =  {"status-red","status-green","status-blue","navy","blue","aqua","teal","olive","green","lime","yellow","orange","red","fuchsia","purple",
//                "maroon","silver","gray","black"};
//        int random = new Random().nextInt(statusColor.length) ;
//        //create a list of processName for yAxis Category
//        yAxis_processName.add(processes.get(index).getProcessName());
////        yAxis.setCategories(FXCollections.observableArrayList(yAxis_processName));
//
//        XYChart.Series<Number, String> series = new XYChart.Series<>();
//        series1.getData().add(new XYChart.Data<>(index, processName,
//                new GanttChart.ExtraData(1, statusColor[random])));
//        chart.getData().add(series1);

        calculateRoundRobin();
        index++;
        burstTime_textfield.clear();
    }

    private void calculateRoundRobin(){
        int burstTime;
        List<Integer> listOfBurstTime = new ArrayList<>();
        for (Process process : processes) {
            burstTime = process.getBurstTime();
            listOfBurstTime.add(burstTime);
        }

        RoundRobin roundRobin = new RoundRobin(processes.size(), quantumValue, listOfBurstTime);
        List<RoundRobin.Process> processList = roundRobin.getRoundRobin();

        avg_turnaround_time.setText(roundRobin.getTurnaroundTime());
        avg_waiting_time.setText(roundRobin.getWaitingTime());

        configGanttChart(processList);


    }

    private void configGanttChart(List<RoundRobin.Process> list){
        String[] statusColor =  {"status-red","status-green","status-blue","navy","blue","aqua","teal","olive","green","lime","yellow","orange","red","fuchsia","purple",
                "maroon","silver","gray","black"};
        if (!chart.getData().isEmpty()){
            chart.getData().clear();
        }
        List<String> processNameList = new ArrayList<>();
//        XYChart.Series<Number, String> series1 = new XYChart.Series<>();
        for (Process process : processes) {
            processNameList.add(process.getProcessName());
        }
        XYChart.Series series = new XYChart.Series();
        for (int index = 0; index < processNameList.size(); index ++){
            Integer random = new Random().nextInt(statusColor.length);
            ListIterator<RoundRobin.Process> listIterator = list.listIterator(list.size());
            while (listIterator.hasPrevious()) {
                String processName = listIterator.previous().getProcessName();
                if (processName.equals(processNameList.get(index))){
                    float turnaroundTime = 0;
                    try {
                        turnaroundTime = listIterator.previous().getTurnaroundTime();
                    }catch (NoSuchElementException e){
                        turnaroundTime = 0;
                    }
                    float xValue = turnaroundTime;
                    String yValue = processNameList.get(index);
                    int rowSpan = list.get(index).getBurstTime();

                    series.getData().add(new XYChart.Data<>(xValue, yValue,
                            new GanttChart.ExtraData(rowSpan, statusColor[index])));

                }
            }

        }
    chart.getData().add(series);


//        for (int i = 0; i < processNameList.size(); i++){
//            for (int index = 0; index < list.size(); index ++){
//
//                //if equals then get the previous turnaroundTime
//                float turnaroundTime = 0;
//                if (Objects.equals(list.get(index).getProcessName(), processes.get(i).getProcessName())){
//                    try{
//                        turnaroundTime = list.listIterator().previous().getTurnaroundTime();
//                    } catch (NoSuchElementException e){
//                        turnaroundTime = 0;
//                    }
//                    float xValue = turnaroundTime;
//                    String yValue = processes.get(i).getProcessName();
//                    int rowSpan = list.get(index).getBurstTime();
//                    series.getData().add(new XYChart.Data<>(xValue, yValue,
//                            new GanttChart.ExtraData(rowSpan, statusColor[random])));
//                }
//            }
//
//            chart.getData().add(series);
//        }
    }
}


