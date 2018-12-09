/**
 * Main class to implement GUI_v3
 *
 * @author  YuKai Bao
 * @version 1.0
 * @since   2018.12.05
 */


package com.example.v3;

import javax.swing.tree.DefaultMutableTreeNode;
import java.nio.file.Paths;

public class main_v1 {

    public static String current_path_str = Paths.get(".").toAbsolutePath().normalize().toString();

    public static void main(String[] args) {

        // Makes GUI
        GUI_v3 gui = new GUI_v3();

        // sets initial filepath
        gui.setInitialFilePath(current_path_str);

        // Sets default beginning folder at where the program is being implemented
        // Can also be added to setInitialFilePath
        DefaultMutableTreeNode head = new DefaultMutableTreeNode(gui.getCurrentDirectory(current_path_str));
        gui.constructTree(current_path_str, head);
    }
}
