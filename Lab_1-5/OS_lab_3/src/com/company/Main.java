package com.company;

public class Main {

    public static void main(String[] args) {

        MemoryDispatcher memoryDispatcher = new MemoryDispatcher();

        memoryDispatcher.setMemoryCapacity(256);
        memoryDispatcher.setPageCapacity(32);
        memoryDispatcher.setNumberOfProcess(8);
        memoryDispatcher.createPageTable();

        memoryDispatcher.run();
    }
}
