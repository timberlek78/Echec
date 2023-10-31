package metier;

import java.util.Scanner;


public class Jeu 
{
	private boolean echecEtMat;
	private Grille grille;
	private String[] str;
	private char couleurTour;
	// private int[] deplace;
	// private Scanner sc;


	public Jeu(Grille grille) 
	{
		this.echecEtMat = false;
		this.grille = grille;
		// this.sc = new Scanner (System.in);
		// this.deplace = new int[4];
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
			Piece p = this.grille.getPieceSelect();

			if(p.getCouleur() == this.couleurTour)
			{
				System.out.println("je rentre quand meme ici");
				if(!this.grille.aSelectDest()) 
					while(!this.grille.aSelectDest()){System.out.print("");} //si la destination est null alors on attend
			
				Piece d = this.grille.getDestination();

				p.deplacer(d.getX(), d.getY());

				p = null;
				d = null;
				this.grille.pieceSelect(false);
				this.grille.destSelect(false);

				if(this.grille.deplacementOK()){
					changementDeCouleur();
					this.grille.estDeplacementOk();
				}
			}
		}
	}

	public void changementDeCouleur()
	{
		this.couleurTour = this.couleurTour == 'N' ? 'B' : 'N';
		this.grille.changementDeCouleur(this.couleurTour);
		System.out.println(this.couleurTour);
	}


	// public void demandeUtilisateur(char action)
	// {
	// 	str = this.sc.nextLine().split(",");
 
	// 	if(action == 'P')
	// 	{
	// 		this.deplace[0] = Integer.parseInt(str[0]);
	// 		this.deplace[1] = Integer.parseInt(str[1]);
	// 	}
	// 	else
	// 	{
	// 		this.deplace[2] = Integer.parseInt(str[0]);
	// 		this.deplace[3] = Integer.parseInt(str[1]);
	// 	}
	// }
	
}


			

			

			