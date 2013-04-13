package br.com.madureira.process;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

/**
 * @author Danilo
 */
public class ImageHistogram extends Thread{

    //public final int HISTOGRAM = 1;

    private final BufferedImage img;
    private final WritableRaster wr;

    private int lut = 0;
    private int histogram[][] = new int[3][256];
    private int h[] = new int[256];
    private double hRelative[][] = new double[3][256];
    private double hAbsolute[][] = new double[3][256];

    public ImageHistogram(BufferedImage img){
        this.setPriority(Thread.MAX_PRIORITY);
        this.img = img;
        wr = img.getRaster();
        this.start();
    }

    @Override
    public synchronized void run(){
        rasterImage();
    }

    public int[] getImageHistogram(){
        int x = 0;
        for (int i = 0; i < img.getWidth() ; i++) {
            for (int j = 0; j < img.getHeight(); j++) {

                Color c = new Color(img.getRGB(i, j));

                x =(int) ( c.getRed() + c.getGreen() + c.getBlue() ) / 3;

                h[x]++;
            }
        }
        return h;
    }

    public int getMaxValue(int[] histogram){

        int x = 0;

        for (int i = 0; i < histogram.length; i++)
            if(histogram[i] > x) x = histogram[i];

        return x;
    }

    private void rasterImage(){
        synchronized(wr){            
            for (int i = 0; i < img.getWidth() ; i++) {
                for (int j = 0; j < img.getHeight(); j++) {

                    Color c = new Color(img.getRGB(i, j));

                    histogram[0][c.getRed()]++;
                    
                    histogram[1][c.getGreen()]++;
                    
                    histogram[2][c.getBlue()]++;
                }//end-for
            }//end-for
        }//end-synchronized

        getHistogramRelative(histogram);

        getHistogramAbsolute(hRelative);

        //this.saveNewImage("LUT");
    }

    private double[][] getHistogramRelative(int[][] histogram){

        int pixels = img.getWidth() * img.getHeight();

        for(int i = 0; i < histogram.length; i++){
            for(int j = 0; j < histogram[i].length; j++){
                hRelative[i][j] = (double) histogram[i][j] / pixels ;
            }//end-for
        }//end-for

        return hRelative;
    }

    private double[][] getHistogramAbsolute(double[][] hRelative){

        for (int i = 0; i < hRelative.length; i++) {

            hAbsolute[i][0] = hRelative[i][0];

            double acum = 0d;

            for (int j = 1; j < hRelative[i].length; j++) {

                acum += hRelative[i][j] + hRelative[i][j-1];

                hAbsolute[i][j] = acum;

            }//end-for
        }//end-for

        return hAbsolute;
    }

    private void saveNewImage(String functionUsed){
        try {
            ImageOutputStream out = new
                    FileImageOutputStream(
                        new File("out" + functionUsed + ".jpg"));
            ImageIO.write(img, "JPG", out );
            out.flush();
            out.close();
        }catch(FileNotFoundException ex){
            System.out.println("[EQImage][FileNotFoundException]"
                    + ex.getMessage());
        }
        catch (IOException ex) {
            System.out.println("[EQImage][IOException]" + ex.getMessage());
        }
    }
}