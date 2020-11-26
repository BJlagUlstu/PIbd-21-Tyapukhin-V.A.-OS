package com.company;

public class Cluster {

    private Cluster nextCluster;
    private int needCountCluster;
    private boolean isEmpty;
    private int selectionType = 0;

    public Cluster(int countClusters, boolean isEmpty) {
        this.isEmpty = isEmpty;
        this.needCountCluster = countClusters;

        if (needCountCluster > 0) {
            Main.console.append(String.format("Create a file cluster. It remains to create %d more cluster(s)" + "\n", needCountCluster));
            nextCluster = new Cluster(needCountCluster - 1, false);
        }
    }

    //получаем тип выделения
    public int getSelectionType() {
        return selectionType;
    }

    //устанавливаем тип выделения
    public void setSelectionType(int selectionType) {
        this.selectionType = selectionType;

        if(selectionType == 0) {
            isEmpty = true;
        }

        if(nextCluster != null) {
            nextCluster.setSelectionType(selectionType);
        }
    }

    public Cluster getLinkOnNextCluster() {
        return nextCluster;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}