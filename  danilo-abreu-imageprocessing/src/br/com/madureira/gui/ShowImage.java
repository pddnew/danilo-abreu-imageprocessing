package br.com.madureira.gui;

import br.com.madureira.process.EqualizeImage;
import br.com.madureira.process.ImageHistogram;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ShowImage extends JFrame{

    private JFileChooser fileChooser = this.createChooser();
    BufferedImage image = null;
    private JFrame me = this;
    JPanel panel1, panel2;
    JLabel info = new JLabel("Load an image file!");
    protected int coefficient;

    public ShowImage() throws IOException {

        super(".:.::.. EQUALIZER OF HISTOGRAM .:.::..");        

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(800, 600);

        Container c = getContentPane();

        JPanel masterPanel = new JPanel(new BorderLayout());

        panel1 = new DesignPanel();

        panel2 = new PrintImageHistogram();       

        //ImageIcon icon = new ImageIcon();
        //c.add(panel1, BorderLayout.NORTH);
        //c.add(new JScrollPane((Component)panel1));
        //c.add(new JScrollPane((Component)panel2));

        masterPanel.add( panel1 , BorderLayout.CENTER);

        masterPanel.add( panel2 , BorderLayout.LINE_END);

        c.add(masterPanel);

        c.add(this.createMenu(), BorderLayout.NORTH);

        c.add(info, BorderLayout.SOUTH);
    }


    private JMenuBar createMenu(){
        JMenuBar menuBar = new JMenuBar();        

        JMenu file = new JMenu("File");
        JMenuItem loadItem = new JMenuItem("Load File");        

        loadItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int option = fileChooser.showDialog(me, "Load File");
                if(option == JFileChooser.APPROVE_OPTION){
                    try {
                        image = ImageIO.read(fileChooser.getSelectedFile());

                        DesignPanel p1 = (DesignPanel) panel1;

                        p1.repaint(image);
                        
                        PrintImageHistogram p2 = (PrintImageHistogram) panel2;
                        
                        ImageHistogram imgHistogram = new ImageHistogram(image);
                                                
                        p2.repaint(imgHistogram.getImageHistogram(),
                                imgHistogram.getMaxValue(imgHistogram.getImageHistogram()));

                        info.setText("Dimensions: " +
                                image.getWidth() + " x " + image.getHeight()
                                +" Bands: " + image.getRaster().getNumBands());
                    } catch (Exception ex) {
                        System.out.println("[ShowImage][ImageIO]"
                            + ex.getMessage());
                        JOptionPane.showMessageDialog(null,
                                " Load has Failed!\n" +
                                " Probably the selected image isn't Image File");                        
                    }
                }
            }
        });
        
        file.add(loadItem);

        JMenu equalizer = new JMenu("Specification");

        JMenuItem linear = new JMenuItem("Linear");
        linear.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                if(image != null){
                    JFrame frame = new MyJSlider(1, 5, EqualizeImage.LINEAR,
                            1);
                    DesignPanel p1 = (DesignPanel) panel1;
                    
                    p1.repaint(image);
                }
            }
        });
        equalizer.add(linear);

        JMenuItem logarithmic = new JMenuItem("Logarithmic");
        logarithmic.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if(image != null){
                    EqualizeImage eI = new EqualizeImage(image,
                            EqualizeImage.LOGARITHMIC);
                    DesignPanel p1 = (DesignPanel) panel1;
                    p1.repaint(image);

                    PrintImageHistogram p2 = (PrintImageHistogram) panel2;

                    ImageHistogram imgHistogram = new ImageHistogram(image);

                    p2.repaint(imgHistogram.getImageHistogram(),
                            imgHistogram.getMaxValue(imgHistogram.getImageHistogram()));
                }
            }
        });
        equalizer.add(logarithmic);

        JMenuItem exponential = new JMenuItem("Exponential");
        exponential.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if(image != null){
                    EqualizeImage eI = new EqualizeImage(image,
                            EqualizeImage.EXPONENTIAL);
                    DesignPanel p1 = (DesignPanel) panel1;

                    p1.repaint(image);
                    
                    PrintImageHistogram p2 = (PrintImageHistogram) panel2;

                    ImageHistogram imgHistogram = new ImageHistogram(image);

                    p2.repaint(imgHistogram.getImageHistogram(),
                            imgHistogram.getMaxValue(imgHistogram.getImageHistogram()));

                }
            }
        });
        equalizer.add(exponential);

        JMenuItem squareRoot = new JMenuItem("Square Root");
        squareRoot.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if(image != null){
                    EqualizeImage eI = new EqualizeImage(image,
                            EqualizeImage.SQUARE_ROOT);
                    DesignPanel p1 = (DesignPanel) panel1;
                    
                    p1.repaint(image);

                    PrintImageHistogram p2 = (PrintImageHistogram) panel2;

                    ImageHistogram imgHistogram = new ImageHistogram(image);

                    p2.repaint(imgHistogram.getImageHistogram(),
                            imgHistogram.getMaxValue(imgHistogram.getImageHistogram()));
                }
            }
        });
        equalizer.add(squareRoot);

        JMenuItem power = new JMenuItem("Power");
        power.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(image != null){
                    JFrame frame = new MyJSlider(1, 32, EqualizeImage.POWER, 3);
                    
                    DesignPanel p1 = (DesignPanel) panel1;

                    p1.repaint(image);
                }
            }
        });
        equalizer.add(power);

        JMenu histogram = new JMenu("Equalization");

        JMenuItem appHistogram = new JMenuItem("Apply histogram");

        appHistogram.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(null, "testing");
                if(image != null){

                    ImageHistogram imgHistogram = new ImageHistogram(image);
                    
                    DesignPanel p1 = (DesignPanel) panel1;

                    p1.repaint(image);
                }
            }
        });

        histogram.add(appHistogram);

        menuBar.add(file); menuBar.add(equalizer); menuBar.add(histogram);

        return menuBar;
    }

    private JFileChooser createChooser(){
        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("LOAD AN IMAGE FILE");
        javax.swing.filechooser.FileFilter filter = new GenericFilter();
        fileChooser.setFileFilter(filter);
        return fileChooser;
    }

    protected class GenericFilter extends
            javax.swing.filechooser.FileFilter{

        public boolean accept(File pathname) {
            return ( pathname.getName().endsWith(".jpg") ||
                    pathname.getName().endsWith(".JPG") ||
                    pathname.getName().endsWith(".png") ||
                    pathname.getName().endsWith(".PNG") ||
                    pathname.getName().endsWith(".jpeg") ||
                    pathname.getName().endsWith(".JPEG") ||
                    pathname.getName().endsWith(".bmp") ||
                    pathname.getName().endsWith(".BMP") ) ;
        }

        @Override
        public String getDescription() {
            return "Image Files *.JPG, *.PNG, *.BMP";
        }
    }

    protected class MyJSlider extends JFrame{

        JFrame frame;
        JSlider slider;
        JLabel label;
        JButton button;
        protected MyJSlider(int min, int max, final int pOption, int tickSpacing){
            frame = new JFrame();
            
            slider = new JSlider();

            slider.setMajorTickSpacing(tickSpacing);

            slider.setPaintTicks(true);

            slider.setPaintLabels(true);

            slider.setValue((min + max)/2);

            slider.setMinimum(min);

            slider.setMaximum(max);

            slider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    label.setText(Integer.toString(slider.getValue()));
                }
            });

            label = new JLabel(Integer.toString(slider.getValue()));

            button = new JButton("OK");

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    coefficient = slider.getValue();

                    if(image != null){
                        EqualizeImage eI = new EqualizeImage(image,
                            pOption , coefficient);

                        PrintImageHistogram p2 = (PrintImageHistogram) panel2;

                        ImageHistogram imgHistogram = new ImageHistogram(image);

                        p2.repaint(imgHistogram.getImageHistogram(),
                            imgHistogram.getMaxValue(imgHistogram.getImageHistogram()));
                    }
                }
            });

            JPanel panel = new JPanel();

            panel.add(slider);

            panel.add(label);

            panel.add(button);

            frame.add(panel, BorderLayout.CENTER);

            frame.setSize(350, 150);

            frame.setResizable(false);

            frame.setVisible(true);
        }
    }
}