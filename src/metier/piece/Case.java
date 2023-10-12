package metier.piece;

import metier.Grille;
import metier.Piece;

public class Case extends Piece 
{
	private boolean estMenace;
	private char    couleurPiece;
	
	public Case(int X,int Y,Grille grille)
	{
		super(X,Y,grille,".");
		this.estMenace    = false;
		this.couleurPiece = 'c';
	}

	public boolean deplacer(int nX,int nY)
	{
		return true;
	}

	public char getCouleurMenace(){return couleurPiece;}

	public void activation(){}
	

	public boolean getMenace(){return this.estMenace;}

	public void setMenace(boolean estMenace,char couleur)
	{
		this.estMenace    = estMenace;
		this.couleurPiece = couleur;
	}

	public String toString()
	{
		return "La case " + this.getX() + " ; " + this.getY() + " est " + (this.estMenace ? "menacée":"pas menacée");
	}
}
