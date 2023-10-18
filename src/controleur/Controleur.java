package controleur;

import metier.Grille;
import metier.Jeu;
import vue.FrameJeu;

public class Controleur 
{
    private Grille   metier;
    private FrameJeu ihm;
    private Jeu      partie;


    public Controleur()
    {
        
        this.metier = new Grille  (this);
        this.ihm    = new FrameJeu(this);
        this.partie = new Jeu(this.metier);
        
        this.partie.partie();
    }

    public String[][] getGrilleModele(){return this.metier.getGrilleModele();}

    public void setPieceSelect(int coordX,int coordY){this.metier.setPieceSelect(coordX,coordY);}
    public void setDestination(int coordX,int coordY){this.metier.setDestination(coordX,coordY);}



    public static void main(String[] args) 
    {
        Controleur ctrl = new Controleur();
    }
}
