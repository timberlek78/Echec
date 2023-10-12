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

    public String[][] getGrilleModele(){return this.metier.getGrilleModele();}



    public static void main(String[] args) 
    {
        System.out.println("je suis dans ce main");
        Controleur ctrl = new Controleur();
        System.out.println(ctrl.metier);
    }
}
