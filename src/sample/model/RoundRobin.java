package sample.model;

import java.text.DecimalFormat;
import java.util.*;

import static java.lang.System.*;

public class RoundRobin {

    //Declaration of global variables
    private float turnaroundTime, waitingTime;

    private DecimalFormat decimalFormat;
    private List<Process> processList;
    private List<Float> waitTimeList;
    private List<Float> turnaroundTimeList;
    private List<Map<String, Integer>> processBurstList;
    private List<Map<String, Float>> processTurnaroundList;
    private String[] processName;
    private int numberOfProcess;

    private int quantum;
    private List<Integer> listOfBurstTime;

    //------------------------constructor_---------------------------------
    public RoundRobin(int numberOfProcess, int quantum, List<Integer> listOfBurstTime) {
        this.numberOfProcess = numberOfProcess;
        this.quantum = quantum;
        this.listOfBurstTime = listOfBurstTime;

        startTheLogic();
    }

    //------------------------getter methods---------------------------------
    public String getTurnaroundTime() {
        return decimalFormat.format(turnaroundTime);
    }

    public String getWaitingTime() {
        return decimalFormat.format(waitingTime);
    }

    public List<Process> getRoundRobin(){
        processList = new ArrayList<>();

        for (int index = 0; index < processBurstList.size(); index++) {
            Process process = new Process();
            Map.Entry<String, Integer> entries = processBurstList.get(index).entrySet().iterator().next();
            process.setProcessName(entries.getKey());
            process.setBurstTime(entries.getValue());
            process.setTurnaroundTime(turnaroundTimeList.get(index));
            processList.add(process);
        }
        return processList;
    }

    public Integer nextBurstTime(){
        return processList.listIterator().next().getBurstTime();
    }

    public String nextProcessName() {return processList.listIterator().next().getProcessName();}

    public Float nextTurnaroundTime() {return processList.listIterator().next().getTurnaroundTime();}

    public Integer previousBurstTime() {return processList.listIterator().previous().getBurstTime();}

    public String previousProcessName() {return processList.listIterator().previous().getProcessName();}

    public Float previousTurnaroundTime() {
        //if the previous element is empty then return 0
        try{
            return processList.listIterator().previous().getTurnaroundTime();
        }catch (NoSuchElementException e){
            return 0f;
        }
    }

    //------------------------logics---------------------------------
    private void initializeInstanceVariables() {
        processBurstList = new ArrayList<>();
        waitTimeList = new ArrayList<>();
        decimalFormat = new DecimalFormat("0.#");
        processTurnaroundList = new ArrayList<>();
        turnaroundTimeList = new ArrayList<>();
    }

    private void startTheLogic() {
        initializeInstanceVariables();

        out.println("\n\t\tROUND ROBIN SCHEDULING ALGORITHM");
        saveProcessBurstList();

        getTATList();
        saveProcessTATList();

        showGanttChart();

        getAverageTurnAroundTime();
        out.println("THE AVERAGE TURNAROUND TIME IS: " + decimalFormat.format(turnaroundTime));
        getAverageWaitingTime();
        out.println("THE AVERAGE WAITING TIME IS: " + decimalFormat.format(waitingTime));

        out.print("\n\n");

    }

    private  void getAverageWaitingTime() {
        List<Float> waitTime = new ArrayList<>();
        List<Float> toBeSubtracted = new ArrayList<>();
        List<Float> totalWaitingTime = new ArrayList<>();
        for (int index = 0; index < processName.length; index++){
            ListIterator<Map<String, Float>> mapListIterator = processTurnaroundList.listIterator();
            while (mapListIterator.hasNext()){
                Map.Entry<String, Float> entry = mapListIterator.next().entrySet().iterator().next();
                String key = entry.getKey();
                //iterate all and save the values to list.
                if (key.equals(processName[index])){
                    //TODO: Understand the context that I was trying to come up here a couple days ago.
//                    if (mapListIterator.previousIndex() == 0 && index == 0)
//                        toBeSubtracted.add(0f);
                    float value = entry.getValue();
                    toBeSubtracted.add(value);

                    if (mapListIterator.previousIndex() != 0){
                        mapListIterator.previous();
                        Map.Entry<String, Float> next = mapListIterator.previous().entrySet().iterator().next();

                        waitTime.add(next.getValue());
                        mapListIterator.next();
                        mapListIterator.next();
                    }

                }
                //if the map Iterator reached the last limit of the process[index] then do the computation.
                if (!mapListIterator.hasNext()){
                    float value = 0;
                    if (index == 0){
                        for (int i = 0; i < waitTime.size(); i++) {
                            float subtrahend = toBeSubtracted.get(i);
                            float waitTimeValue = waitTime.get(i);
                            value += (waitTimeValue - subtrahend);
                        }
                    }else {
                        for (int i = 0; i < waitTime.size() - 1; i++) {
                            int k = i +1;
                            float waitTimeValue = waitTime.get(k);
                            float subtrahend = toBeSubtracted.get(i);
                            value += (waitTimeValue - subtrahend);
                        }
                        value += waitTime.get(0);
                    }
                    waitTime.clear();
                    toBeSubtracted.clear();
                    totalWaitingTime.add(value);
                }
            }
        }

        for (float value : totalWaitingTime){
            waitingTime += value;
        }
        waitingTime /= numberOfProcess;

    }

    private void showGanttChart() {
        out.println("--------------------------------Gantt Chart--------------------------------");


            out.print("PROCESS  ");
        for (int index = 0; index < processBurstList.size(); index++) {
            Map.Entry<String, Integer> entries = processBurstList.get(index).entrySet().iterator().next();
            out.format("|%2s%5s%5s","",entries.getKey(),"");
        }
        out.println("|");

        out.print("BURST    ");
        for (int index = 0; index < processBurstList.size(); index++) {
            Map.Entry<String, Integer> entries = processBurstList.get(index).entrySet().iterator().next();
            out.format("|%2s%5s%5s","",entries.getValue(),"");
        }
        out.println("|");


        out.format("%10s",0);
        for (int index = 0; index < processBurstList.size(); index++) {
            out.format("%13s",decimalFormat.format(turnaroundTimeList.get(index)));
        }

//        for (int index = 0; index < processBurstList.size(); index++){
//            Map.Entry<String, Integer> entries = processBurstList.get(index).entrySet().iterator().next();
//            out.print("PROCESS  ");
//            out.format("|%2s%5s%5s","",entries.getKey(),"");
//        }

        out.println("\n\n");
    }

    private void saveProcessBurstList() {

        processName = new String[numberOfProcess];

        for (int index = 0; index < numberOfProcess; index++) {
            processName[index] = "P" + (index + 1);
        }

        while (true){
            boolean done = true;
            for (int index = 0; index < listOfBurstTime.size(); index++){
                Map<String, Integer> cpuRR = new HashMap<>();
                if (listOfBurstTime.get(index) > 0){
                    done = false;
                    int rr = 0;
                    if (listOfBurstTime.get(index) > quantum) {
                        rr = listOfBurstTime.get(index) - quantum;
                        cpuRR.put(processName[index], quantum);
                    } else {
                        //if the value of burst time is less than the quantum then subtract the value of burst time to itself.
                        rr = 0;
                        cpuRR.put(processName[index], listOfBurstTime.get(index));
                    }
                    processBurstList.add(cpuRR);
                    listOfBurstTime.set(index, rr);
                }
            }
            if (done) break;
        }
    }

    private void saveProcessTATList() {
        for (int index = 0; index < processBurstList.size(); index++){
            Map.Entry<String, Integer> s = processBurstList.get(index).entrySet().iterator().next();
            String processName = s.getKey();
            float tat = turnaroundTimeList.get(index);
            Map<String, Float> processTATMap = new HashMap<>();
            processTATMap.put(processName, tat);

            processTurnaroundList.add(processTATMap);
        }
    }

    private void getTATList() {
        for (int index = 0; index < processBurstList.size(); index++) {
            Map.Entry<String, Integer> s = processBurstList.get(index).entrySet().iterator().next();
            turnaroundTime += s.getValue();
            turnaroundTimeList.add(turnaroundTime);
        }
    }

    private void getAverageTurnAroundTime() {
        turnaroundTime=0;
        for (int index = 0; index < processName.length; index++){
            ListIterator<Map<String, Float>> mapListIterator = processTurnaroundList.listIterator(processBurstList.size());
            while (mapListIterator.hasPrevious()){
                Map.Entry<String, Float> entry = mapListIterator.previous().entrySet().iterator().next();
                String key = entry.getKey();
                if (key.equals(processName[index])){
//                    Map<String, Float> previous = mapListIterator.previous();
//                    Set<Map.Entry<String, Float>> entries = previous.entrySet();
//                    Iterator<Map.Entry<String, Float>> iterator = entries.iterator();
//                    Map.Entry<String, Float> next = iterator.next();


//                    Map.Entry<String, Float> processTAT = mapListIterator.previous().entrySet().iterator().next();
                    turnaroundTime += entry.getValue();
                    break;
                }
            }
        }
        turnaroundTime /= numberOfProcess;
    }

    public class Process{
        private String processName;
        private Integer burstTime;
        private Float turnaroundTime;

        public String getProcessName() {
            return processName;
        }

        public void setProcessName(String processName) {
            this.processName = processName;
        }

        public Integer getBurstTime() {
            return burstTime;
        }

        public void setBurstTime(Integer burstTime) {
            this.burstTime = burstTime;
        }

        public Float getTurnaroundTime() {
            return turnaroundTime;
        }

        public void setTurnaroundTime(Float turnaroundTime) {
            this.turnaroundTime = turnaroundTime;
        }
    }
}
