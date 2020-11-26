package com.company;

import java.util.HashMap;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class Methods {

    private HDD hardDrive;
    private JTree fileManager;
    private DefaultMutableTreeNode folder;
    private DefaultMutableTreeNode file;
    private MyTreeNode treeNode;
    private DefaultMutableTreeNode defaultMutableTreeNode;
    private boolean check = false;
    private HashMap<String, Integer> identicalObjectsForFile = new HashMap<>();
    private HashMap<String, Integer> identicalObjectsForFolder = new HashMap<>();
    private int countCopy = 0;
    private int nodeSize = 0;
    private StringBuilder nameObject;

    public Methods(DefaultMutableTreeNode root, HDD hardDrive, JTree fileManager) {
        this.hardDrive = hardDrive;
        this.fileManager = fileManager;

        initializeJTree(root);
    }

    //устанавливаем параметры узлов
    public void setOptions(String nameNode, String FileExpansion) {
        treeNode = new MyTreeNode(nameNode, FileExpansion, false);
        file = new DefaultMutableTreeNode(treeNode);
        folder.add(file);
        hardDrive.addToHDD(treeNode);
    }

    //инициализируем дерево
    public void initializeJTree(DefaultMutableTreeNode root) {
        folder = new DefaultMutableTreeNode(new MyTreeNode("OS_lab", true));
        root.add(folder);
        setOptions("Laba #1", "java");
        setOptions("Laba #2", "java");
        setOptions("Picture #1", "png");
        displayTheSelectedObject(folder, 1);

        folder = new DefaultMutableTreeNode(new MyTreeNode("OS_lecture", true));
        root.add(folder);
        setOptions("Lecture #1", "docx");
        setOptions("Lecture #2", "txt");
        setOptions("Music", "mp3");
        displayTheSelectedObject(folder, 1);

        folder = new DefaultMutableTreeNode(new MyTreeNode("Empty", true));
        root.add(folder);
        setOptions("Empty", "txt");
        displayTheSelectedObject(folder, 1);
    }

    //получаем размер узла
    public int getNodeSize(DefaultMutableTreeNode node) {
        nodeSize = 0;
        if(((MyTreeNode)node.getUserObject()).isFolder()) {
            for (int i = 0; i < node.getChildCount(); i++) {
                defaultMutableTreeNode = (DefaultMutableTreeNode)node.getChildAt(i);
                if (!((MyTreeNode) defaultMutableTreeNode.getUserObject()).isFolder()){
                    nodeSize += ((MyTreeNode) defaultMutableTreeNode.getUserObject()).getSize();
                } else {
                    nodeSize += getNodeSize((DefaultMutableTreeNode) node.getChildAt(i));
                }
            }
            Main.console.append(String.format("Folder size %s is %d" + "\n", node, nodeSize));
            return nodeSize;
        } else {
            Main.console.append(String.format("File size %s is %d" + "\n", node, nodeSize));
            return nodeSize = ((MyTreeNode) defaultMutableTreeNode.getUserObject()).getSize();
        }
    }

    //добавляем узел в дерево
    public MyTreeNode addToJTree(DefaultMutableTreeNode selectedNode, MyTreeNode node) {
        nameObject = new StringBuilder(node.getName());
        check = false;
        if(!(identicalObjectsForFile.containsKey(node.getName())) && !(node.isFolder())) {
            identicalObjectsForFile.put(node.getName(), 1);
        } else if(!(identicalObjectsForFolder.containsKey(node.getName())) && (node.isFolder())) {
            identicalObjectsForFolder.put(node.getName(), 1);
        }

        for (; ;) { //проверяем наличие поступаемого объекта в нашем дереве
            for (int i = 0; i < selectedNode.getChildCount(); i++) {
                //если добавляется файл или папка, которые уже есть в дереве
                if ((!(((MyTreeNode) ((DefaultMutableTreeNode) selectedNode.getChildAt(i)).getUserObject()).isFolder())
                        && ((MyTreeNode) ((DefaultMutableTreeNode) selectedNode.getChildAt(i)).getUserObject()).getName().contentEquals(nameObject)
                        && ((MyTreeNode) ((DefaultMutableTreeNode) selectedNode.getChildAt(i)).getUserObject()).getFileExtension().equals(node.getFileExtension()))
                        || (((MyTreeNode) ((DefaultMutableTreeNode) selectedNode.getChildAt(i)).getUserObject()).isFolder()
                        && ((MyTreeNode) ((DefaultMutableTreeNode) selectedNode.getChildAt(i)).getUserObject()).getName().contentEquals(nameObject))) {
                    check = true;
                }
            }
            //смотрим сколько раз этот файл или папка встречались в дереве
            if (check) {
                if(identicalObjectsForFile.containsKey(node.getName()) && !(node.isFolder())) {
                    countCopy = identicalObjectsForFile.get(node.getName());
                    identicalObjectsForFile.put(node.getName(), identicalObjectsForFile.get(node.getName()) + 1);
                } else if(identicalObjectsForFolder.containsKey(node.getName()) && (node.isFolder())) {
                    countCopy = identicalObjectsForFolder.get(node.getName());
                    identicalObjectsForFolder.put(node.getName(), identicalObjectsForFolder.get(node.getName()) + 1);
                }
                check = false;
                nameObject.append(String.format(" (%d)", countCopy));
            } else {
                break;
            }
        }

        MyTreeNode newTreeNode;
        if(node.isFolder()) {
            newTreeNode = new MyTreeNode(nameObject.toString(), true);
        } else {
            newTreeNode = new MyTreeNode(nameObject.toString(), node.getFileExtension(), false);
        }

        DefaultMutableTreeNode newJTreeNode = new DefaultMutableTreeNode(newTreeNode);
        selectedNode.add(newJTreeNode);

        DefaultTreeModel defaultTreeModel = (DefaultTreeModel) fileManager.getModel();
        defaultTreeModel.reload();

        fileManager.scrollPathToVisible(new TreePath(newJTreeNode.getPath()));
        fileManager.setSelectionPath(new TreePath(newJTreeNode.getPath()));
        return newTreeNode;
    }

    //удаляем узел из дерева
    public void removeFromJTree(boolean removeFromHDD) {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)fileManager.getLastSelectedPathComponent();
        if(selectedNode != fileManager.getModel().getRoot()) {
            if (removeFromHDD) {
                displayTheSelectedObject(selectedNode, 0);
            } else {
                displayTheSelectedObject(selectedNode, 3);
            }

            hardDrive.setCountOfEmptySectors(getNodeSize(selectedNode));

            MyTreeNode node = (MyTreeNode) selectedNode.getUserObject();
            String nameNode = node.getName();
            if(!node.isFolder()) {
                String fileExtension = node.getFileExtension();
                Main.console.append(String.format("Remove from HDD \"%s.%s\"" + "\n", nameNode, fileExtension));
            } else {
                Main.console.append(String.format("Remove from HDD \"%s\"" + "\n", nameNode));
            }

            DefaultTreeModel defaultTreeModel = (DefaultTreeModel)fileManager.getModel();
            defaultTreeModel.removeNodeFromParent(selectedNode);
            defaultTreeModel.reload();
        }
    }

    //перемещаем узел в указанную папку, удаляя из исходной
    public void moveInFolder (DefaultMutableTreeNode selectedNode, DefaultMutableTreeNode newNode){
        copyTheNode(selectedNode, newNode, false);
        fileManager.setSelectionPath(new TreePath(selectedNode.getPath()));
        removeFromJTree(false);
    }

    //копируем узел и его потомков (если есть)
    public void copyTheNode(DefaultMutableTreeNode selectedNode, DefaultMutableTreeNode newNodeToCopy, boolean createNewFile) {
        MyTreeNode object = (MyTreeNode) selectedNode.getUserObject();
        nameObject = new StringBuilder(object.getName());
        check = false;

        for (; ;) {
            for (int i = 0; i < newNodeToCopy.getChildCount(); i++) {
                //проверяем наличие объекта в дереве: если это папка или файл, у которых совпадают названия с уже имеющимися в дереве
                if (!(((MyTreeNode) ((DefaultMutableTreeNode) newNodeToCopy.getChildAt(i)).getUserObject()).isFolder())
                        && ((MyTreeNode) ((DefaultMutableTreeNode) newNodeToCopy.getChildAt(i)).getUserObject()).getName().contentEquals(nameObject)
                        && ((MyTreeNode) ((DefaultMutableTreeNode) newNodeToCopy.getChildAt(i)).getUserObject()).getFileExtension().equals(object.getFileExtension())
                        || ((MyTreeNode) ((DefaultMutableTreeNode) newNodeToCopy.getChildAt(i)).getUserObject()).isFolder()
                        && ((MyTreeNode) ((DefaultMutableTreeNode) newNodeToCopy.getChildAt(i)).getUserObject()).getName().contentEquals(nameObject)) {
                    check = true;
                }
            }

            if (check) {
                nameObject.append(" — copy");
                check = false;
            } else {
                break;
            }
        }

        MyTreeNode newTreeNode;
        if(createNewFile) {
            if(object.isFolder()) {
                newTreeNode = new MyTreeNode(nameObject.toString(), true);
            } else {
                newTreeNode = new MyTreeNode(nameObject.toString(), object.getFileExtension(), false);
                hardDrive.addToHDD(newTreeNode);
            }
        } else {
            newTreeNode = (MyTreeNode) selectedNode.getUserObject();
        }

        //копируем узел и его потомков, а затем добавляем в дерево
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newTreeNode);
        newNodeToCopy.add(newNode);

        for (int i = 0; i < selectedNode.getChildCount(); i++) {
            copyTheNode((DefaultMutableTreeNode)selectedNode.getChildAt(i), newNode, createNewFile);
        }

        DefaultTreeModel defaultTreeModel = (DefaultTreeModel) fileManager.getModel();
        defaultTreeModel.reload();
        fileManager.scrollPathToVisible(new TreePath(newNode.getPath()));
        displayTheSelectedObject(newNode, 3);
    }

    //отображаем выбранный в дереве объект (снимаем выделение, если он уже не выбран)
    public void displayTheSelectedObject(DefaultMutableTreeNode selectedNode, int selectionType) {

        //если выбрана папка, то выделяем всех потомков, иначе выбран файл и выделяем только его
        if (!selectedNode.isRoot() && ((MyTreeNode) selectedNode.getUserObject()).isFolder()) {
            for (int i = 0; i < selectedNode.getChildCount(); i++) {
                defaultMutableTreeNode = (DefaultMutableTreeNode) selectedNode.getChildAt(i);
                displayTheSelectedObject(defaultMutableTreeNode, selectionType);
            }
        } else if (!selectedNode.isRoot() && !((MyTreeNode) selectedNode.getUserObject()).isFolder()) {
            ((MyTreeNode) selectedNode.getUserObject()).displayTheSelectedObject(selectionType);
        }
        hardDrive.repaint();
    }
}