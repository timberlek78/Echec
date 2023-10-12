package vue;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controleur.Controleur;

public class FrameJeu extends JFrame
{
    private final int HEIGHT;
    private final int WIDTH;
    private JPanel panelPlateau;
    private Controleur ctrl;



    public FrameJeu (Controleur ctrl)
    {
        Dimension taille = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        this.HEIGHT = (int)taille.getHeight();
        this.WIDTH  = (int)taille.getWidth ();

        this.ctrl = ctrl;

        this.setLayout(new BorderLayout());
        this.setTitle("Échec et mât'Hair !");
        this.setSize    ( this.WIDTH,this.HEIGHT - 50 );
		this.setLocation(  0, 0 );

        /* Création des composants */

        this.panelPlateau = new PanelPlateau(this);

        /* Positionnement des composants */

        this.add(this.panelPlateau);


        this.setVisible(true);

    }

    public int getHeight(){return this.HEIGHT;}
    public int getWidth (){return this.WIDTH ;}
}
