package com.company;

public class Main {

    public static void main(String[] args) throws CloneNotSupportedException {

        Core core = new Core();

        core.setQuantumTime(100);
        core.setNumberOfProcesses(5);
        core.createProcesses();
        core.processesPlanningWithoutBlocking();
        core.processesPlanningWithBlocking();
    }
}