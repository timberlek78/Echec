package metier.piece;

import java.util.ArrayList;

import metier.Grille;
import metier.Piece;

public class Roi extends Piece
{
	private static int nbPiece = 1;
	private Grille  grille;
	private int     num;
	private boolean aRoque;

	public Roi(int X, int Y,Grille grille)
	{
		super(X,Y,grille,"R");
		this.grille = grille;
		this.num    = nbPiece++;
		this.aRoque = true;
		if(this.num > 1)
		{
			this.setCouleur('N');
			this.grille.addPieceNoir(this);
		}
		else
		{
			this.setCouleur('B');
			this.grille.addPieceBlanche(this);
		}
	}

	public void activation()
	{
		casesAtteignablesPourRoi();
	}

	public boolean deplacer(int nX,int nY)
	{
		int X = super.getX();
		int Y = super.getY();

		int deltaX = Math.abs(X - nX);
		int deltaY = Math.abs(Y - nY);

		if(deltaY == 2 )
		{
			if(aRoque)
			{
				if(roque(X, Y, nX, nY))
				{
					aRoque = false;
					return true;
				}
				return false;
			}
			else
			{
				System.out.println("Le roque est impossible");
				return false;
			}
		}
		

		if(deltaX == 1 || deltaX == 0)
		{
			if(deltaY == 1 || deltaY == 0)
			{
				if(!this.grille.estOccupe(nX, nY))
				{
					if(this.grille.estDeMemeCouleur(nX, nY, getCouleur()))
					{
						System.out.println("je suis dans le if de meme couleur");
						return false;
					}
					else
					{
						if(this.grille.estCaseMenace(nX, nY) && this.grille.getPiece(nX, nY).getCouleurMenace() != this.getCouleur()) // si la case n'est pas menacée mais que Thomas lui-même est menacé
						{
							System.out.println("non la case est menacé");
							return false;
						}
						else
						{
							System.out.println("je suis dans le true");
							this.grille.pieceManger(this.grille.getPiece(nX, nY));
							this.majDeplacement(X, Y, nX, nY);
							aRoque = false;
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public void casesAtteignablesPourRoi() 
	{
		int roiX = this.getX(); // Position X actuelle du roi
		int roiY = this.getY(); // Position Y actuelle du roi

		ArrayList<Case> casesPossibles = new ArrayList<Case>();

		// Tableaux de déplacement relatifs pour un roi (8 directions possibles)
		int[] deltaX = {-1, -1, -1, 0, 0, 1, 1, 1};
		int[] deltaY = {-1, 0, 1, -1, 1, -1, 0, 1};

		for (int i = 0; i < 8; i++) {
			int x = roiX + deltaX[i];
			int y = roiY + deltaY[i];

			// Vérifiez si la case est valide (dans les limites de l'échiquier)
			if (x >= 0 && x < 8 && y >= 0 && y < 8) {
				// Vérifiez si la case est vide (pas de pièce)
				if (!this.grille.estOccupe(x, y)) {
					// Ajoutez la case à la liste des cases atteignables
					casesPossibles.add((Case)this.grille.getGrillePiece()[x][y]);
				}
			}
		}
		// Mettez à jour l'attribut this.caseMenace avec les cases atteignables
		super.setCaseMenace(casesPossibles);
	}

	public void majDeplacement(int X, int Y, int nX,int nY)
	{
		super.miseAjourModele(nX,nY,X,Y,super.getSymbole());
		super.setX(nX);
		super.setY(nY);
		super.majIHM();
		this.grille.estDeplacementOk();
		this.casesAtteignablesPourRoi();
	}

	public boolean roque(int X, int Y, int nX,int nY)
	{
		System.out.println(Y - nY);
		if( Y - nY == 2)
		{
			System.out.println(!(!this.grille.estOccupe(nX, Y + 1 ) && !this.grille.estOccupe(nX, Y + 2)));
			if((!this.grille.estOccupe(nX, Y - 1 ) && !this.grille.estOccupe(nX, Y - 2)))
			{
				if(this.grille.estCaseMenace(nX, nY))
					System.out.println("la case est menacée ");
				super.miseAjourModele(X,Y - 1,X,Y - 4, "T");
			}
			System.out.println("je suis dans le true");
			this.majDeplacement(X, Y, nX, nY);
			return true;
			

		}
		else if ( Y - nY == -2)
		{
			System.out.println((!this.grille.estOccupe(nX, Y + 1 ) && !this.grille.estOccupe(nX, Y + 2)));
			if((!this.grille.estOccupe(nX, Y + 1 ) && !this.grille.estOccupe(nX, Y + 2)))
			{
				if(this.grille.estCaseMenace(nX, nY)){
					System.out.println("la case est menacée ");
					return false;
				}
				super.miseAjourModele(X, Y + 1, X, Y+3, "T");
			}
			System.out.println("je suis dans le true");
			this.majDeplacement(X, Y, nX, nY);
			return true;
		}
		return false;
	}
}



/*
TODO: Pour savoir si les roi est echec et mat, je stocke le chemin par lequel la piece menace le roi.
je parcours toutes les cases sur lesquelle le roi peut se deplacer, si il n'y en a pas je parcours toutes les pièces de l'echequier,
si ils sont de la même couleur que le roi menacé alors je regarde leur deplacement possible si l'une des cases est presente parmis le chemin de la piece menacante
et bah je le dis (ou je ne fais rien on verra) sinon pas c'est échec et mat. */ 
