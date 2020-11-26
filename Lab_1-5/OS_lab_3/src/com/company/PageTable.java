package com.company;

import java.util.Arrays;

public class PageTable {

    private Page[] pageTable;
    private int maxPages;

    public PageTable(int maxPages) {
        this.maxPages = maxPages;
        pageTable = new Page[maxPages];
    }

    public int getMaxPages() {
        return maxPages;
    }

    public Page foundPage(int id) {
        return pageTable[id];
    }

    public void setPage(int id, Page page) {
        pageTable[id] = page;
    }

    public void sortPageTable() {
        Arrays.sort(pageTable);
    }
}
