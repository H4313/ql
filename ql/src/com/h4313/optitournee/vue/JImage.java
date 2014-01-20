package com.h4313.optitournee.vue;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import java.io.File;

import java.io.IOException;

import javax.imageio.ImageIO;

import javax.swing.JPanel;

/**
 * la classe JImage est une classe de gestion de 
 * l'affichage d'images dans l'IHM.
 *
 * @author H4403
 */
//Objet non serialise -> on n'a pas besoin de num�ro de version
@SuppressWarnings("serial")
public class JImage extends JPanel{
    private Image img;
    private Color fond=Color.WHITE;
    private Color borderColor=Color.WHITE;
    private int borderSize=0;

    public JImage(Image img) {
        this.img=img;
    }

    public JImage() {

    }
  public JImage(String img) {
        try {
            this.img=ImageIO.read(new File(img));
        } catch (IOException e) {
          System.out.println("Fichier inexistant");
          this.img=null;
        }
    }

    public void setSimpleBorder(Color color, double size) {
      borderColor=color;
      borderSize=(int)(size/2);
    }

    public void paint(Graphics g) {

     // super.paintComponent(g);
        g.setColor(borderColor);
        g.fillRect(0, 0, getWidth(), getHeight());
      if(img!=null)
      {
        g.drawImage(img,borderSize,borderSize, getWidth()-2*borderSize,getHeight()-2*borderSize,null);
      }else {
        g.setColor(fond);
        g.fillRect(borderSize, borderSize, getWidth()-2*borderSize, getHeight()-2*borderSize);
      }
      this.paintChildren(g);

    }

    public void setFond(Color color) {
      fond=color;
    }

    public void setImage(Image img) {
      this.img=img;
    }

  public void setImage(String img) {
    try {
        this.img=ImageIO.read(new File(img));
    } catch (IOException e) {
        System.out.println("Fichier inexistant");
        this.img=null;
    }
  }

}