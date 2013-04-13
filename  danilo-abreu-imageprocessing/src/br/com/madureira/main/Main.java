package br.com.madureira.main;

import java.io.IOException;
import br.com.madureira.gui.ShowImage;
import javax.swing.JOptionPane;


public class Main {
	
    public static void main(String args[]){
        try {

            ShowImage frame = new ShowImage();
            frame.setVisible(true);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            System.out.print("[]:" + ex.getCause().getLocalizedMessage());
        }catch(Exception ex){
            System.out.print("[]:" + ex.getCause().getLocalizedMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
}