import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Assignment1 {
    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Brendan Ryder - Assignment 1");
        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;

        // This block configure the logger with handler and formatter
        fh = new FileHandler("C:/temp/test/MyLogFile.log", true);
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        JButton btn = new JButton("Open File Explorer");
        JButton btn1 = new JButton("Load File to Text Area");

        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileHandler fh = null;

                // This block configure the logger with handler and formatter
                try {
                    fh = new FileHandler("C:/temp/test/MyLogFile.log", true);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                logger.addHandler(fh);
                SimpleFormatter formatter = new SimpleFormatter();
                fh.setFormatter(formatter);
                JFileChooser jFileChooser = new JFileChooser();
                logger.info("File Explorer Open");
                int result = jFileChooser.showOpenDialog(new JFrame());
                File selectedFile = jFileChooser.getSelectedFile();
                if (result == JFileChooser.APPROVE_OPTION) {

                    logger.info("Selected file: " + selectedFile.getAbsolutePath());
                }
                btn1.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        FileHandler fh = null;

                        // This block configure the logger with handler and formatter
                        try {
                            fh = new FileHandler("C:/temp/test/MyLogFile.log", true);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        logger.addHandler(fh);
                        SimpleFormatter formatter = new SimpleFormatter();
                        fh.setFormatter(formatter);
                        logger.info("Loading File . . .");
                        DataInputStream in = null;
                        try {
                            in = new DataInputStream(new FileInputStream(selectedFile.getAbsolutePath()));
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }
                        String line = null;
                        try {
                            line = in.readLine();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        long start = System.nanoTime();
                        while (line != null) {
                            //System.out.println(line);
                            textArea.append(line + "\n");
                            try {
                                line = in.readLine();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                        long end = System.nanoTime();
                        long res = (end - start) / 1000000;
                        logger.info("File Loaded");
                        logger.info("Time to display file contents: " + (res) + "ms");
                        fh.close();
                        try {
                            in.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        fh.close();
                    }
                });
                fh.close();
            }
        });

        frame.add(textArea, BorderLayout.CENTER);
        frame.add(btn, BorderLayout.NORTH);
        frame.add(btn1, BorderLayout.SOUTH);
        JScrollPane scroll = new JScrollPane (textArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        frame.add(scroll);
        frame.setVisible (true);
        fh.close();
    }
}