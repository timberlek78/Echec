package metier;

import java.util.ArrayList;
import java.util.List;

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
	String[][] grilleModele = {{"T", "C", "F", "D", "R", "F", "C", "T"},
							   {"P", "P", "P", "P", "P", "P", "P", "P"},
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
	private Piece   pieceSelect;
	private Piece   destination;
	private boolean echec;
	private boolean aPieceSelectionner;
	private boolean destSelect;
	private boolean deplacementOK;
	private ArrayList<Piece> pieceBlanche;
	private ArrayList<Piece> pieceNoir;
	private ArrayList<Case>  ensCase;
	private Controleur ctrl;
	private char couleurEchec;

	public Grille(Controleur ctrl)
	{
		this.grillePiece        = new Piece[8][8];
		this.pieceBlanche       = new ArrayList<>();
		this.pieceNoir          = new ArrayList<>();
		this.ensCase            = new ArrayList<>();
		this.ctrl               = ctrl;
		this.aPieceSelectionner = false;
		this.echec              = false;
		this.pieceSelect        = null ;
		this.deplacementOK 		= false;
		creationGrillePiece();	
		activation();
	}


	/*Getteur */
	public String[][] getGrilleModele    ()  { return this.grilleModele;      }
	public Piece [][] getGrillePiece     ()  { return this.grillePiece;       }
	public boolean    getEchec           ()  { return this.echec;             }
	public boolean    aPieceSelectionner ()  { return this.aPieceSelectionner;}
	public boolean    aSelectDest        ()  { return this.destSelect;        }
	public boolean    deplacementOK      ()	 { return this.deplacementOK;     }
	public Piece      getPiece(int x,int y)  { return this.grillePiece[x][y]; }
	public Piece      getPieceSelect     ()  { return this.pieceSelect;       }
	public Piece      getDestination     ()  { return this.destination;       }
	public ArrayList<Piece> getPieceNoir ()  { return this.pieceNoir;         }
	public ArrayList<Piece> getPieceBlanche(){ return this.pieceBlanche;      }    
	public char getCouleurEchec()            { return this.couleurEchec;      }

	

	/*Setteur */
	public void resetEchec() {this.echec = false;}
	public void setGrilleModele(String[][] nvGrille){this.grilleModele = nvGrille;}
	public void pieceSelect(boolean b) {this.aPieceSelectionner = b;}
	public void destSelect(boolean b) {this.destSelect = b;}
	public void estDeplacementOk()    {this.deplacementOK = !this.deplacementOK;}
	public void setPieceSelect(int coordX,int coordY) 
	{
		if(this.getPiece(coordX,coordY) instanceof Case){return;}
		this.pieceSelect = this.getPiece(coordX, coordY);
		pieceSelect(true);
	}

	public void setDestination(int coordX,int coordY)
	{
		this.destination = this.getPiece(coordX, coordY);
		destSelect(true);
	}

	public void resetCaseMenace()
	{
		
		for (Case case1 : this.ensCase) 
		{
			case1.resetCouleurMenace();
		}
	}


	/* Méthodes transitives */
	public void majIHM(){this.ctrl.majIHM();}
	public void pieceManger(Piece p){this.ctrl.pieceManger(p);}

	public void addEnsCase     (Case c ){this.ensCase.add(c);     }
	public void addPieceNoir   (Piece p){this.pieceNoir   .add(p);}
	public void addPieceBlanche(Piece p){this.pieceBlanche.add(p);}

	public void removePieceNoir   (Piece p){this.pieceNoir.remove(p)   ;}
	public void removePieceBlanche(Piece p){this.pieceBlanche.remove(p);}
	

	/*---------------------------*/
	/* Gestion de l'echec et mat */
	/*---------------------------*/
	public void setEchec       (Piece p,char couleur,Roi r) //piece qui met le roi en echec, couleur c'est la couleur du roi
	{
		System.out.println("je rentre dans le setechec");
		this.echec = true;
		
		System.out.println(this.getEchec());
		this.couleurEchec = couleur;

		activationEchec(p);
		if(estEchecEtMat(p,couleur,r))
			System.out.println("Eh oh tu es echec et mat gros malin !!!!");           
	}

	//verifie si le roi est échec et mat 
	public boolean estEchecEtMat(Piece p,char couleur,Roi r)
	{
		ArrayList<Case> chemin = p.getChemin();

		boolean peutCouvrir    = peutCouvrir(p,couleur,chemin);
		boolean peutDeplacer   = peutDeplacer(r);
		boolean peutEtreManger = peutEtreManger(p);


		System.out.println("peutCouvrir    : " + peutCouvrir);
		System.out.println("peutDeplacer   : " + peutDeplacer);
		System.out.println("peutEtreManger : " + peutEtreManger);

		if(peutCouvrir && peutDeplacer && peutEtreManger)
			return true;
		else
			return false;

	}

	//verifie si une pièce peut venir couvrir l'echec 
	public boolean peutCouvrir(Piece p, char couleur, ArrayList<Case> chemin) 
	{
		ArrayList<Piece> pieces;
		
		if (couleur == 'B') {
			pieces = this.getPieceBlanche();
		} else {
			pieces = this.getPieceNoir();
		}
	
		for (Piece p1 : pieces) {
			if (p1.equals(p) || (p1 instanceof Roi)) {
				continue;
			}
	
			if (deplacementCommun(p1, chemin)) {
				return true; //l'echec peut etre couvert
			}
		}
		return false;
	}

	//verifie si le roi peut se déplacer
	public boolean peutDeplacer(Roi r)
	{
		ArrayList<Case> deplacementPossible = r.getCaseMenace();

		for (Case case1 : deplacementPossible) //parcours toutes les cases ou le déplacement est possible (pas occupé)
		{
			System.out.println(case1);
			System.out.println("rechercherCouleurMenace : " + case1.rechercheCouleurMenace(r.getCouleur()));
			if(case1.getMenace() && !case1.rechercheCouleurMenace(r.getCouleur()) ) // si une des cases n'est pas menacée
			{
				return true; //le roi peut alors se déplacer
			}		
		}
		return false;
	}

	public boolean peutEtreManger(Piece p) //piece qui met le roi en ecchec 
	{
		char couleur = p.getCouleur();
		List<Piece> pieces = (couleur == 'B') ? this.pieceBlanche : this.pieceNoir;

		for (Piece piece : pieces) 
		{
			for (Piece p1 : p.getPieceMenace()) 
			{
				if (piece.equals(p1) ) 
				{
					return true; //la piece qui met le roi en echec peut etre mangée
				}
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

	/*------------------------FIN ECHEC ET MAT----------------------------- */


	//verification des cases
	public boolean estOccupe(int nX,int nY)
	{
		if(this.grillePiece[nX][nY] instanceof Case)
			return false;
		else
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

		if(tmp instanceof Case)
			this.ensCase.remove(tmp);

		this.grillePiece[X][Y] = new Case( X, Y, this);

		this.ensCase.add((Case)this.grillePiece[X][Y]);

		this.grillePiece[nX][nY] = tmp;
	}


	//activation des cases menacées par toutes les pièces
	public void activation()
	{
		for (int i = 0; i < grillePiece.length; i++) 
			for (int j = 0; j < grillePiece[i].length; j++) 
				this.grillePiece[i][j].activation();
	}

	public void activationEchec(Piece pieceEchec)
	{
		ArrayList<Piece> caseTest = (pieceEchec.getCouleur() == 'B' ? this.pieceBlanche : this.pieceNoir);

		for (Piece piece : caseTest) 
		{
			boolean estMemePosition = (piece.getX() == pieceEchec.getX() && piece.getY() == pieceEchec.getY()); 
			if(!(piece.getCouleur() == pieceEchec.getCouleur() && estMemePosition))
			{
				piece.activation();
			}
		}
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