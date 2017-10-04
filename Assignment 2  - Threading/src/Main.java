import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) throws IOException {

        Date date = new Date();
        System.out.println(date.toString());
        JFrame frame = new JFrame("Brendan Ryder - Assignment 2");
        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;

        // This block configure the logger with handler and formatter
        fh = new FileHandler("C:/temp/test/MyLogFile.log", true);
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(850, 650);
        frame.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        JTextArea textArea1 = new JTextArea();
        JButton LoadBtn = new JButton("Load File to Text Area");
        JButton zipBtn = new JButton("Zip");

        // This block configure the logger with handler and formatter

        JFileChooser jFileChooser = new JFileChooser();
        logger.info("File Explorer Open");
        int result = jFileChooser.showOpenDialog(new JFrame());
        File selectedFile = jFileChooser.getSelectedFile();
        if (result == JFileChooser.APPROVE_OPTION) {

            logger.info("Selected file: " + selectedFile.getAbsolutePath());
        }
        SwingWorker load = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                textArea.setText("");
                // This block configure the logger with handler and formatter

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

                //Read each line of the file, until line = null
                while (line != null) {
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
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                return null;
            }
        };
        SwingWorker a = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                return null;
            }
        };

        SwingWorker zip = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {

                String zipFilePath = "C:\\Users\\Brendan\\Desktop\\testZipped.zip";
                try {

                    // Wrap a FileOutputStream around a ZipOutputStream
                    FileOutputStream fileOutputStream = new FileOutputStream(zipFilePath);
                    ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

                    // a ZipEntry represents a file entry in the zip archive
                    // Name the ZipEntry after the original file's name
                    ZipEntry zipEntry = new ZipEntry(selectedFile.getName());
                    logger.info("Zipping " + zipEntry + " to " + zipFilePath + " . . .");
                    zipOutputStream.putNextEntry(zipEntry);

                    FileInputStream fileInputStream = new FileInputStream(selectedFile);
                    byte[] buf = new byte[1024];
                    int bytesRead;

                    // Read the input file by chucks of 1024 bytes
                    // and write the read bytes to the zip stream
                    while ((bytesRead = fileInputStream.read(buf)) > 0) {
                        zipOutputStream.write(buf, 0, bytesRead);
                    }

                    logger.info("Successfully Compressed " + zipEntry + " to " + zipFilePath );

                    // close ZipEntry to store the stream to the file
                    zipOutputStream.closeEntry();

                    zipOutputStream.close();
                    fileOutputStream.close();

                    double uncompressedSize = zipEntry.getSize();
                    double compressedSize = zipEntry.getCompressedSize();
                    double percentRed = (compressedSize / uncompressedSize) * 100;
                    percentRed = (100 - percentRed);

                    DecimalFormat dc = new DecimalFormat("0.00");
                    String formattedText = dc.format(percentRed);

                    textArea1.setText("");
                    textArea1.append("\t\t\n" + "Zip File Created: " + date.toString());
                    textArea1.append("\t\n" + "Original File Size: " + uncompressedSize + " bytes");
                    textArea1.append("\t\n" + "Zipped File Size: " + compressedSize + " bytes");
                    textArea1.append("\t\n" + "Percentage Reduction: " + formattedText + "%");


                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        //GUI button to load selected file
        LoadBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                load.execute();
            }
        });

        //GUI button to ZIP selected file
        zipBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                zip.execute();
            }
        });

        //Add text area and buttons to GUI, make GUI visable.
        frame.add(textArea, BorderLayout.CENTER);
        frame.add(textArea1,BorderLayout.EAST);
        frame.add(LoadBtn, BorderLayout.SOUTH);
        frame.add(zipBtn, BorderLayout.WEST);
        JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        frame.add(scroll);
        frame.setVisible(true);
    }
}
