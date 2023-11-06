package metier;

import javax.swing.JOptionPane;

import metier.piece.Roi;

public class Jeu 
{
	private boolean echecEtMat;
	private Grille grille;
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
		boolean bOk = true;

		System.out.print("");
		if(this.grille.aPieceSelectionner())
		{	
			this.p = this.grille.getPieceSelect();



			if(this.grille.getEchec())
			{
				System.out.println("this.grille.peuventManger(p, this.grille.getPieceEchec()) : " + this.grille.peuventManger(p, this.grille.getPieceEchec()));
				if(!(this.p instanceof Roi && this.p.getCouleur() == this.grille.getCouleurEchec()) && !this.grille.peuventManger(p, this.grille.getPieceEchec()))
				{
					System.out.println("Vous etes en echec, vous devez jouer votre roi");
					// TODO: affichageRoiEchec
					bOk = false;
				}
			}

			if(p.getCouleur() == this.couleurTour && bOk)
			{
				if(!this.grille.aSelectDest()) 
					while(!this.grille.aSelectDest()){System.out.print("");} //si la destination est null alors on attend
			
				this.d = this.grille.getDestination();

				if(this.p != null && this.d != null)
					if(p.deplacer(d.getX(), d.getY()))
					{
						resetSelect();
					}
				
				if(this.grille.deplacementOK())
				{
					changementDeCouleur();
					this.grille.estDeplacementOk();
					this.grille.resetCaseMenace();
					this.grille.activation();

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
	}

	public void changementDeCouleur()
	{
		this.couleurTour = (this.couleurTour == 'N' ? 'B' : 'N');
	}
}
