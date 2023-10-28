package metier;

import java.util.Scanner;

import metier.piece.Roi;

public class Jeu 
{
	private boolean echecEtMat;
	private Grille grille;
	private String[] str;
	private int[] deplace;
	private Scanner sc;


	public Jeu(Grille grille) 
	{
		this.echecEtMat = false;
		this.grille = grille;
		this.sc = new Scanner (System.in);
		this.deplace = new int[4];
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

		System.out.println();
		if(this.grille.aPieceSelectionner())
		{
			Piece p = this.grille.getPieceSelect();

			if(p.getCouleur() == ensJoueurs[valeur].getCouleur())
			{
				if(!this.grille.aSelectDest()) 
				while(!this.grille.aSelectDest()){System.out.println();} //si la destination est null alors on attend
			
				Piece d = this.grille.getDestination();

				p.deplacer(d.getX(), d.getY());

				p = null;
				d = null;
				this.grille.pieceSelect(false);
				this.grille.destSelect(false);
			}
		}

	

	// 	System.out.println("C'est au joueur " + ensJoueurs[valeur].getCouleur() + " de jouer.");
	// 	System.out.print("Choissisez la pièce que vous voulez jouer !");
	// 	demandeUtilisateur('P');

		
	// 	if(this.grille.getPiece(this.deplace[0], this.deplace[1]).getCouleur() != ensJoueurs[valeur].getCouleur())
	// 	{
	// 		while(this.grille.getPiece(this.deplace[0], this.deplace[1]).getCouleur() != ensJoueurs[valeur].getCouleur())
	// 		{
	// 			System.out.println("C'est au joueur " + ensJoueurs[valeur].getCouleur()+ " de jouer.");
	// 			System.out.print("Choissisez la pièce que vous voulez jouer ! ");
	// 			demandeUtilisateur('P');
	// 		}
	// 	}

	// 	if(this.grille.getEchec())
	// 	{
	// 		while(!(this.grille.getPiece(this.deplace[0], this.deplace[1]) instanceof Roi))
	// 		{
	// 			System.out.println("Vous êtes en echec, veuillez deplacer votre roi ");
	// 			System.out.print("Choissisez la pièce que vous voulez jouer ! ");
	// 			demandeUtilisateur('P');
	// 		}
	// 	}

	// 	while(!estValide(this.deplace[0], this.deplace[1]))
	// 	{
	// 		System.out.print("Choissisez la piece que vous voulez jouer (X,Y) :");
	// 		demandeUtilisateur('P');
	// 	}
		
	// 	System.out.print("Où voulez vous la déplacer ? (X,Y) :");
	// 	demandeUtilisateur('D');
		
	// 	while(!this.grille.getGrillePiece()[this.deplace[0]][this.deplace[1]].deplacer(this.deplace[2], this.deplace[3]))
	// 	{
	// 		System.out.print("Où voulez vous la déplacer ? (X,Y) (while) :");
	// 		demandeUtilisateur('D');
	// 	}
	// 	System.out.println(this.grille);

	
	// }

	// public boolean estValide(int x, int y)
	// {

	// 	if(x >= 0 && x < 8 && y >= 0 && y < 8)
	// 	{
	// 		if(this.grille.estOccupe(x, y))
	// 		{
	// 			return true;
	// 		}
	// 		System.out.println("Il n'y a pas de pièce à la case sélectionnée.");
	// 		return false;
	// 	}
	// 	System.out.println("Les coordonnées que vous avez rentré ne sont pas valide, elles doivent être entre 0 et 7");
	// 	return false;
	}


	public void demandeUtilisateur(char action)
	{
		str = this.sc.nextLine().split(",");
 
		if(action == 'P')
		{
			this.deplace[0] = Integer.parseInt(str[0]);
			this.deplace[1] = Integer.parseInt(str[1]);
		}
		else
		{
			this.deplace[2] = Integer.parseInt(str[0]);
			this.deplace[3] = Integer.parseInt(str[1]);
		}
	}
	
}


			

			

			