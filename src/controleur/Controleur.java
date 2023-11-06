package controleur;

import metier.Grille;
import metier.Jeu;
import metier.Piece;
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

	public Piece[][] getGrillePiece(){return this.metier.getGrillePiece();}

	public void majIHM(){this.ihm.majIHM();}
	public void pieceManger(Piece p){this.ihm.pieceManger(p);}
	public void setPieceSelect(int coordX,int coordY){this.metier.setPieceSelect(coordX,coordY);}
	public void setDestination(int coordX,int coordY){this.metier.setDestination(coordX,coordY);}

	public static void main(String[] args) 
	{
		Controleur ctrl = new Controleur();
	}
}
