package metier.piece;

import java.util.ArrayList;

import metier.Grille;
import metier.Piece;

public class Case extends Piece 
{
	private boolean estMenace;
	private ArrayList<Character>    couleurPiece;
	private Grille grille;
	
	public Case(int X,int Y,Grille grille)
	{
		super(X,Y,grille," ");
		this.estMenace    = false;
		this.couleurPiece = new ArrayList<Character>();
		this.grille = grille;
		this.grille.addEnsCase(this);
	}

	public boolean deplacer(int nX,int nY)
	{
		return true;
	}

	public void activation(){}

	public boolean rechercheCouleurMenace(char couleur) // couleur de la piece qui menace
	{
		System.out.println(this.couleurPiece);
		for (int i = 0; i <this.couleurPiece.size(); i++) 
			if(this.couleurPiece.get(i) == couleur){
				return true; //la case est menacé par une piece de la couleur de la piece qui menace
			}
		return false;
	}
	
	public void resetCouleurMenace()
	{
		this.couleurPiece = new ArrayList<>();
	}

	public boolean getMenace(){return this.estMenace;}

	public void setMenace(boolean estMenace,char couleur)
	{
		this.estMenace    = estMenace;

		if(couleur != 'c')
			this.couleurPiece.add(couleur);
	}

	public String toString()
	{
		return "La case " + this.getX() + " ; " + this.getY() + " est " + (this.estMenace ? "menacée par une case " + this.getCouleurMenace() : "pas menacée") ;
	}
}
