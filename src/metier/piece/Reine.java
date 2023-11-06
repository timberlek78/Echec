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
				else
					this.grille.pieceManger(this.grille.getPiece(nX, nY));
					if(this.getCouleur() == 'B')
						this.grille.removePieceBlanche(this.grille.getPiece(nX, nY));
					else
						this.grille.removePieceNoir(this.grille.getPiece(nX, nY));
			}
			confirmationDeplacement(nX, nY, X, Y);
		}
		return true;
	}

	public void confirmationDeplacement(int nX,int nY,int X,int Y)
	{
		super.miseAjourModele(nX,nY,X,Y,super.getSymbole());
		super.setX(nX);
		super.setY(nY);
		super.majIHM();
		this.grille.estDeplacementOk();
		// caseMenaceParReine();

	}


	public void caseMenaceParReine() {
		ArrayList<Case> nvCasesMenacees = new ArrayList<>();
		ArrayList<Case> chemin = new ArrayList<Case>();

		super.resetPieceMenace();
	
		for (Case case1 : super.getCaseMenace()) {
			case1.setMenace(false, 'c');
		}
	
		int[][] directions = {
			{1, 0}, {-1, 0}, {0, 1}, {0, -1},
			{1, 1}, {1, -1}, {-1, 1}, {-1, -1}
		};
	
		for (int[] direction : directions) {
			int dx = direction[0];
			int dy = direction[1];
	
			int x = super.getX();
			int y = super.getY();
	
			while (true) 
			{
				x += dx;
				y += dy;
	
				if (x < 0 || x >= 8 || y < 0 || y >= 8) {
					break;
				}
	
				if (this.grille.getPiece(x, y) instanceof Case) {
					chemin.add((Case) this.grille.getPiece(x, y));
				}
	
				if (this.grille.getPiece(x, y) instanceof Roi) {
					if (!this.grille.estDeMemeCouleur(x, y, this.getCouleur())) {
						super.setChemin(chemin);
						this.grille.setEchec(this, this.grille.getPiece(x, y).getCouleur(), (Roi) this.grille.getPiece(x, y));
					}
				}
	
				if (this.grille.estOccupe(x, y)) 
				{
					super.ajoutPieceMenace(this.grille.getPiece(x, y));
					break;
				}
	
				nvCasesMenacees.add((Case) this.grille.getGrillePiece()[x][y]);
			}
		}
	
		for (Case case1 : nvCasesMenacees) {
			case1.setMenace(true, this.getCouleur());
		}

		// System.out.print(this.getCouleur() + " : ");
		// System.out.println(nvCasesMenacees);
	
		super.setCaseMenace(nvCasesMenacees);
	}



}
