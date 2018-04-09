package sample.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Process {
    private SimpleStringProperty processName;
    private IntegerProperty burstTime;

    public Process(String processName, int burstTime) {
        this.processName = new SimpleStringProperty(processName);
        this.burstTime = new SimpleIntegerProperty(burstTime);
    }

    public String getProcessName() {
        return processName.get();
    }

    public SimpleStringProperty processNameProperty() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName.set(processName);
    }

    public int getBurstTime() {
        return burstTime.get();
    }

    public IntegerProperty burstTimeProperty() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime.set(burstTime);
    }
}
