package metier.piece;

import java.util.ArrayList;
import java.util.List;

import metier.Grille;
import metier.Piece;

public class Cavalier extends Piece
{
	private static int nbPiece = 1;
	private List<Case> caseMenace;
	private Grille grille;
	private int num;
	
	public Cavalier(int x,int y, Grille grille)
	{
		super(x,y,grille,"C");
		this.grille = grille;
		this.caseMenace = new ArrayList<Case>();
		this.num = Cavalier.nbPiece++;
		if(this.num > 2)
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
		caseMenaceParCavalier();
	}

	public boolean deplacer(int nX,int nY)
	{
		int X = super.getX();
		int Y = super.getY();

		//si la nouvelle coordonnées Y est 3 case au dessus ou en dessus OU 1 case en dessous ou au dessus  
		if( nY == Y + 2 || nY == Y - 2 || nY == Y + 1 || nY == Y - 1 )
		{
			//si la nouvelle coordonnées X est a 3 case au dessus ou en dessous OU 1 case en dessous ou au dessus
			if(nX == X + 1 || nX == X + 2 || nX == X - 1 || nX == X - 2 )
			{
				//si le deplacement est possible
				if(this.grille.estOccupe(nX,nY))
				{
					if(this.grille.estDeMemeCouleur(nX, nY, this.getCouleur()))
						return false;
				}
				super.miseAjourModele(nX,nY,X,Y,super.getSymbole());
				super.setX(nX);
				super.setY(nY);
				this.caseMenaceParCavalier();
				return true; //retourne true si le déplacement est validé et que les nouvelles coordonnées sont enregistré
			}
		}
		System.out.println("deplacement impossible");
		return false;
	}



	public void caseMenaceParCavalier()
	{
		List<Case> nvCasesMenacees = new ArrayList<Case>();
		ArrayList<Case> chemin = new ArrayList<Case>();

		for (Case case1 : this.caseMenace) 
		{
			case1.setMenace(false,'c');
		}

		int X = super.getX();
		int Y = super.getY();

		int[] dx = {2, 2, 1, 1, -1, -1, -2, -2};
		int[] dy = {1, -1, 2, -2, 2, -2, 1, -1};

		for (int i = 0; i < 8; i++) 
		{
			int x = X + dx[i];
			int y = Y + dy[i];
		
			if (x >= 0 && x < 8 && y >= 0 && y < 8) 
			{
				if(this.grille.getPiece(x, y) instanceof Case)
				{
					chemin.add((Case)this.grille.getPiece(x, y));
				}

				if(this.grille.getPiece(x, y) instanceof Roi)
				{
					if(!this.grille.estDeMemeCouleur(x, y, getCouleur()))
					{
						super.setChemin(chemin);
						this.grille.setEchec(this,this.grille.getPiece(x, y).getCouleur(),(Roi)this.grille.getPiece(x, y));
					}
				}
				if (grille.estOccupe(x, y)) 
				{
					// Vérifier si la case n'est pas déjà occupée par une autre pièce
					break;
				}
				nvCasesMenacees.add((Case)this.grille.getGrillePiece()[x][y]);
			}
		}


		for (Case case1 : nvCasesMenacees) 
		{
			case1.setMenace(true,this.getCouleur());
		}

		this.caseMenace = nvCasesMenacees;
	}


}
