package metier.piece;

import java.util.ArrayList;

import metier.Grille;
import metier.Piece;

public class Reine extends Piece
{
	private static int nbPiece = 1;
	private Grille grille;
	private int num;

	public Reine(int X,int Y,Grille grille)
	{
		super(X,Y,grille,"D");
		this.grille = grille;
		this.num = Reine.nbPiece++;
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
		caseMenaceParReine();
	}

	public boolean deplacer(int nX,int nY)
	{
		int X = super.getX();
		int Y = super.getY();
		if(super.deplacementColonne(nX, nY, X, Y) || super.deplacementDiag(nX, nY, X, Y))
		{
			if(this.grille.estOccupe(nX, nY))
			{
				if(this.grille.estDeMemeCouleur(nX, nY, this.getCouleur()))
					return false;
			}
			super.miseAjourModele(nX,nY,X,Y,super.getSymbole());
			super.setX(nX);
			super.setY(nY);
			caseMenaceParReine();
		}
		return true;
	}


	public void caseMenaceParReine()
	{
		ArrayList<Case> nvCasesMenacees = new ArrayList<>();
		ArrayList<Case> chemin = new ArrayList<Case>();

		for (Case case1 : super.getCaseMenace()) 
		{
			case1.setMenace(false,'c');
		}

		//activation des diagonales

		int[] dx = {1, -1, 1, -1}; // Déplacements horizontaux (diagonales)
		int[] dy = {1, 1, -1, -1}; // Déplacements verticaux (diagonales)

		for (int i = 0; i < 4; i++) {
			int x = super.getX();
			int y = super.getY();

			while (true) {
				x += dx[i];
				y += dy[i];


				if (x < 0 || x >= 8 || y < 0 || y >= 8) {
					break; // Sortir de la boucle si hors de l'échiquier
				}	


				if(this.grille.getPiece(x, y) instanceof Case)
				{
					chemin.add((Case)this.grille.getPiece(x, y));
				}
				
				if(this.grille.getPiece(x,y) instanceof Roi)
				{
					if(!this.grille.estDeMemeCouleur(x, y, this.getCouleur()))
					{
						System.out.println("je rentre la dans le echec fou");
						super.setChemin(chemin);
						// this.grille.deplacementCommun(this.grille.getPiece(x, y).getCouleur(),this,chemin);
						this.grille.setEchec(this,this.grille.getPiece(x, y).getCouleur(),(Roi)this.grille.getPiece(x, y));
						break;
					}
				}
				else
				{
					break;
				}

				// Vérifier si une pièce est présente à la nouvelle position
				// Si c'est le cas, arrêter de parcourir dans cette direction
				

				// chemin.clear();

				if(this.grille.getPiece(x, y) instanceof Case)
					nvCasesMenacees.add((Case)this.grille.getGrillePiece()[x][y]);
			}
		}

		//activation des colonnes

		int[] deltaX = {-1,1};
		int[] deltaY = {1,-1};

		for (int dir = 0; dir < 2; dir++) {
			int x = super.getX();
			int y = super.getY();
			while (true) {
				x += deltaX[dir];
				if (x < 0 || x >= 8)
					break;
				if (this.grille.estOccupe(x, y)) 
					break;
				
				nvCasesMenacees.add((Case) this.grille.getGrillePiece()[x][y]);
			}
	
			x = super.getX(); // Réinitialisez x à la position initiale
			y = super.getY(); // Réinitialisez y à la position initiale
	
			while (true) {
				y += deltaY[dir];
				if (y < 0 || y >= 8)
					break;

			
				if(this.grille.getPiece(x, y) instanceof Case)
				{
					chemin.add((Case)this.grille.getPiece(x, y));
				}

				if(this.grille.getPiece(x,y) instanceof Roi)
				{
					if(!this.grille.estDeMemeCouleur(x, y, this.getCouleur()))
					{
						super.setChemin(chemin);
						this.grille.setEchec(this,this.grille.getPiece(x, y).getCouleur(),(Roi)this.grille.getPiece(x, y));
					}
				}
				if (this.grille.estOccupe(x, y))
					break;
				nvCasesMenacees.add((Case) this.grille.getGrillePiece()[x][y]);
			}
		}

		for (Case case1 : nvCasesMenacees) 
		{
			case1.setMenace(true,this.getCouleur());
		}

		super.setCaseMenace(nvCasesMenacees);
	}
}
