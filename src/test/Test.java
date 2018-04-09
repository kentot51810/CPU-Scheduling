package test;

import sample.model.RoundRobin;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(4);
//        list.add(15);
//        list.add(20);
        RoundRobin roundRobin = new RoundRobin(1,2,list);
        List<RoundRobin.Process> roundRobin1 = roundRobin.getRoundRobin();
        Float aFloat = roundRobin.previousTurnaroundTime();
        System.out.println(aFloat);
        for (int index = 0; index < roundRobin1.size(); index++) {
            String processName = roundRobin1.get(index).getProcessName()+" ";
            String burstTime = roundRobin1.get(index).getBurstTime()+" ";
            String turnaroundTime = String.valueOf(roundRobin1.get(index).getTurnaroundTime());
            System.out.print(processName);
            System.out.print(burstTime);
            System.out.println(turnaroundTime);
        }
    }
}
