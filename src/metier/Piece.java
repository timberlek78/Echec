package metier;

import java.util.ArrayList;

import metier.piece.Case;

public abstract class Piece 
{
	protected static String ANSI_YELLOW = "\u001B[33m";
	protected static ArrayList<Case> chemin;

	private boolean etat;
	private int x,y;
	private char couleur;
	private Grille grille;
	private String symbole;

	private ArrayList<Case>  caseMenace; 


	public Piece( int x, int y, Grille grille, String symbole )
	{
		this.x          = x;
		this.y          = y;
		this.grille     = grille;
		this.symbole    = symbole;
		Piece.chemin          = new ArrayList<>();
		this.caseMenace = new ArrayList<>();

	}

	public Piece( int x, int y,Grille grille)
	{
		this.x          = x;
		this.y          = y;
		this.grille     = grille;
		Piece.chemin          = new ArrayList<>();
		this.caseMenace = new ArrayList<>();

	}

	//méthode commune à toutes les classes filles
	public abstract boolean deplacer(int nvX,int nvY);
	public abstract void activation();

	public void miseAjourModele(int nX,int nY,int X,int Y, String symbole)
	{   
		String[][] grilleModele = this.grille.getGrilleModele();

		grilleModele[X][Y]   = ".";
		grilleModele[nX][nY] = symbole;
		
		this.grille.setGrilleModele(grilleModele);

		this.grille.miseAjourPiece(X,Y,nX,nY);
	}


	/* Getteur */
	public int 	            getX() 			 { return this.x;             }
	public int 	            getY() 		 	 { return this.y;             }
	public boolean          getEtat()  		 { return this.etat;          }
	public char             getCouleur()  	 { return this.couleur;       }
	public Grille           getGrille() 	 { return this.grille;        }
	public String           getSymbole()	 { return this.symbole;       }
	public ArrayList<Case>  getCaseMenace()  { return this.caseMenace;    }
	public char 		    getCouleurMenace(){return 'c';};
	public ArrayList<Case>  getChemin()      { return Piece.chemin;        }



	/* Setteur */
	public void setX      ( int x )              {this.x = x;        }
	public void setY      ( int y )              {this.y = y;        }
	public void setCouleur( char c)              {this.couleur = c  ;}
	public void setChemin (ArrayList<Case> chem) {Piece.chemin = chem;}
	public void setCaseMenace(ArrayList<Case> nvCaseMenace) { this.caseMenace = nvCaseMenace;}


	

	public boolean deplacementColonne(int nX,int nY, int X,int Y)
	{
		boolean bOk = false;
		if( nX == X)
		{
			//on parcours toutes les cases jusqu'a la destination souhaité
			System.out.println("je suis dans le X");
			for (int y = Y+1; y < nY; y++) 
			{
				//si l'une des cases est occupé alors le deplacement est impossible
				if(this.grille.estOccupe(nX,y))
				{
					System.out.println("deplacement impossible");
					return false;
				}
			}
			bOk = true;
		}

		//si le déplacement est sur la ligne 
		if(nY == Y)
		{
			System.out.println("je suis dans le Y");
			for (int x = X + 1; x < nX; x++) 
			{
				if(this.grille.estOccupe(x,nY))
				{
					System.out.println("deplacement impossible");
					return false;
				}
			}
			bOk = true;
		} 


		return bOk;
	}

	public boolean deplacementDiag(int nX, int nY,int X,int Y)
	{
		// Vérifier si les coordonnées de départ et d'arrivée sont valides
		if (X < 0 || X > 7 || Y < 0 || Y > 7 || nX < 0 || nX > 7 || nY < 0 || Y > 7) 
		{
			System.out.println("deplacement impossible, case de départ invalide");
			return false;
		}
	
		// Calculer la différence entre les coordonnées de départ et d'arrivée
		int deltaX = Math.abs(nX - X);
		int deltaY = Math.abs(nY - Y);
	
		// Le déplacement est valide si la différence est la même sur les deux axes (en diagonale)
		if (deltaX != deltaY) 
		{
			System.out.println("deplacement impossible ----");
			return false;
		}
	
		// Vérifier s'il y a des pièces sur la trajectoire
		int directionX = (nX - X) > 0 ? 1 : -1;
		int directionY = (nY - Y) > 0 ? 1 : -1;
		int x = X + directionX;
		int y = Y + directionY;
	
		while (x != nX && y != nY) {
			if (this.grille.estOccupe(x, y)) 
			{
				// Il y a une pièce sur la trajectoire
				System.out.println("deplacement impossible, piece sur la trajectoire ");
				return false;
			}
			x += directionX;
			y += directionY;
		}
		return true;
	}


	public String toString()
	{
		return "" + this.symbole + " x : "+ this.getX()+" y: "+ this.getY(); 
	}

}
