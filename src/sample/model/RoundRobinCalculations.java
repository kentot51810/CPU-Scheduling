package sample.model;

import javafx.collections.ObservableList;

public class RoundRobinCalculations {

    private ObservableList<Process> processList;
    private int quantum;
    private int total_wt = 0;
    private int total_tat = 0;


    public RoundRobinCalculations(ObservableList<Process> processList, int quantum) {
        this.processList = processList;
        this.quantum = quantum;

        findAverageTime(processList,quantum);
    }

    // Method to find the waiting time for all
    // processes
    private void findWaitingTime(int waitingTime[], int quantum) {
        // Make a copy of burst times burstTime[] to store remaining
        // burst times.
        int rem_bt[] = new int[processList.size()];
        for (int index = 0; index < processList.size(); index++)
            rem_bt[index] = processList.get(index).getBurstTime();

        int currentTime = 0; // Current time

        // Keep traversing processes in round robin manner
        // until all of them are not done.
        while (true) {
            boolean done = true;

            // Traverse all processes one by one repeatedly
            for (int index = 0; index < processList.size(); index++) {
                // If burst time of a process is greater than 0
                // then only need to process further
                if (rem_bt[index] > 0) {
                    done = false; // There is a pending process

                    if (rem_bt[index] > quantum) {
                        // Increase the value of currentTime i.e. shows
                        // how much time a process has been processed
                        currentTime += quantum;

                        // Decrease the burst_time of current process
                        // by quantum
                        rem_bt[index] -= quantum;
                    }

                    // If burst time is smaller than or equal to
                    // quantum. Last cycle for this process
                    else {
                        // Increase the value of currentTime i.e. shows
                        // how much time a process has been processed
                        currentTime = currentTime + rem_bt[index];

                        // Waiting time is current time minus time
                        // used by this process
                        waitingTime[index] = currentTime - processList.get(index).getBurstTime();

                        // As the process gets fully executed
                        // make its remaining burst time = 0
                        rem_bt[index] = 0;
                    }
                }
            }

            // If all processes are done
            if (done)
                break;
        }
    }

    // Method to calculate turn around time
    private void findTurnAroundTime(int waitingTime[], int tat[]) {
        // calculating turnaround time by adding
        // burstTime[i] + waitingTime[i]
        for (int index = 0; index < processList.size(); index++)
            tat[index] = processList.get(index).getBurstTime() + waitingTime[index];
    }

    // Method to calculate average time
    private void findAverageTime(ObservableList<Process> processList, int quantum) {
        int waitingTime[] = new int[processList.size()];
        int tat[] = new int[processList.size()];

        // Function to find waiting time of all processes
        findWaitingTime(waitingTime, quantum);

        // Function to find turn around time for all processes
        findTurnAroundTime(waitingTime, tat);

        for (int index = 0; index < processList.size(); index++) {
            total_wt += waitingTime[index];
            total_tat += tat[index];
        }
    }

    //-----------------------------Getter Method------------------------------
    public float getAverageTurnaroundTime(){
        return (float) total_tat / (float) processList.size();
    }

    public float getAverageWaitingTime(){
        return (float) total_wt / (float) processList.size();
    }


}
