package com.company;

import java.util.ArrayList;
import java.util.Random;

public class Process {

    private Random rnd = new Random();
    private ArrayList<Page> virtualMemory;
    private int ID;

    Process(int ID) {
        this.ID = ID;
        virtualMemory = new ArrayList<>();
        int pagesCount = rnd.nextInt(3) + 2;
        for (int i = 1; i < pagesCount + 1; i++) {
            virtualMemory.add(new Page(i, ID));
        }
        System.out.println(String.format("Process %d contains %d pages", ID, pagesCount));
    }

    public int getID() {
        return ID;
    }

    public Page getMemoryPage(int id) {
        return virtualMemory.get(id);
    }

    public int getSize() {
        return virtualMemory.size();
    }
}
