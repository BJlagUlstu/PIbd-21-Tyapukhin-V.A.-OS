package com.company;

public class Page implements Comparable<Page> {

    private int ID;
    private boolean isInMemory;
    private int circulationIndex = 0;
    private int modificationIndex = 0;
    private int PageID;
    private int PID;

    Page(int ID, int PID){
        this.ID = ID;
        this.PID = PID;
    }

    public int getID() {
        return ID;
    }

    public int getPID() {
        return PID;
    }

    public int getModificationIndex(){
        return modificationIndex;
    }

    public void setModificationIndex(int index) {
        modificationIndex = index;
    }

    public int getCirculationIndex() {
        return circulationIndex;
    }

    public void setCirculationIndex(int index) {
        circulationIndex = index;
    }

    public int getPageID() {
        return PageID;
    }

    public void setPageID(int PageID) {
        this.PageID = PageID;
    }

    public boolean isInMemory() {
        return isInMemory;
    }

    public void setInMemory(boolean inMemory) {
        isInMemory = inMemory;
    }

    public int compareTo(Page page) {
        if(circulationIndex * 2 + modificationIndex > page.getCirculationIndex() * 2 + page.getModificationIndex()){
            return 1;
        } else if (circulationIndex * 2 + modificationIndex < page.getCirculationIndex() * 2 + page.getModificationIndex()){
            return -1;
        } else {
            return 0;
        }
    }
}
