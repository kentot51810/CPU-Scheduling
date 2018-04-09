package sample.model;

import java.util.Scanner;

public class RoundRobinGanttChart {

    public static void main(String args[]) {
        Scanner s = new Scanner(System.in);

        int wtime[], btime[], rtime[], processNumber, quantum, total;

        wtime = new int[10];
        btime = new int[10];
        rtime = new int[10];

        System.out.print("Enter number of processes(MAX 10): ");
        processNumber = s.nextInt();
        System.out.print("Enter burst time");
        for (int i = 0; i < processNumber; i++) {
            System.out.print("\nP[" + (i + 1) + "]: ");
            btime[i] = s.nextInt();
            rtime[i] = btime[i];
            wtime[i] = 0;
        }
        System.out.print("\n\nEnter quantum: ");
        quantum = s.nextInt();
        int rp = processNumber;
        int index = 0;
        int time = 0;
        System.out.print("0");
        wtime[0] = 0;
        while (rp != 0) {
            if (rtime[index] > quantum) {
                rtime[index] = rtime[index] - quantum;
                System.out.print(" | P[" + (index + 1) + "] | ");
                time += quantum;
                System.out.print(time);
            } else if (rtime[index] <= quantum && rtime[index] > 0) {
                time += rtime[index];
                rtime[index] = 0;
                System.out.print(" | P[" + (index + 1) + "] | ");
                rp--;
                System.out.print(time);
            }

            index++;
            if (index == processNumber) {
                index = 0;
            }

        }


    }
}