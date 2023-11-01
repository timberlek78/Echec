package vue;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.plaf.synth.SynthScrollBarUI;

import metier.Piece;
import metier.piece.Case;

public class PanelPlateau extends JPanel implements MouseListener
{
	private final int TAILLE_CASE = 85;
	private FrameJeu frame;
	private Rectangle[][] ensRec;
	private boolean bTemp; //true = une piece a été selectionné et attend sa destination/ false = une piece n'a pas encore été selectionné
	private Graphics g;

	public PanelPlateau(FrameJeu frame)
	{
		this.frame = frame;
		this.ensRec = new Rectangle[8][8];

		this.repaint();


		/* activation des composants */

		this.addMouseListener(this);
	}


	public void paintComponent(Graphics g)
	{
		this.g = g;
		int XposDep = this.frame.getWidth() / 4;
		int YposDep = 50;
		echequier( XposDep,YposDep,this.g);
		affichageDesPieces(XposDep, YposDep, this.g);
	}

	public void majIHM()
	{
		this.repaint();
	}

	public void echequier(int XposDep,int YposDep ,Graphics g)
	{
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
		this.g.setFont(new Font("piece", Font.BOLD, TAILLE_CASE - 10));
		for (int i = 0; i < 8; i++) 
		{	
			int x = posXDep;
			for (int j = 0; j < 8; j++) 
			{
				if(grillePiece[i][j].getCouleur() == 'B')
					this.g.setColor(Color.CYAN);
				else
					this.g.setColor(Color.DARK_GRAY);
					
				this.g.drawString(grillePiece[i][j].getSymbole(), x + 10, y  + TAILLE_CASE - 10);
				x += TAILLE_CASE;
			}
			y += TAILLE_CASE;
		} 
	}

	public void changementDeCouleur(char couleur)
	{
		this.g.setColor(Color.black);
		this.g.drawRect(10, 10, 85,85);
		this.g.setFont(new Font("piece", Font.BOLD, TAILLE_CASE - 10));
		this.g.drawString(""+ couleur, 10, 10);

		this.repaint();
	}


	public void mouseClicked(MouseEvent e) 
	{
		int sourisX =  e.getX();
		int sourisY =  e.getY();


		pieceSelect(sourisX, sourisY);

	}


	public void pieceSelectIHM(int x,int y)
	{
		this.g.setColor(Color.YELLOW);
		this.g.fillOval(x, y, 100, 100);
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