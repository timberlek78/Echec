package metier.piece;

import java.util.ArrayList;
import java.util.List;

import metier.Grille;
import metier.Piece;

public class Tour extends Piece
{
	private static int nbPiece = 1;
	private Grille grille;
	private int num;
	private List<Case> caseMenace;
	
	public Tour(int X, int Y,Grille grille)
	{
		super(X,Y,grille,"T");
		this.grille = grille;
		this.caseMenace = new ArrayList<Case>();
		this.num = Tour.nbPiece++; 
		if(this.num > 2)
		{
			this.setCouleur('B');
			this.grille.addPieceBlanche(this);		}
		else
		{
			this.setCouleur('N');
			this.grille.addPieceNoir(this);
		}
	}

	public void activation()
	{
		casesMenaceesParTour();
	}

	public boolean deplacer(int nX,int nY)
	{
		int X = super.getX();
		int Y = super.getY();
		boolean bOk = true;


		if(this.grille.estOccupe(nX, nY))// Si la destination est occupée
		{
			if(this.grille.estDeMemeCouleur(nX, nY, this.getCouleur()))//si elle est occupée par une piece de m^me couleur
			{
				bOk = false;
				return false; //le déplacement est invalide
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
		//si le déplacement est sur la colonne
		if( nX == X)
		{
			//on parcours toutes les cases jusqu'a la destination souhaité
			for (int y = Y+1; y < nY; y++) 
			{
				//si l'une des cases est occupé
				if(this.grille.estOccupe(nX,y))
				{
					bOk = false;
				}
			}
		}

		//si le déplacement est sur la ligne 
		if(nY == Y)
		{
			// On parcours toutes les cases jusqu'a la destination souhaité
			for (int x = X + 1; x < nX; x++) 
			{
				if(this.grille.estOccupe(x,nY))
				{
					bOk = false;
				}
			}
		} 
		

		if(bOk)
		{
			confirmationDeplacement(nX, nY, X, Y);
			return bOk;
		}
		else
		{
			System.out.println("Deplacement impossible -- ");
		}
		return bOk;
		
	}


	public void confirmationDeplacement(int nX, int nY, int X,int Y)
	{
			super.miseAjourModele(nX,nY,X,Y,super.getSymbole());
			super.setX(nX);
			super.setY(nY);
			super.majIHM();
			this.grille.estDeplacementOk();
			// this.casesMenaceesParTour();
	}

	public void casesMenaceesParTour() {
		ArrayList<Case> nvCaseMenace = new ArrayList<Case>();
		ArrayList<Case> chemin = new ArrayList<Case>();
		boolean estEchec = false;
		int xEchec = 0;
		int yEchec = 0;
	
		for (Case case1 : this.caseMenace) {
			case1.setMenace(false,'c');
		}
	
		int[] deltaX = {-1,1};
		int[] deltaY = {1,-1};

		for (int dir = 0; dir < 2; dir++) {
			int x = super.getX();
			int y = super.getY();
			while (true) {
				x += deltaX[dir];
				if (x < 0 || x >= 8)
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
						estEchec = true;
						xEchec = x;
						yEchec = y;
					}
				}
				else
				{
					super.ajoutPieceMenace(this.grille.getPiece(x, y));
					break;
				}
					
					
				if(this.grille.getPiece(x, y) instanceof Case)
					nvCaseMenace.add((Case) this.grille.getGrillePiece()[x][y]);
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
						estEchec = true;
						xEchec = x;
						yEchec = y;
						
					}
				}
				else
				{
					this.grille.setEchec();
					super.ajoutPieceMenace(this.grille.getPiece(x, y));
					break;
				}
				if(this.grille.getPiece(x, y) instanceof Case)
					nvCaseMenace.add((Case) this.grille.getGrillePiece()[x][y]);
			}
		}

		
		this.caseMenace = nvCaseMenace;

		for (Case case1 : this.caseMenace) 
		{
			case1.setMenace(true,this.getCouleur());
		}

		if(estEchec) this.grille.setEchec(this,this.grille.getPiece(xEchec, yEchec).getCouleur(),(Roi)this.grille.getPiece(xEchec, yEchec));

	}	
}
