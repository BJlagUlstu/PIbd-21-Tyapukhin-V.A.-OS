package com.company;

import java.util.Random;

public class Process implements Cloneable {

    private int PID;
    private int executionTime;
    private int ioTimeout;
    private Random rnd = new Random();

    public Process(int pid, int quantumTime, boolean block) {
        PID = pid;
        executionTime = rnd.nextInt(quantumTime) + 30;
        if (block) {
            ioTimeout = rnd.nextInt(quantumTime * 3) + 50;
        } else {
            ioTimeout = 0;
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getPID() {
        return PID;
    }

    public boolean isCompleted() {
        return executionTime <= 0;
    }

    public boolean isAwaitingIO() {
        return ioTimeout > 0;
    }

    public int getTimeout() {
        return ioTimeout;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public void setTimeout(int quantumTime) {
        ioTimeout -= quantumTime;
    }

    public void setExecutionTime(int quantumTime) {
        executionTime -= quantumTime;
    }

    public void performProcess() { executionTime = 0; }

    public void performWait() {
        ioTimeout = 0;
    }
}