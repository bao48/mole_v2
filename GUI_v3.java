package com.example.v3;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.io.*;
import java.util.*;
import java.util.List;


public class GUI_v3 extends JFrame implements WindowListener {

    private JPanel panelNorth;
    private static JLabel defaultNorth = new JLabel("No folder selected.");
    private String pathInitial = "";
    private String currentDirectory;         // Directory that the open file is in
    private String currentOpenFile;         // Open file's name
    private String currentFilePath;

    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuFile = new JMenu("File");
    private JMenuItem menuItemOpen = new JMenuItem("Open");
    private JMenuItem menuItemSave = new JMenuItem("Save");

    private JPanel panelWest;
    private static JLabel defaultWest = new JLabel("No folder selected.");
    private JTree fileTree;

    private JPanel panelEast;
    private static JLabel defaultEast = new JLabel("Maybe something should go here!");
    private JLabel pathLabel;

    private JPanel panelCenter;
    private static JLabel defaultCenter = new JLabel("Select a file to begin");
    private JTextArea docEdit;

    private JPanel panelSouth;
    private static JLabel defaultSouth = new JLabel("Default footer");  // to remove

    public GUI_v3() {

        menuFile.setMnemonic(KeyEvent.VK_CONTROL);
        menuItemOpen.setMnemonic(KeyEvent.VK_O);
        menuItemSave.setMnemonic(KeyEvent.VK_S);
        KeyStroke keyStrokeToOpen = KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK);
        KeyStroke keyStrokeToSave = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
        menuItemOpen.setAccelerator(keyStrokeToOpen);
        menuItemSave.setAccelerator(keyStrokeToSave);

        menuItemOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                System.out.println("Opening File...");
            }
        });

        menuItemSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if ((currentOpenFile != null) && !currentOpenFile.isEmpty()) {
                    saveFile();
                }
                System.out.println("Saving File...");
            }
        });

        menuFile.add(menuItemOpen);
        menuFile.add(menuItemSave);

        menuBar.add(menuFile);

        panelNorth = new JPanel(new FlowLayout());
        panelNorth.add(menuBar);
        panelNorth.add(defaultNorth);

        panelWest = new JPanel(new FlowLayout());
        panelWest.add(defaultWest);

        panelEast = new JPanel(new FlowLayout());
        panelEast.add(defaultEast);

        panelCenter = new JPanel(new FlowLayout());
        panelCenter.add(defaultCenter);

        panelSouth = new JPanel(new FlowLayout());
        panelSouth.add(defaultSouth);

        setLayout(new BorderLayout());
        add(panelNorth, BorderLayout.NORTH);
        add(panelWest, BorderLayout.WEST);
        add(panelEast, BorderLayout.EAST);
        add(panelCenter, BorderLayout.CENTER);
        add(panelSouth, BorderLayout.SOUTH);

        addWindowListener(this);
        setVisible(true);
        setTitle("GUI_v3");
        pack();
    }

    public void saveFile() {
        System.out.println("Hello!");
        String contents = docEdit.getText();
        System.out.println(contents);
        try {

            XWPFDocument doc = new XWPFDocument();
            FileOutputStream out = new FileOutputStream(new File(currentFilePath));

            XWPFParagraph parag = doc.createParagraph();
            XWPFRun run = parag.createRun();

            for (String line : docEdit.getText().split("\\n")) {
                run.setText(line);
                run.addBreak();
            }
            doc.write(out);

            out.close();
            System.out.println(currentOpenFile + " was written successfully!");
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public void setInitialFilePath(String filePath) {
        pathInitial = filePath;
        currentDirectory = pathInitial;
        pathLabel = new JLabel(pathInitial);
        panelNorth.remove(defaultNorth);
        panelNorth.add(pathLabel);

        currentDirectory = getCurrentDirectory(filePath);
    }

    public String getCurrentDirectory(String filePath) {
        int lastIndex = pathInitial.lastIndexOf(File.separatorChar);

        System.out.println(lastIndex);

        String cd = "";

        if (lastIndex != -1) {
            cd = pathInitial.substring(lastIndex+1);
            System.out.println(currentDirectory);
        }
        return cd;
    }

    public String getHistory(String filePath) {
        int lastIndex = pathInitial.lastIndexOf(File.separatorChar);

        System.out.println(lastIndex);

        String cd = "";

        if (lastIndex != -1) {
            cd = pathInitial.substring(0, lastIndex);
            System.out.println(currentDirectory);
        }
        return cd;
    }


    public void constructTree(String filepath, DefaultMutableTreeNode head) {

        ArrayList<String> f1 = getFolderNames(filepath);
        ArrayList<String> f2 = getAllFileNames(filepath);

        if (f1.isEmpty() && f2.isEmpty()) {
            return;
        }

        ArrayList<DefaultMutableTreeNode> tn = new ArrayList<DefaultMutableTreeNode>();


        for (String f : f1) {
            tn.add(new DefaultMutableTreeNode(f));
            head.add(tn.get(tn.size()-1));
            constructTree(filepath + File.separatorChar + f, tn.get(tn.size()-1));
        }

        for (String f : f2) {
            tn.add(new DefaultMutableTreeNode(f));
            head.add(tn.get(tn.size()-1));
        }

        fileTree = new JTree(head);
        panelWest.setLayout(new BorderLayout());
        panelWest.setMaximumSize(new Dimension(5, 5));
        panelWest.remove(defaultWest);
        panelWest.add(fileTree);
        fileTree.addTreeSelectionListener(new TreeHandler());

    }


    public static ArrayList<String> getFolderNames(String filepath) {
        ArrayList<String> names = new ArrayList<String>();

        File allFolders = new File(filepath);
        String[] directories = allFolders.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isDirectory();
            }
        });

        for (String d : directories) {
            names.add(d);
        }

        return names;
    }

    public static ArrayList<String> getAllFileNames(String filepath) {
        ArrayList<String> names = new ArrayList<String>();

        File[] allFiles = new File(filepath).listFiles();

        for (File f : allFiles) {
            if (f.isFile()) {
                names.add(f.getName());
                System.out.println(f.getName());
            }
        }

        return names;

    }

    public static List<XWPFParagraph> getDocxParagraphs(String filename) {

        System.out.println(filename);

        File file = null;
        // XWPFWordExtractor ex = null;

        try {
            file = new File(filename);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            XWPFDocument doc = new XWPFDocument(fis);
            List<XWPFParagraph> parag = doc.getParagraphs();
            for (XWPFParagraph p : parag) {
                System.out.println(p.getText());

                System.out.println(p.getText());
                System.out.println(p.getAlignment());
                System.out.println(p.getRuns().size());
                System.out.println(p.getStyle());

                // Returns numbering format for this p, eg bullet or lowerLetter.
                System.out.println(p.getNumFmt());
                System.out.println(p.getAlignment());

                System.out.println(p.isWordWrapped());

                System.out.println("********************************************************************");
            }

            return parag;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
            /// System.out.println("Caught!");
        }
    }


    public void updateFromTreePath(TreePath path) {
        String ad = getHistory(pathInitial);
        String updatedCurrent = ad;
        for (int i = 0; i < path.getPathCount(); i++) {
            System.out.println(path.getPathComponent(i).toString());
            ad = ad + File.separatorChar + path.getPathComponent(i).toString();

            if (i == path.getPathCount()-1) {
                currentOpenFile = path.getPathComponent(i).toString();
            } else {
                updatedCurrent = updatedCurrent + path.getPathComponent(i).toString();
            }
        }

        currentFilePath = ad;
        currentDirectory = updatedCurrent;

    }

    public class TreeHandler implements TreeSelectionListener {
        public void valueChanged(TreeSelectionEvent e) {
            TreePath path = e.getPath();
            updateFromTreePath(path);

            File f = new File(currentFilePath);

            if (f.isFile()) {
                panelCenter.removeAll();
                List<XWPFParagraph> parag = getDocxParagraphs(currentFilePath);

                docEdit = new JTextArea(25, 50);
                panelCenter.add(docEdit);

                for (XWPFParagraph p : parag) {
                    docEdit.setText(p.getText());
                }

                docEdit.setLineWrap(true);
                docEdit.setWrapStyleWord(true);
                docEdit.setEditable(true);

                System.out.println(currentFilePath);
            }
        }
    }

    @Override
    public void windowClosing(WindowEvent evt) {
        System.exit(0);  // Terminate the program
    }
    @Override public void windowOpened(WindowEvent evt) { }
    @Override public void windowClosed(WindowEvent evt) { }
    // For Debugging
    @Override public void windowIconified(WindowEvent evt) { System.out.println("Window Iconified"); }
    @Override public void windowDeiconified(WindowEvent evt) { System.out.println("Window Deiconified"); }
    @Override public void windowActivated(WindowEvent evt) { System.out.println("Window Activated"); }
    @Override public void windowDeactivated(WindowEvent evt) { System.out.println("Window Deactivated"); }
}
