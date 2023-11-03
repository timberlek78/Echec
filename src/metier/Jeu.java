package metier;

import javax.swing.JOptionPane;

import metier.piece.Case;

public class Jeu 
{
	private boolean echecEtMat;
	private Grille grille;
	private String[] str;
	private char couleurTour;
	private Piece p;
	private Piece d;


	public Jeu(Grille grille) 
	{
		this.echecEtMat = false;
		this.grille = grille;
		this.couleurTour = 'B';
	}


	public boolean isEchecEtMat() {return echecEtMat;}
	public Grille getGrille() {return grille;}


	public void setEchecEtMat(boolean echecEtMat) {this.echecEtMat = echecEtMat;}

	public void partie()
	{
		int cpt = 0;
		boolean alterner = true;
		Joueur[] ensJoueurs = {new Joueur('B'),new Joueur('N')};

		while(true)
		{
			boucleDeJeu(ensJoueurs,alterner);
			alterner = !alterner;
			cpt++;
		}

	}

	public void boucleDeJeu(Joueur[] ensJoueurs,boolean alterner) 
	{
		int valeur = alterner ? 0:1;

		System.out.print("");
		if(this.grille.aPieceSelectionner())
		{
			this.p = this.grille.getPieceSelect();

			if(this.p instanceof Case)
				resetSelect();


			if(p.getCouleur() == this.couleurTour)
			{
				if(!this.grille.aSelectDest()) 
					while(!this.grille.aSelectDest()){System.out.print("");} //si la destination est null alors on attend
			
				this.d = this.grille.getDestination();

				if(this.p != null && this.d != null)
					if(p.deplacer(d.getX(), d.getY()))
					{
						resetSelect();
					}
				
				if(this.grille.deplacementOK()){
					changementDeCouleur();
					this.grille.estDeplacementOk();
				}
			}
			else
			{
				resetSelect();
			}
		}
	}

	public void resetSelect()
	{
		this.p = null;
		this.d = null;
		this.grille.pieceSelect(false);
		this.grille.destSelect(false);
		System.out.println("toute est clear");
	}

	public void changementDeCouleur()
	{
		this.couleurTour = (this.couleurTour == 'N' ? 'B' : 'N');
	}
}


			

			

			