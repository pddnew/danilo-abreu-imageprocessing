package br.com.madureira.gui;

import br.com.madureira.process.EqualizeImage;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Danilo
 */
public class MyMenu extends Thread{

    JMenuBar menuBar;

    private JFileChooser fileChooser = this.createChooser();
    private BufferedImage image = null;
    private JFrame me = null;
    private JPanel panel1 = null;
    private JLabel info = null;
    private int coefficient = 1;

    /**
     * Contrói o Menu
     *
     * @param img Parametro BufferedImage
     * @param who Parametro JFrame
     * @param panel Parametro JPanel
     * @param infoLabel Parametro JLabel
     */
    public MyMenu(BufferedImage img, JFrame who, JPanel panel, JLabel infoLabel){
        this.setPriority(MAX_PRIORITY);
        this.image =  img;
        this.me = who;
        this.panel1 = panel;
        this.info = infoLabel;
        this.start();
    }

    @Override
    public void run(){
        this.createMenu();
    }

    public JMenuBar getMenu(){
        return menuBar;
    }

    private void createMenu(){

        menuBar = new JMenuBar();

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

        JMenu equalizer = new JMenu("Equalizer");

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

        menuBar.add(file); menuBar.add(equalizer);        
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
                    }
                }
            });

            JPanel panel = new JPanel();

            panel.add(slider);

            panel.add(label);

            panel.add(button);

            frame.add(panel, BorderLayout.CENTER);

            frame.setSize(250, 150);

            frame.setResizable(false);

            frame.setVisible(true);
        }
    }
}
