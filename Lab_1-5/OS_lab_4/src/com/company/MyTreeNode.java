package com.company;

public class MyTreeNode {

    private String nameObject;
    private String fileFormat;
    private int nodeSize;
    private Cluster firstCluster;
    private boolean isFolder;

    public MyTreeNode() {}

    //конструктор создания папки
    public MyTreeNode(String nameFolder, boolean isFolder) {
        this.nameObject = nameFolder;
        this.isFolder = isFolder;
        Main.console.append(String.format("Create \"%s\" folder" + "\n", nameFolder));
    }

    //конструктор создания файла
    public MyTreeNode(String nameFile, String fileExtension, boolean isFolder) {
        this.nameObject = nameFile;
        this.fileFormat = fileExtension;
        this.isFolder = isFolder;

        setSize(fileExtension);
        firstCluster = new Cluster(nodeSize, false);
        Main.console.append(String.format("Create \"%s.%s\" file" + "\n", nameFile, fileExtension));
    }

    public boolean isFolder() {
        return isFolder;
    }

    public String getName() {
        return nameObject;
    }

    public String getFileExtension() {
        return fileFormat;
    }

    //устанавливаем размеры файла (если папка, то 0)
    private void setSize(String fileExtension) {
        switch (fileExtension) {
            case "txt":
                nodeSize = 2;
                break;
            case "mp3":
                nodeSize = 16;
                break;
            case "java":
                nodeSize = 8;
                break;
            case "docx":
                nodeSize = 16;
            case "png":
                nodeSize = 4;
                break;
            default:
                nodeSize = 0;
                break;
        }
    }

    public void displayTheSelectedObject(int selectionType) {
        firstCluster.setSelectionType(selectionType);
    }

    public Cluster getFirstCluster() {
        return firstCluster;
    }

    public int getSize() {
        return nodeSize;
    }

    public int getSizeByFileFormat(String fileExtension) {
        switch (fileExtension) {
            case "txt":
                return nodeSize = 2;
            case "mp3":
            case "docx":
                return nodeSize = 16;
            case "java":
                return nodeSize = 8;
            case "png":
                return nodeSize = 4;
        }
        return nodeSize = 0;
    }
    @Override
    public String toString() {
        if (isFolder) {
            return nameObject;
        }
        return nameObject + "." + fileFormat;
    }
}