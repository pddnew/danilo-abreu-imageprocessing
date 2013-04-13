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

public class EqualizeImage extends Thread{

    public static final int LINEAR = 1;
    public static final int LOGARITHMIC = 2;
    public static final int EXPONENTIAL = 3;
    public static final int SQUARE_ROOT = 4;
    public static final int POWER = 5;

    /**
     * Atributo Objeto representante da imagem
     */
    private final BufferedImage img;
    private int option = 1;
    private float coefficient = 0.5f;
    private final WritableRaster wr;

    public EqualizeImage(BufferedImage img, int option, float coefficient){
        this(img, option, false);
        this.startUpThread(coefficient);
    }

    private EqualizeImage(BufferedImage img, int option, boolean bool){
        this.setPriority(Thread.MAX_PRIORITY);
        this.img = img;
        this.option = option;
        wr = img.getRaster();
        if(bool == true)
            this.startUpThread();
    }

    public EqualizeImage(BufferedImage img, int option){
        this(img, option, true);
    }

    private void startUpThread(){
        this.start();
    }

    private void startUpThread(float coefficient){
        this.coefficient = coefficient;
        this.start();
    }
	
    @Override
    public void run(){
        switch(option){
            case 1 :
                rasterImageWithLinear(coefficient); break;
            case 2 :
                rasterImageWithLogarithmic(); break;
            case 3 :
                rasterImageWithExponential(); break;
            case 4 :
                rasterImageWithSquareRoot(); break;
            case 5 :
                rasterImageWithPower(coefficient); break;
        }
    }

    private void rasterImageWithLinear( float b){
        
        synchronized(wr){

            for (int i = 0; i < img.getWidth() ; i++) {
                for (int j = 0; j < img.getHeight(); j++) {
                    Color c = new Color(img.getRGB(i, j));

                    int red = (int) b * c.getRed();
                    wr.setSample(i, j, 0, red > 255 ? 255 : red);

                    int green = (int) b * c.getGreen();
                    wr.setSample(i, j, 1, green > 255 ? 255 : green);

                    int blue = (int) b * c.getBlue();
                    wr.setSample(i, j, 2, blue > 255 ? 255 : blue);
                }
            }
        }
        this.saveNewImage("Linear"+b);
    }

    private void rasterImageWithLogarithmic(){

        double lambda = 255 / Math.log10(256);

        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight() ; j++) {
                Color c = new Color(img.getRGB(i, j));

                wr.setSample(i, j, 0, lambda * Math.log10(c.getRed() + 1));

                wr.setSample(i, j, 1, lambda * Math.log10(c.getGreen() + 1));

                wr.setSample(i, j, 2, lambda * Math.log10(c.getBlue() + 1));
            }
        }
        this.saveNewImage("Logarithmic");
    }

    private void rasterImageWithExponential(){
        double w = 255 / Math.log(256);               

        for (int i = 0; i < img.getWidth() ; i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                Color c = new Color(img.getRGB(i, j));

                wr.setSample(i, j, 0, Math.exp(c.getRed() / w) - 1);

                wr.setSample(i, j, 1, Math.exp(c.getGreen() / w) - 1);

                wr.setSample(i, j, 2, Math.exp(c.getBlue() / w) - 1);
            }
        }
        this.saveNewImage("Exponential");
    }

    private void rasterImageWithSquareRoot(){
        double p = 255 / Math.sqrt(255);
    

        for (int i = 0; i < img.getWidth() ; i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                Color c = new Color(img.getRGB(i, j));

                wr.setSample(i, j, 0, (int) p * Math.sqrt(c.getRed()));

                wr.setSample(i, j, 1, (int) p * Math.sqrt(c.getGreen()));

                wr.setSample(i, j, 2, (int) p * Math.sqrt(c.getBlue()));
            }
        }
        this.saveNewImage("SquareRoot");
    }

    private void rasterImageWithPower(float exp){
        synchronized(img){
            double p = Math.pow(255, 1 - ((int)exp));
            
            int red = 0, green = 0, blue = 0;

            for (int i = 0; i < img.getWidth() ; i++) {
                for (int j = 0; j < img.getHeight(); j++) {
                    Color c = new Color(img.getRGB(i, j));

                    red = (int) ( p * Math.pow(c.getRed(), exp));
                    wr.setSample(i, j, 0, red > 255 ? 255 : red);

                    green = (int) ( p * Math.pow(c.getGreen(), exp));
                    wr.setSample(i, j, 1, green > 255 ? 255 : green);

                    blue = (int) ( p * Math.pow(c.getBlue(), exp));
                    wr.setSample(i, j, 2, blue > 255 ? 255 : blue);
                }
            }
        }
        this.saveNewImage("Power"+(int)exp);
    }

    public BufferedImage getBufferedImage(){
        synchronized(img){
            return this.img;
        }
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