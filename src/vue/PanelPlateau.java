package vue;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import metier.Piece;
import metier.piece.Case;

public class PanelPlateau extends JPanel implements MouseListener
{
	private final int TAILLE_CASE = 70;
	private final int Y_PIECE_MANGER_N = TAILLE_CASE * 9 + 50;
	private final int Y_PIECE_MANGER_B = 50;
	private final int X_PIECE_MANGER;
	private Rectangle[][] ensRec;
	private ArrayList<BufferedImage> ensImagePieceManger;
	private ArrayList<Character> couleurPieceManger;
	private Graphics2D g;
	private FrameJeu frame;
	private boolean afficherCercle = false;
	private boolean bTemp; //true = une piece a été selectionné et attend sa destination/ false = une piece n'a pas encore été selectionné
	private boolean echequierFait = false;
	private int xCercle;
	private int yCercle;
	private int nbPieceMangerB;
	private int nbPieceMangerN;

	public PanelPlateau(FrameJeu frame)
	{
		this.frame = frame;
		this.ensRec = new Rectangle[8][8];
		X_PIECE_MANGER = this.frame.getWidth() / 4 - 50;

		this.ensImagePieceManger = new ArrayList<BufferedImage>();
		this.couleurPieceManger  = new ArrayList<Character>();

		this.nbPieceMangerB = 0;
		this.nbPieceMangerN = 0;
		this.repaint();

		/* activation des composants */

		this.addMouseListener(this);
	}


	public void paintComponent(Graphics g) 
	{
		Graphics2D g2d = (Graphics2D) g;
		this.g = g2d;
		this.g.setStroke(new BasicStroke(10)); // Épaisseur de trait de 2 pixels

		int XposDep = this.frame.getWidth() / 4;
		int YposDep = 100;


		

		echequier(XposDep, YposDep, this.g);

		affichageDesPieces(XposDep, YposDep, this.g);
		
		if (afficherCercle) 
		{
			this.g.setStroke(new BasicStroke(5));
			this.g.setColor(Color.YELLOW);
			this.g.drawRect(xCercle , yCercle, TAILLE_CASE, TAILLE_CASE );
		}


		for (int i = 0; i < ensImagePieceManger.size(); i++) 
		{
			if(this.couleurPieceManger.get(i) == 'N')
			{
				this.g.drawImage(ensImagePieceManger.get(i), X_PIECE_MANGER + (nbPieceMangerB * TAILLE_CASE/2), Y_PIECE_MANGER_B, TAILLE_CASE/2, TAILLE_CASE/2, frame);
			}
			else
			{
				this.g.drawImage(ensImagePieceManger.get(i), X_PIECE_MANGER + (nbPieceMangerN * TAILLE_CASE/2), Y_PIECE_MANGER_N, TAILLE_CASE/2, TAILLE_CASE/2, frame);
			}
		}
	}

	public void majIHM()
	{
		this.afficherCercle   = false;
		this.repaint();
	}

	public void echequier(int XposDep,int YposDep ,Graphics g)
	{
		this.g.drawRect(XposDep, YposDep, TAILLE_CASE * 8, TAILLE_CASE * 8);
		int y = YposDep;
		for (int i = 0; i < 8; i++) 
		{	
			int x = XposDep;
			for (int j = 0; j < 8; j++) 
			{
				if((i + j) % 2 == 0)
					this.g.setColor(Color.WHITE);
				else
					this.g.setColor(Color.BLACK);

				this.g.fillRect(x, y, TAILLE_CASE, TAILLE_CASE);
				this.ensRec[i][j] =  new Rectangle(x, y, TAILLE_CASE, TAILLE_CASE);
				x += TAILLE_CASE;
			}
			y += TAILLE_CASE;
		} 
	}

	public void affichageDesPieces(int posXDep, int YposDep,Graphics g)
	{
		int y = YposDep;
		Piece[][] grillePiece = this.frame.getGrillePiece();
		for (int i = 0; i < 8; i++) 
		{	
			int x = posXDep;
			for (int j = 0; j < 8; j++) 
			{
				try 
				{
					BufferedImage image = ImageIO.read(new File("lib/"+grillePiece[i][j].getCouleur()+"/"+grillePiece[i][j].getSymbole()+".png"));
					this.g.drawImage(image, x, y, TAILLE_CASE,TAILLE_CASE,frame);
		
				}
				catch (Exception e) {}
				x += TAILLE_CASE;
			}
			y += TAILLE_CASE;
		} 
	}

	public void pieceManger(Piece p)
	{
		try
		{
			this.ensImagePieceManger.add(ImageIO.read(new File("lib/"+p.getCouleur()+"/"+p.getSymbole()+".png")));
			
		}
		catch(Exception e){}

		this.couleurPieceManger.add(p.getCouleur());
		if(p.getCouleur() == 'B')
			this.nbPieceMangerB++;	
		else
			this.nbPieceMangerN++;

		this.repaint();
	}


	public void mouseClicked(MouseEvent e) 
	{
		int sourisX =  e.getX();
		int sourisY =  e.getY();

		if(e.getButton() == MouseEvent.BUTTON1)
			pieceSelect(sourisX, sourisY);

		if(e.getButton() == MouseEvent.BUTTON3)
			majIHM();

	}


	public void pieceSelectIHM(int x, int y) 
	{
		this.xCercle = x;
		this.yCercle = y;
		afficherCercle = true;
		this.repaint();
	}

	public void pieceSelect(int sourisX,int sourisY)
	{
		for (int i = 0; i < this.ensRec.length; i++) 
		{
			for (int j = 0; j < this.ensRec.length;j++) 
			{
				Rectangle rec = this.ensRec[i][j];
				if(sourisX > rec.getMinX() && sourisX < rec.getMaxX() && sourisY > rec.getMinY() && sourisY < rec.getMaxY())
				{
					if(bTemp)
					{
						this.frame.setDestination(i,j);
						this.bTemp = false;
					}
					else
					{
						if(!(this.frame.getGrillePiece()[i][j] instanceof Case))
						{
							this.frame.setPieceSelect(i, j);
							this.bTemp = true;
							pieceSelectIHM((int)rec.getX(),(int)rec.getY());
						}
					}
				}
			}
		}
	}


	@Override
	public void mousePressed(MouseEvent e) {
	}


	@Override
	public void mouseReleased(MouseEvent e) {
	}


	@Override
	public void mouseEntered(MouseEvent e) {
	}


	@Override
	public void mouseExited(MouseEvent e) {
	}
}