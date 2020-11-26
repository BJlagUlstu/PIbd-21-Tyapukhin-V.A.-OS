package com.company;

import java.util.ArrayList;
import java.util.Random;

public class MemoryDispatcher {

    private PageTable PM;
    private int memoryCapacity, pageCapacity, number0fProcess;
    private ArrayList <Process> processList = new ArrayList <> ();
    private Random rnd = new Random();
    private int counterPageCapacity = 0;

    public void setMemoryCapacity(int capacity) {
        try {
            if (capacity > 0)
                memoryCapacity = capacity;
        } catch (Exception ex) {
            System.out.println("Invalid input!");
        }
    }

    public void setPageCapacity(int capacity) {
        try {
            if (capacity > 0)
                pageCapacity = capacity;
        } catch (Exception ex) {
            System.out.println("Invalid input!");
        }
    }

    public void setNumberOfProcess(int number) {
        try {
            if (number > 0)
                number0fProcess = number;
        } catch (Exception ex) {
            System.out.println("Invalid input!");
        }
    }

    public void createPageTable() {
        PM = new PageTable(pageCapacity / 2);
        for (int i = 0; i < number0fProcess; i++) {
            processList.add(new Process(i));
        }
        System.out.println();
    }

    public void lowerPagePriority() {
        System.out.println("___Reducing pages priority___");
        for(int id = 0; id < PM.getMaxPages(); id++) {
            if (PM.foundPage(id) != null) {
                PM.foundPage(id).setCirculationIndex(0);
                PM.foundPage(id).setModificationIndex(0);
            }
        }
    }

    public void performPageBreak(Page usefulPage) {
        System.out.println("___Executing a page break___");
        PM.sortPageTable();
        for(int id = 0; id < PM.getMaxPages(); id++) {
            System.out.println(String.format("PM: %d process: %d page: %d class: %d", PM.foundPage(id).getPageID(), PM.foundPage(id).getPID(), PM.foundPage(id).getID(), (PM.foundPage(id).getCirculationIndex() * 2 + PM.foundPage(id).getModificationIndex())));
        }
        System.out.println(String.format("Unloading page %d of process %d", PM.foundPage(0).getID(), PM.foundPage(0).getPID()));
        usefulPage.setInMemory(true);
        usefulPage.setCirculationIndex(1);
        usefulPage.setPageID(PM.foundPage(0).getPageID());
        PM.foundPage(0).setInMemory(false);
        PM.setPage(0, usefulPage);
        System.out.println(String.format("Loading page %d of process %d\nPage break completed successfully", usefulPage.getID(), usefulPage.getPID()));
    }

    public void swapping(Process process) {
        System.out.println("___Swapping___");
        for (int i = 0; i < process.getSize(); i++) {
            Page trashPage = PM.foundPage(i);
            if (trashPage != null) {
                System.out.println(String.format("Writing to HDD page %d of process %d",trashPage.getID(), trashPage.getPID()));
            }
            PM.setPage(i, process.getMemoryPage(i));
            System.out.println(String.format("Adding page %d of process %d", PM.foundPage(i).getID(), PM.foundPage(i).getPID()));
        }
        counterPageCapacity = 0;
    }

    public void run() {
        int counter = 0;
        while (counter < pageCapacity / 4) {
            for (Process process : processList) {
                if(counterPageCapacity >= (memoryCapacity * 0.8)) {
                    swapping(process); // выполнение свопинга
                    continue;
                }
                int index = rnd.nextInt(process.getSize());
                Page usefulPage = process.getMemoryPage(index);
                int actionType = rnd.nextInt(2);
                if (usefulPage.isInMemory()) { // страница уже находится в ФП
                    System.out.print(String.format("Page: %d process: %d PM: %d - ", usefulPage.getID(), process.getID(), usefulPage.getPageID()));
                    if (actionType == 0) {
                        usefulPage.setCirculationIndex(1); // изменяем индекс класса
                        System.out.println("Circulation");
                    } else {
                        usefulPage.setModificationIndex(1); // изменяем индекс класса
                        System.out.println("Modification");
                    }
                } else if (PM.foundPage(PM.getMaxPages() - 1) == null) { // страница отсутствует и область памяти свободна
                    for (int i = 0; i < PM.getMaxPages(); i++) {
                        if (PM.foundPage(i) == null) {
                            usefulPage.setInMemory(true);
                            usefulPage.setPageID(i);
                            PM.setPage(i, usefulPage);
                            System.out.println(String.format("Page %d of process %d is now in the PM: %d", usefulPage.getID(), process.getID(), i));
                            counterPageCapacity += pageCapacity;
                            break;
                        }
                    }
                } else { // в ФП нет свободного места
                    performPageBreak(usefulPage); // выполнение страничного прерывания
                }
            }
            lowerPagePriority(); // cнижение приоритета страниц
            counter++;
        }
    }
}