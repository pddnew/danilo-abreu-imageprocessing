package br.com.madureira.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 * @author danilo.abreu
 */
public class PrintImageHistogram extends JPanel{

    private int[] histogram;
    private boolean msg = false;
    private int maxValue = 0;

    public PrintImageHistogram(/*int[] histogram*/){
        //this.histogram = histogram;
        //super.repaint();
    }

    public void repaint(int[] histogram, int maxValue){
        this.histogram = histogram;
        this.maxValue = maxValue;
        msg = true;
        super.repaint();
    }

    public JPanel panel(){
        return this;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponents(g);

        if(msg){
            Graphics2D g2 = (Graphics2D)g;

            g2.setColor(Color.LIGHT_GRAY);
            g2.fill3DRect(0, 0, getWidth(), getHeight(), true);
            g2.setFont(new java.awt.Font(Font.SANS_SERIF, 10, 10) );
            g2.setPaint(Color.BLACK);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON );

            int x = 10; int y = (int)this.getHeight() / 2 + 200;

            float scale = ((float)( y - 100 )/ maxValue );

            for(int i = 0; i < histogram.length; i++){
                g2.draw3DRect(x+i, y - (int)( histogram[i] * scale),
                        1,
                        (int)(histogram[i] * scale), true);
            }
        }
    }
}