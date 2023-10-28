package metier.piece;

import java.util.ArrayList;
import java.util.List;

import metier.Grille;
import metier.Piece;

public class Fou extends Piece
{
	private static int nbPiece = 1;
	private int num;
	private Grille grille;
	private List<Case> caseMenace;

	public Fou(int X, int Y, Grille grille)
	{
		super(X,Y,grille,"F");
		this.grille = grille;
		this.caseMenace = new ArrayList<Case>();
		this.num = Fou.nbPiece++;
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

	public boolean deplacer(int nX,int nY)
	{
		int X = super.getX();
		int Y = super.getY();


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
			System.out.println("deplacement impossible");
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
				if(this.grille.estDeMemeCouleur(x, y, this.getCouleur()))
					return false;
			}
			x += directionX;
			y += directionY;
		}
		
		super.miseAjourModele(nX,nY,X,Y,super.getSymbole());
		super.setX(nX);
		super.setY(nY);
		casesMenaceesParFou();
		this.grille.majIHM();
		return true;	
	}

	public void activation()
	{
		casesMenaceesParFou();
	}

	public void casesMenaceesParFou() 
	{ 
		List<Case> nvCasesMenacees = new ArrayList<>();
		ArrayList<Case> chemin = new ArrayList<Case>();

		for (Case case1 : this.caseMenace) 
		{
			case1.setMenace(false,'c');
		}

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
		for (Case case1 : nvCasesMenacees) 
		{
			case1.setMenace(true,this.getCouleur());
		}

		this.caseMenace = nvCasesMenacees;
	}
}
