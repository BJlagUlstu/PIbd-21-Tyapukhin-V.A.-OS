package com.company;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Main {

    private HDD hardDrive;
    private JTree fileManager;
    private Methods methods;

    private JFrame frame;
    private JComboBox<String> comboBoxFileFormats;
    private DefaultMutableTreeNode selectedItem;
    private DefaultMutableTreeNode newItemInTree;
    private JScrollPane treeViewScrollPane;
    public static JTextArea console;
    private JScrollPane consoleScrollPane;
    private JTextField textField;
    private JLabel labelNameObject;
    private JRadioButton radioButtonFolder;
    private JRadioButton radioButtonFile;
    private ButtonGroup group;
    private JButton buttonCopyInFolder;
    private JButton buttonMoveInFolder;
    private JButton buttonCreate;
    private JButton buttonDelete;
    private JButton buttonCopy;
    private JButton buttonMove;
    private MyTreeNode node;

    private final int diskPartitionSize = 2048; //размеры дискового раздела
    private final int diskSectorSize = 4; //размеры сектора диска
    private String[] fileFormats = {"txt", "png", "mp3", "java", "docx"};

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main window = new Main();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Main() {
        initialize();
    }

    public void initialize() {

        frame = new JFrame();
        frame.setBounds(100, 100, 1200, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        Font font = new Font("Serif", Font.BOLD, 14);

        console = new JTextArea();
        console.setBorder(new LineBorder(new Color(0, 0, 0)));
        console.setEditable(false);
        console.setFont(font);
        console.setBounds(450, 425, 725, 225);
        frame.getContentPane().add(console);

        hardDrive = new HDD(diskPartitionSize, diskSectorSize);
        hardDrive.setBorder(new LineBorder(new Color(0, 0, 0)));
        hardDrive.setBounds(5, 10, 890, 400);
        hardDrive.setBackground(Color.WHITE);
        hardDrive.setPreferredSize(new Dimension(800, 1000));
        frame.getContentPane().add(hardDrive);

        //создаем и инициализируем дерево
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new MyTreeNode("Hard Disk Drive", true));
        fileManager = new JTree(root);
        methods = new Methods(root, hardDrive, fileManager);
        fileManager.setEditable(true);
        fileManager.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        DefaultTreeModel model = (DefaultTreeModel) fileManager.getModel();
        model.reload();

        //отображаем занятое пространство выбранного узла в дереве
        fileManager.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)fileManager.getLastSelectedPathComponent();
            if (node != null) {
                hardDrive.removeSelection();
                methods.displayTheSelectedObject(node, 2);
            }
        });
        fileManager.setCellRenderer(new MyTreeCellRenderer());

        treeViewScrollPane = new JScrollPane(fileManager);
        treeViewScrollPane.setBounds(900, 10, 275, 400);
        frame.getContentPane().add(treeViewScrollPane);

        consoleScrollPane = new JScrollPane(console);
        consoleScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        consoleScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        consoleScrollPane.setBounds(450, 425, 725, 220);
        frame.getContentPane().add(consoleScrollPane);

        textField = new JTextField();
        textField.setBounds(10, 435, 300, 20);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        comboBoxFileFormats = new JComboBox<>(fileFormats);
        comboBoxFileFormats.setBounds(315, 435, 100, 20);
        comboBoxFileFormats.setFocusable(false);
        comboBoxFileFormats.setEditable(false);
        frame.getContentPane().add(comboBoxFileFormats);

        labelNameObject = new JLabel("Enter the title");
        labelNameObject.setBounds(10, 415, 150, 20);
        frame.getContentPane().add(labelNameObject);

        radioButtonFolder = new JRadioButton("Folder");
        radioButtonFolder.setBounds(20, 462, 100, 25);
        frame.getContentPane().add(radioButtonFolder);

        radioButtonFile = new JRadioButton("File");
        radioButtonFile.setBounds(120, 462, 100, 25);
        frame.getContentPane().add(radioButtonFile);

        group = new ButtonGroup();
        group.add(radioButtonFile);
        group.add(radioButtonFolder);

        node = new MyTreeNode();

        buttonCreate = new JButton("Create");
        buttonCreate.setFocusPainted(false);
        buttonCreate.setContentAreaFilled(false);
        buttonCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(radioButtonFile.isSelected()) {
                    if (node.getSizeByFileFormat((String)comboBoxFileFormats.getSelectedItem()) <= hardDrive.getCountOfEmptySectors()) {
                        selectedItem = (DefaultMutableTreeNode) fileManager.getLastSelectedPathComponent();
                        if(selectedItem.isRoot() || ((MyTreeNode)selectedItem.getUserObject()).isFolder()) {
                            MyTreeNode treeNode = methods.addToJTree(selectedItem, new MyTreeNode(textField.getText(), (String) comboBoxFileFormats.getSelectedItem(), false));
                            if (treeNode != null) {
                                hardDrive.addToHDD(treeNode);
                            }
                        } else {
                            JOptionPane.showMessageDialog(hardDrive, "Can not add file to file","Warning!", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Not enough free space", "Warning!", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (radioButtonFolder.isSelected()) {
                    selectedItem = (DefaultMutableTreeNode) fileManager.getLastSelectedPathComponent();
                    methods.addToJTree(selectedItem, new MyTreeNode(textField.getText(), true));
                } else {
                    JOptionPane.showMessageDialog(frame, "Indicate what you are creating", "Warning!", JOptionPane.INFORMATION_MESSAGE);
                }
                hardDrive.repaint();
            }
        });
        buttonCreate.setBounds(10, 500, 175, 25);
        frame.getContentPane().add(buttonCreate);

        buttonDelete = new JButton("Delete");
        buttonDelete.setFocusPainted(false);
        buttonDelete.setContentAreaFilled(false);
        buttonDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                methods.removeFromJTree(true);
                hardDrive.repaint();
            }
        });
        buttonDelete.setBounds(240, 500, 175, 25);
        frame.getContentPane().add(buttonDelete);

        buttonCopy = new JButton("Copy");
        buttonCopy.setFocusPainted(false);
        buttonCopy.setContentAreaFilled(false);
        buttonCopy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedItem = (DefaultMutableTreeNode) fileManager.getLastSelectedPathComponent();
                if (methods.getNodeSize(selectedItem) <= hardDrive.getCountOfEmptySectors()) {
                    buttonCopy.setEnabled(false);
                    buttonCopyInFolder.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(hardDrive, "Object size is too large", "Warning!", JOptionPane.ERROR_MESSAGE);
                }
                hardDrive.repaint();
            }
        });
        buttonCopy.setBounds(10, 547, 175, 25);
        buttonCopy.setEnabled(true);
        frame.getContentPane().add(buttonCopy);

        buttonCopyInFolder = new JButton("Copy to folder");
        buttonCopyInFolder.setFocusPainted(false);
        buttonCopyInFolder.setContentAreaFilled(false);
        buttonCopyInFolder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonCopy.setEnabled(true);
                buttonCopyInFolder.setEnabled(false);
                newItemInTree = (DefaultMutableTreeNode) fileManager.getLastSelectedPathComponent();
                if(!(selectedItem == newItemInTree)){
                    methods.copyTheNode(selectedItem, newItemInTree, true);
                } else {
                    JOptionPane.showMessageDialog(hardDrive, "An error occurred while copying", "Warning!", JOptionPane.ERROR_MESSAGE);
                }
                hardDrive.repaint();
            }
        });
        buttonCopyInFolder.setBounds(240, 548, 175, 25);
        buttonCopyInFolder.setEnabled(false);
        frame.getContentPane().add(buttonCopyInFolder);

        buttonMove = new JButton("Move");
        buttonMove.setFocusPainted(false);
        buttonMove.setEnabled(true);
        buttonMove.setContentAreaFilled(false);
        buttonMove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonMove.setEnabled(false);
                buttonMoveInFolder.setEnabled(true);
                selectedItem = (DefaultMutableTreeNode) fileManager.getLastSelectedPathComponent();
                hardDrive.repaint();
            }
        });
        buttonMove.setBounds(10, 604, 175, 25);
        buttonMove.setEnabled(true);
        frame.getContentPane().add(buttonMove);

        buttonMoveInFolder = new JButton("Move to folder");
        buttonMoveInFolder.setFocusPainted(false);
        buttonMoveInFolder.setEnabled(true);
        buttonMoveInFolder.setContentAreaFilled(false);
        buttonMoveInFolder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonMove.setEnabled(true);
                buttonMoveInFolder.setEnabled(false);
                newItemInTree = (DefaultMutableTreeNode) fileManager.getLastSelectedPathComponent();
                if(!(selectedItem == newItemInTree)){
                    methods.moveInFolder(selectedItem, newItemInTree);
                } else {
                    JOptionPane.showMessageDialog(hardDrive, "You can not move the folder to itself", "Warning!", JOptionPane.ERROR_MESSAGE);
                }
                hardDrive.repaint();
            }
        });
        buttonMoveInFolder.setBounds(240, 605, 175, 25);
        buttonMoveInFolder.setEnabled(false);
        frame.getContentPane().add(buttonMoveInFolder);
    }
}