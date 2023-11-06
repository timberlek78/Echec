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

	public boolean deplacer(int nX, int nY) {
		int X = super.getX();
		int Y = super.getY();
	
		int deltaX = Math.abs(nX - X);
		int deltaY = Math.abs(nY - Y);
	
		if ((deltaX == 2 && deltaY == 1) || (deltaX == 1 && deltaY == 2)) {
			// Vérifiez si la case cible est occupée par une pièce de couleur différente
			if (this.grille.estOccupe(nX, nY)) {
				if (this.grille.estDeMemeCouleur(nX, nY, this.getCouleur())) 
				{
					return false;
				}
				else
				{
					this.grille.pieceManger(this.grille.getPiece(nX, nY));
					if(this.getCouleur() == 'B')
						this.grille.removePieceBlanche(this.grille.getPiece(nX, nY));
					else
						this.grille.removePieceNoir(this.grille.getPiece(nX, nY));
				}
			}
			confirmationDeplacement(nX, nY, X, Y);
			return true;
		}
	
		return false;
	}

	public void confirmationDeplacement(int nX,int nY, int X, int Y )
	{
			super.miseAjourModele(nX,nY,X,Y,super.getSymbole());
			super.setX(nX);
			super.setY(nY);
			super.majIHM();
			this.grille.estDeplacementOk();
			// this.caseMenaceParCavalier();
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

		boolean caseTraitee = false; // Indicateur pour vérifier si la case a déjà été traitée

		for (int i = 0; i < 8; i++) {
			int x = X + dx[i];
			int y = Y + dy[i];
			
			caseTraitee = false; // Réinitialisez l'indicateur pour chaque nouvelle case

			if (x >= 0 && x < 8 && y >= 0 && y < 8) {
				// Vérifiez si la case a déjà été traitée
				for (Case case1 : nvCasesMenacees) 
				{
					// System.out.println("case courante : " + this.grille.getPiece(x, y));
					// System.out.print("case a tester : " + case1);
					if (case1.getX() == x && case1.getY() == y)
					{
						caseTraitee = true;
						break;
					}
				}
				
				if (!caseTraitee)
				{
					if (this.grille.getPiece(x, y) instanceof Case) {
						chemin.add((Case) this.grille.getPiece(x, y));
					}

					if (this.grille.getPiece(x, y) instanceof Roi) {
						if (!this.grille.estDeMemeCouleur(x, y, getCouleur())) {
							System.out.println("-------------- je rentre 2 fois ici -----------------------" + this.getSymbole() + " : "+ this.getCouleur());
							super.setChemin(chemin);
							this.grille.setEchec(this, this.grille.getPiece(x, y).getCouleur(), (Roi) this.grille.getPiece(x, y));
						}
					}
					if (grille.estOccupe(x, y)) {
						super.ajoutPieceMenace(this.grille.getPiece(x, y));
						break;
					}
					nvCasesMenacees.add((Case) this.grille.getGrillePiece()[x][y]);
				}
			}
		}

		for (Case case1 : nvCasesMenacees) 
		{
			case1.setMenace(true,this.getCouleur());
		}

		this.caseMenace = nvCasesMenacees;
	}


}
