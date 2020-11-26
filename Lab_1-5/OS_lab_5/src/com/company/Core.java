package com.company;

import java.util.ArrayList;
import java.util.Random;

public class Core {

    private ArrayList <Process> processesListOne = new ArrayList <>();
    private ArrayList <Process> processesListTwo = new ArrayList <>();
    ArrayList <Process> listOfBlockedProcesses = new ArrayList <>();
    private Random rnd = new Random();
    private int numberOfProcesses;
    private int quantumTime;
    private int allTime = 0;
    private int maxWaitingTime = Integer.MAX_VALUE;

    public void createProcesses() throws CloneNotSupportedException {
        for (int pid = 0; pid < numberOfProcesses; pid++) {

            Process process;
            int blockProcess = rnd.nextInt(2); // 0 - не блокируется, 1 - блокируется

            if (blockProcess == 0) {
                process = new Process(pid, quantumTime, false);
            } else {
                process = new Process(pid, quantumTime, true);
            }
            processesListOne.add(process);

            //Клонируем процессы
            Process clone = (Process) process.clone();
            processesListTwo.add(clone);
        }
    }

    public void setQuantumTime(int time) {
        try {
            if (time > 0) quantumTime = time;
        } catch(Exception ex) {
            System.out.println("Invalid input!");
        }
    }

    public void setNumberOfProcesses(int number) {
        try {
            if (number > 0) numberOfProcesses = number;
        } catch(Exception ex) {
            System.out.println("Invalid input!");
        }
    }

    //Процессы не блокируются
    public void processesPlanningWithoutBlocking() {

        System.out.println("\n...Cyclical scheduling of processes without blocking...\n");

        int index = 0;
        int time;
        allTime = 0;

        while (!processesListOne.isEmpty()) {

            int currentQuantumTime = quantumTime;
            Process process = processesListOne.get(index);

            //Проверяем ожидает ли процесс ввод/вывод
            if (process.isAwaitingIO()) {
                System.out.println(String.format("___Process: %d is waiting for an I/O device___\n", process.getPID()));
                allTime += process.getTimeout();
            }

            //Проверяем выполнен процесс или нет, сравнивая текущий квант времени и требуемое время выполнения
            if (process.getExecutionTime() >= currentQuantumTime) {
                System.out.println(String.format("Process: %d has a runtime: %d ms", process.getPID(), currentQuantumTime));
                time = currentQuantumTime;
                process.setExecutionTime(currentQuantumTime);
            } else {
                System.out.println(String.format("Process: %d completed its execution in %d ms", process.getPID(), process.getExecutionTime()));
                time = process.getExecutionTime();
                process.performProcess();
            }

            allTime += time;
            currentQuantumTime -= time;

            System.out.println(String.format("Quantum size: %d ms\n", currentQuantumTime));

            //Удаляем из списка выполненные процессы
            if (processesListOne.get(index).isCompleted()) {
                processesListOne.remove(index);
                index--;
            }

            index++;

            if (index >= processesListOne.size()) {
                index = 0;
            }
        }
        System.out.println(String.format("Total time: %d ms", allTime));
    }

    //Процессы блокируются
    public void processesPlanningWithBlocking() {

        System.out.println("\n...Cyclical scheduling of processes with blocking...\n");

        int index = 0;
        int time;
        allTime = 0;

        while (!processesListTwo.isEmpty()) {

            if (index >= processesListTwo.size()) {

                index = 0;

                if(listOfBlockedProcesses.size() == processesListTwo.size()) {

                    System.out.println("All processes are waiting for an I/O device");
                    int waitingTime = maxWaitingTime;

                    for (Process blockedProcess : listOfBlockedProcesses) {
                        waitingTime = Integer.min(waitingTime, blockedProcess.getTimeout());
                    }

                    for (Process blockedProcess: listOfBlockedProcesses) {
                        System.out.println(String.format("___Process: %d is waiting for an I/O device___", blockedProcess.getPID()));
                        if (blockedProcess.getTimeout() <= waitingTime) {
                            blockedProcess.performWait();
                        } else {
                            blockedProcess.setTimeout(waitingTime);
                        }
                    }

                    for(int i = 0; i < listOfBlockedProcesses.size(); i++) {
                        if (!listOfBlockedProcesses.get(i).isAwaitingIO()) {
                            listOfBlockedProcesses.remove(i);
                        }
                    }

                    allTime += waitingTime;
                    System.out.println(String.format("Waiting time: %d ms\n", waitingTime));
                }
            }

            int currentQuantumTime = quantumTime;
            Process process = processesListTwo.get(index);

            if (listOfBlockedProcesses.contains(process)) {
                index++;
                continue;
            }

            //Проверяем ожидает ли процесс ввод/вывод
            if (process.isAwaitingIO()) {
                System.out.println(String.format("Process: %d was blocked, because it is waiting for I/O\n", process.getPID()));
                listOfBlockedProcesses.add(process);
                index++;
                continue;
            }

            //Проверяем выполнен процесс или нет, сравнивая текущий квант времени и требуемое время выполнения
            if (process.getExecutionTime() >= currentQuantumTime) {
                System.out.println(String.format("Process: %d has a runtime: %d ms", process.getPID(), currentQuantumTime));
                time = currentQuantumTime;
                process.setExecutionTime(currentQuantumTime);
            } else {
                System.out.println(String.format("Process: %d completed its execution in %d ms", process.getPID(), process.getExecutionTime()));
                time = process.getExecutionTime();
                process.performProcess();
            }

            allTime += time;
            currentQuantumTime -= time;

            System.out.println(String.format("Quantum size: %d ms\n", currentQuantumTime));

            for (Process blockedProcess: listOfBlockedProcesses) {
                System.out.println(String.format("___Process: %d is waiting for an I/O device___\n", blockedProcess.getPID()));
            }

            //Удаляем из списка процессы, которые не ожидают ввод/вывод
            for(int i = 0; i < listOfBlockedProcesses.size(); i++) {
                if (!listOfBlockedProcesses.get(i).isAwaitingIO()) {
                    listOfBlockedProcesses.remove(i);
                }
            }

            //Удаляем из списка выполненные процессы
            if (processesListTwo.get(index).isCompleted()) {
                processesListTwo.remove(index);
                index--;
            }

            index++;
        }
        System.out.println(String.format("Total time: %d ms", allTime));
    }
}