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

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import metier.Piece;
import metier.piece.Case;

public class PanelPlateau extends JPanel implements MouseListener
{
	private final int TAILLE_CASE = 85;
	private FrameJeu frame;
	private Rectangle[][] ensRec;
	private boolean bTemp; //true = une piece a été selectionné et attend sa destination/ false = une piece n'a pas encore été selectionné
	private Graphics2D g;
	private boolean afficherCercle = false;
	private int xCercle;
	private int yCercle;

	public PanelPlateau(FrameJeu frame)
	{
		System.out.println("mais je rentre quand meme");
		this.frame = frame;
		this.ensRec = new Rectangle[8][8];
		

		this.repaint();


		/* activation des composants */

		this.addMouseListener(this);
	}


	public void paintComponent(Graphics g) 
	{
		Graphics2D g2d = (Graphics2D) g;
		this.g = g2d;
		g2d.setStroke(new BasicStroke(20)); // Épaisseur de trait de 2 pixels

		int XposDep = this.frame.getWidth() / 4;
		int YposDep = 50;


		this.g.drawRect(XposDep, YposDep, TAILLE_CASE * 8, TAILLE_CASE * 8);
		echequier(XposDep, YposDep, g2d);
		affichageDesPieces(XposDep, YposDep, g2d);
		
		if (afficherCercle) 
		{
			g2d.setColor(Color.YELLOW);
			g2d.drawOval(xCercle - 20, yCercle - 20, TAILLE_CASE / 2, TAILLE_CASE / 2);
		}
	}

	public void majIHM()
	{
		afficherCercle = false;
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
							pieceSelectIHM((int)rec.getCenterX(),(int)rec.getCenterY());
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