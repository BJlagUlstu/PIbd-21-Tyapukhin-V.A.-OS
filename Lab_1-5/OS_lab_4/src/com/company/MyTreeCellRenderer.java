package com.company;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import java.awt.*;

//наследуем системный класс для добавления собственных иконок в дерево
public class MyTreeCellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        ImageIcon imageIcon = setIcon(node);

        if (imageIcon != null) {
            Image image = imageIcon.getImage();
            Image newImage = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(newImage);
            setIcon(imageIcon);
        }
        return c;
    }

    //устанавливаем иконку узла в зависимости от типа
    private ImageIcon setIcon(DefaultMutableTreeNode node) {

        MyTreeNode file = (MyTreeNode)node.getUserObject();

        if (node.isRoot()) {
            return new ImageIcon("Icons/HDD.png");
        } else if (file.isFolder()) {
            return new ImageIcon("Icons/FOLDER.png");
        } else {
            String fileExtension = file.getFileExtension();
            switch (fileExtension) {
                case "mp3":
                    return new ImageIcon("Icons/AUDIO.png");
                case "png":
                    return new ImageIcon("Icons/IMAGE.png");
                case "txt":
                    return new ImageIcon("Icons/TXT.png");
                case "java":
                    return new ImageIcon("Icons/JAVA.png");
                case "docx":
                    return new ImageIcon("Icons/DOCX.png");
            }
        }
        return null;
    }
}