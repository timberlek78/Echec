package metier.piece;

import java.rmi.server.SocketSecurityException;
import java.util.ArrayList;
import java.util.List;

import metier.Grille;
import metier.Piece;

public class Pion extends Piece
{
	private static int nbPiece = 1;
	private List<Case> caseMenace;
	private boolean aJoue = false; //par défault le pions n'a pas été joué
	private Grille grille;
	private int num;

	public Pion(int X,int Y,Grille grille)
	{
		super(X,Y,grille,"P");
		this.grille = grille;
		this.caseMenace = new ArrayList<Case>();
		this.num = Pion.nbPiece++;
		if(this.num > 8)
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
		caseMenaceParPion();
	}

	public boolean deplacer(int nX, int nY) 
	{
		int X = super.getX();
		int Y = super.getY();
	
		if (Math.abs(nX - X) > 2)
			return false;
	
		boolean attaqueValide = (Math.abs(nX - X) == 1 && Math.abs(nY - Y) == 1);

		if(attaqueValide)
			if (nX > X && this.getCouleur() == 'B' || nX < X && this.getCouleur() == 'N')
				if (this.grille.estOccupe(nX, nY)){
					if (!this.grille.estDeMemeCouleur(nX, nY, this.getCouleur())) 
					{
						this.grille.pieceManger(this.grille.getPiece(nX, nY));
						if(this.getCouleur() == 'B')
							this.grille.removePieceBlanche(this.grille.getPiece(nX, nY));
						else
							this.grille.removePieceNoir(this.grille.getPiece(nX, nY));
						confirmationDeplacement(nX, nY, X, Y);
						return true;
					}
				}

		if (Math.abs(nX - X) == 2)
			if (!aJoue)
				if (!this.grille.estOccupe(nX, nY)) {
					confirmationDeplacement(nX, nY, X, Y);
					aJoue = true;
					return true;
				}
			
		if (Math.abs(nX - X) == 1 && nY == Y) {
			if ((this.getCouleur() == 'B' && nX - X == 1) || (this.getCouleur() == 'N' && nX - X == -1)) {
				if (!this.grille.estOccupe(nX, nY)) {
					confirmationDeplacement(nX, nY, X, Y);
					aJoue = true;
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	public void confirmationDeplacement(int nX,int nY,int X,int Y)
	{
		super.miseAjourModele(nX,nY,X,Y,super.getSymbole());
		super.setX(nX);
		super.setY(nY);
		super.majIHM();
		this.grille.estDeplacementOk();
		// this.caseMenaceParPion();
	}

	public void caseMenaceParPion() {
		ArrayList<Case> nvCaseMenace = new ArrayList<Case>();
		ArrayList<Case> chemin = new ArrayList<Case>();
	
		int x = super.getX();
		int y = super.getY();
	
		for (Case case1 : this.caseMenace) {
			case1.setMenace(false, 'c');
		}
	
		boolean estPieceBlanche = (this.getCouleur() == 'B');
		int targetX = estPieceBlanche ? x + 1 : x - 1;
	
		if (targetX >= 0 && targetX < 8 && y - 1 >= 0 && y + 1 < 8) {
			if (this.grille.getPiece(x, y) instanceof Case) {
				chemin.add((Case) this.grille.getPiece(x, y));
			}
			
			Piece piece1 = this.grille.getPiece(targetX, y - 1);
			Piece piece2 = this.grille.getPiece(targetX, y + 1);
			
			if (piece1 instanceof Roi || piece2 instanceof Roi) 
			{
				if (this.grille.estDeMemeCouleur(targetX, y - 1, this.getCouleur())) 
				{
					this.grille.setEchec(this, this.grille.getPiece(x, y).getCouleur(), (Roi) piece1);
				} 	 
				else if (this.grille.estDeMemeCouleur(targetX, y + 1, this.getCouleur())) 
				{
					this.grille.setEchec(this, this.grille.getPiece(x, y).getCouleur(), (Roi) piece2);
				}	
				System.out.println("territobite"); 
			} 
	
			if (!this.grille.estOccupe(targetX, y - 1) && !this.grille.estOccupe(targetX, y + 1)) {
				nvCaseMenace.add((Case) this.grille.getGrillePiece()[targetX][y - 1]);
				nvCaseMenace.add((Case) this.grille.getGrillePiece()[targetX][y + 1]);
			} else {
				super.ajoutPieceMenace(piece1);
				super.ajoutPieceMenace(piece2);
			}
		}
	
		for (Case case1 : nvCaseMenace) {
			case1.setMenace(true, this.getCouleur());
		}
	
		this.caseMenace = nvCaseMenace;
	}
}
