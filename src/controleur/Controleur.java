package controleur;

import metier.Grille;
import vue.FrameJeu;

public class Controleur 
{
    private Grille   metier;
    private FrameJeu ihm;


    public Controleur()
    {
        this.metier = new Grille();
        this.ihm    = new FrameJeu(this);
    }

    public char[] getGrilleModele(){return this.getGrilleModele();}



    public static void main(String[] args) 
    {
        new Controleur();
    }
}
