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
		this.caseMenaceParPion();
	}

	public void caseMenaceParPion()
	{
		ArrayList<Case> nvCaseMenace = new ArrayList<Case>();
		ArrayList<Case> chemin = new ArrayList<Case>();

		int x = super.getX();
		int y = super.getY();

		for (Case case1 : this.caseMenace) 	
		{
			case1.setMenace(false,'c');
		}

		if(this.getCouleur() == 'B')
		{
			if(x + 1 < 8 && y - 1 > 0 && y + 1 < 8 )
			{		
				if(this.grille.getPiece(x, y) instanceof Case)
				{
					chemin.add((Case)this.grille.getPiece(x, y));
				}		
				if(this.grille.getPiece(x + 1, y - 1) instanceof Roi || this.grille.getPiece(x+1, y+1) instanceof Roi)
				{
					if(this.grille.estDeMemeCouleur(x + 1, y - 1, this.getCouleur()))
					{
						System.out.println("je rentre ici aussi mais je ne sais pas pourquoi ");
						super.setChemin(chemin);
						this.grille.setEchec(this,this.grille.getPiece(x, y).getCouleur(),(Roi)this.grille.getPiece(x + 1, y - 1));
					}
					else if(this.grille.estDeMemeCouleur(x +1 , y + 1, this.getCouleur()))
					{
						System.out.println("je rentre ici mais je ne comprend vrm pas poruqoi ");
						super.setChemin(chemin);
						this.grille.setEchec(this,this.grille.getPiece(x, y).getCouleur(),(Roi)this.grille.getPiece(x + 1, y + 1));
					}
				}
				if(!this.grille.estOccupe(x + 1, y - 1) && !this.grille.estOccupe(x+1,y+1))
				{
					nvCaseMenace.add((Case)this.grille.getGrillePiece()[x+1][y-1]);
					nvCaseMenace.add((Case)this.grille.getGrillePiece()[x+1][y+1]);
				}
			} 
		}
		else
		{
			if(x - 1 >= 0 && y - 1 > 0 && y + 1 < 8 )
			{
				if(this.grille.getPiece( x - 1, y - 1 ) instanceof Roi || this.grille.getPiece( x - 1, y + 1 ) instanceof Roi)
				{
					if(this.grille.estDeMemeCouleur( x - 1, y - 1, this.getCouleur() ))
					{
						this.grille.setEchec(this,this.grille.getPiece(x, y).getCouleur(),(Roi)this.grille.getPiece(x, y));
					}
					else if(this.grille.estDeMemeCouleur(x - 1 , y + 1, this.getCouleur()))
					{
						this.grille.setEchec(this,this.grille.getPiece(x, y).getCouleur(),(Roi)this.grille.getPiece(x, y));
					}
				}

				if(!this.grille.estOccupe(x - 1, y - 1) && !this.grille.estOccupe(x-1,y+1))
				{
					nvCaseMenace.add((Case)this.grille.getGrillePiece()[x-1][y-1]);
					nvCaseMenace.add((Case)this.grille.getGrillePiece()[x-1][y+1]);
				}
			} 
		}

		for (Case case1 : nvCaseMenace) 
		{
			case1.setMenace(true,this.getCouleur());
		}

		this.caseMenace = nvCaseMenace;
	}
}
