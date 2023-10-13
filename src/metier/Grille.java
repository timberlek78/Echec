package metier;

import java.util.ArrayList;

import controleur.Controleur;
import metier.piece.Case;
import metier.piece.Cavalier;
import metier.piece.Fou;
import metier.piece.Pion;
import metier.piece.Reine;
import metier.piece.Roi;
import metier.piece.Tour;

public class Grille 
{
	//grille modèle pour que les pièces est les bonnes coordonnées
	String[][] grilleModele = {{"P", "C", "F", "D", "R", "F", "C", "T"},
							   {"T", "P", "P", "P", "P", "P", "P", "P"},
							   {" ", " ", " ", " ", " ", " ", " ", " "},
							   {" ", " ", " ", " ", " ", " ", " ", " "},
							   {" ", " ", " ", " ", " ", " ", " ", " "},
							   {" ", " ", " ", " ", " ", " ", " ", " "},
							   {"P", "P", "P", "P", "P", "P", "P", "P"},
							   {"T", "C", "F", "D", "R", "F", "C", "T"}};

	// String[][] grilleModele = {{"T", ".", ".", ".", "R", ".", ".", "T"},
	//                            {"P", "P", "P", "P", "P", "P", "P", "P"},
	// 	                       {"F;", "F", ".", ".", ".", ".", ".", "."},
	// 						   {".", ".", ".", ".", ".", ".", ".", "F"},
	// 						   {".", ".", "F", ".", ".", ".", ".", "."},
	// 						   {".", ".", ".", ".", ".", ".", ".", "."},
	// 						   {"P", "P", "P", "P", "P", "P", "P", "P"},
	// 						   {"T", ".", ".", ".", "R", ".", ".", "T"}};

	private Piece[][] grillePiece;
	private boolean echec;
	private ArrayList<Piece> pieceBlanche;
	private ArrayList<Piece> pieceNoir;
	private Controleur ctrl;

	public Grille(Controleur ctrl)
	{
		this.grillePiece = new Piece[8][8];
		this.echec = false;
		this.pieceBlanche = new ArrayList<>();
		this.pieceNoir    = new ArrayList<>();
		this.ctrl = ctrl;
		creationGrillePiece();	
		activation();
	}


	/*Getteur */
	public String[][] getGrilleModele    ()  { return this.grilleModele;      }
	public Piece [][] getGrillePiece     ()  { return this.grillePiece;       }
	public boolean    getEchec           ()  { return this.echec;             }
	public Piece      getPiece(int x,int y)  { return this.grillePiece[x][y]; }
	public ArrayList<Piece> getPieceNoir()   { return this.pieceNoir;         }
	public ArrayList<Piece> getPieceBlanche(){ return this.pieceBlanche;      }    

	

	/*Setteur */
	public void setGrilleModele(String[][] nvGrille){this.grilleModele = nvGrille;}

	public void addPieceNoir   (Piece p){this.pieceNoir   .add(p);}
	public void addPieceBlanche(Piece p){this.pieceBlanche.add(p);}

	public void removePieceNoir   (Piece p){this.pieceNoir.remove(p)   ;}
	public void removePieceBlanche(Piece p){this.pieceBlanche.remove(p);}
	
	public void setEchec       (Piece p,char couleur,Roi r) //piece qui met le roi en echec, couleur c'est la couleur du roi
	{
		this.echec = true;

		if(estEchecEtMat(p,couleur,r))
			System.out.println("Eh oh tu es echec et mat gros malin !!!!");           
	}

	//verifie si le roi est échec et mat 
	public boolean estEchecEtMat(Piece p,char couleur,Roi r)
	{

		System.out.println("Couleur estEchecEtMat = " + couleur);
		ArrayList<Case> chemin = p.getChemin();

		boolean peutCouvrir    = peutCouvrir(p,couleur,chemin);
		boolean peutDeplacer   = peutDeplacer(r);

		System.out.println("peutCouvrir = " + peutCouvrir);
		System.out.println("peutDeplacer = " + peutDeplacer);

		if(peutCouvrir || peutDeplacer)
		{
			System.out.println("true");
			return false;
		}
		else
		{
			System.out.println("false");
			return true;
		}
	}

	//verifie si une pièce peut venir couvrir l'echec 
	public boolean peutCouvrir(Piece p,char couleur, ArrayList<Case> chemin) // pièce qui met le roi en echec - couleur du roi - chemin depuis lequel le roi est en echec
	{
		if(couleur == 'B') //si le roi en echec est blanc
		{
			for (Piece p1 : this.getPieceBlanche()) //on parcours toutes les pièces blanches encore présentes dans le jeu
			{
				if(!p1.equals(p)) // si la pièce p1 n'est pas le roi
					if(deplacementCommun( p1, chemin )) //si p1 a un déplacement en commun
						return true;//il peut alors venir couvrir l'echec
			}
			return false;
		}
		else
		{
			for (Piece p1 : this.getPieceNoir()) 
			{
				if(!p1.equals(p))
					if(!(p1 instanceof Roi))
						if(deplacementCommun( p1, chemin ))
							return true;
			} 
			return false;
		}
	}

	//verifie si le roi peut se déplacer
	public boolean peutDeplacer(Roi r)
	{
		ArrayList<Case> deplacementPossible = r.getCaseMenace();

		for (Case case1 : deplacementPossible) //parcours toutes les cases ou le déplacement est possible (pas occupé)
		{
			if(!case1.getMenace()) // si une des cases n'est pas menacée
			{
				return true; //le roi peut alors se déplacer
			}		
		}
		return false;
	}

	//verifie si la pièce p en argument, peut venir se déplacé sur le chemin en argument 
	public boolean deplacementCommun(Piece p, ArrayList<Case> chemin)// p est la pièce dans le même camp que le roi
	{
		ArrayList<Case> deplacementPiece = p.getCaseMenace(); //prend les cases menacées par la pièce de la même couleur 

		for (Case case1 : deplacementPiece) 
		{
			if(chemin.contains(case1)) //si un des déplacements possibles de la pièce de meme couleur est dans le chemi par lequel la pièce met en echec le roi.
			{
				return true; //alors la pièce peut couvrir le roi donc pas echec et mat	
			}
		}
		return false;
	}

	//verification des cases
	public boolean estOccupe(int nX,int nY)
	{
		if(this.grilleModele[nX][nY] == " ") { return false; } //si la case n'est pas occupé 
		return true;
	}

	//verifie si la pièce au coordonnées passer en paramètre est de la même couleur que la couleur passé en paramètre
	public boolean estDeMemeCouleur(int nX, int nY, char couleur)
	{
		if(this.grillePiece[nX][nY].getCouleur() == couleur){return true;}
		return false;
	}
	//verifie que la case est menacé par une pièce, retourne true si elle est menacé.
	public boolean estCaseMenace(int X,int Y)
	{
		if ( this.grillePiece[X][Y] instanceof Case )
		{
			Case case1 = ( Case ) this.grillePiece[X][Y];
			return case1.getMenace();
		}
		return false;
	}

	//mise a jour de la grille
	public void miseAjourPiece(int X,int Y,int nX,int nY)
	{
		Piece tmp = this.grillePiece [X] [Y];

		this.grillePiece[X][Y] = new Case( X, Y, this);
		this.grillePiece[nX][nY] = tmp;
	}
	//activation des cases menacées par toutes les pièces
	public void activation()
	{
		for (int i = 0; i < grillePiece.length; i++) 
			for (int j = 0; j < grillePiece[i].length; j++) 
				this.grillePiece[i][j].activation();
	}
	//initialisation de la grille 
	public void creationGrillePiece()
	{
		for (int i = 0; i < grilleModele.length; i++) 
		{
			for (int j = 0; j < grilleModele[i].length; j++) 
			{
				switch(this.grilleModele[i][j])
				{
					case "P":
						this.grillePiece[i][j] = new Pion(i, j,this);
						break;
					case "T":
						this.grillePiece[i][j] = new Tour(i, j, this);
						break;
					case "F":
						this.grillePiece[i][j] = new Fou(i, j , this);
						break;
					case "D":
						this.grillePiece[i][j] = new Reine(i, j , this);
						break;
					case "R":
						this.grillePiece[i][j] = new Roi(i, j , this);
						break;
					case "C":
						this.grillePiece[i][j] = new Cavalier(i, j ,this);
						break;
					default:
						this.grillePiece[i][j] = new Case(i,j,this);
						break;
				}
			}
		}
	}

	public String toString() 
	{
		String affi = "";
		for (int i = 0; i < grilleModele.length; i++) 
		{
			affi += "+---+---+---+---+---+---+---+---+" + "\n" + "| ";
			for (int j = 0; j < grilleModele.length; j++) 
			{
				String piece = this.grillePiece[i][j].getSymbole();
				String couleurPiece = "\u001B[97m"; // Blanc par défaut
				if (this.grillePiece[i][j].getCouleur() == 'B') 
				{
					couleurPiece = "\u001B[33m"; // Jaune
				} else if (this.grillePiece[i][j].getCouleur() == 'N') 
				{
					couleurPiece = "\u001B[38;5;208m"; // Orange
				}
				String resetCouleur = "\u001B[0m"; //reset couleur
				affi += couleurPiece + piece + resetCouleur + " | ";
			}
			affi += "\n";
		}
		affi += "+---+---+---+---+---+---+---+---+";

		return affi;
	}

}