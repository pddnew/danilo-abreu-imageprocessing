package br.com.madureira.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class DesignPanel extends JPanel{

    BufferedImage bufferedImage;
    boolean msg = false;
	
    public DesignPanel(){
    }

    public JPanel panel(){
            return this;
    }

    public void repaint(BufferedImage bufferedImage){
        this.bufferedImage = bufferedImage;
        this.msg = true;
        super.repaint();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
               RenderingHints.VALUE_ANTIALIAS_ON );

        //g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        //        RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        if(msg) 
            g2.drawImage(createScaledImage(bufferedImage), null, this);
    }

    protected Image createScaledImage(BufferedImage image){

        Image img = null;

        double thumbRatio = (double) this.getWidth() /
                (double) this.getHeight();

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int IHeight = this.getHeight(), IWidth = this.getWidth();

        double imageRatio = (double) imageWidth / (double) imageHeight;


        if (thumbRatio < imageRatio) {
            IHeight = (int) (this.getWidth() / imageRatio);
        } else {
            IWidth = (int) (this.getHeight() * imageRatio);
        }

        if(image.getWidth() > this.getWidth() &&
                        image.getHeight() > this.getHeight()){
            img = image.getScaledInstance( IWidth,
                    IHeight, Image.SCALE_DEFAULT);
        }else{
            img = image.getScaledInstance( image.getWidth(),
                    image.getHeight(), Image.SCALE_DEFAULT);
        }

        img.setAccelerationPriority(1f);

        return img;
    }
}