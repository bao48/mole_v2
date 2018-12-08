package com.example.v3;

import javax.swing.tree.DefaultMutableTreeNode;
import java.nio.file.Paths;

public class main_v1 {

    public static String current_path_str = Paths.get(".").toAbsolutePath().normalize().toString();

    public static void main(String[] args) {
        GUI_v3 gui = new GUI_v3();
        gui.setInitialFilePath(current_path_str);
        DefaultMutableTreeNode head = new DefaultMutableTreeNode(gui.getCurrentDirectory(current_path_str));
        gui.constructTree(current_path_str, head);
    }
}
