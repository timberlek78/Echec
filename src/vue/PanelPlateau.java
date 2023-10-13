package vue;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class PanelPlateau extends JPanel implements MouseListener
{
	private final int TAILLE_CASE = 85;
	private FrameJeu frame;
	private Rectangle[][] ensRec;

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
		int XposDep = this.frame.getWidth() / 4;
		int YposDep = 50;
		echequier( XposDep,YposDep,g);
		affichageDesPieces(XposDep, YposDep, g);
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
					g.setColor(Color.WHITE);
				else
					g.setColor(Color.BLACK);

				g.fillRect(x, y, TAILLE_CASE, TAILLE_CASE);
				this.ensRec[i][j] =  new Rectangle(x, y, TAILLE_CASE, TAILLE_CASE);
				x += TAILLE_CASE;
			}
			y += TAILLE_CASE;
		} 
	}

	public void affichageDesPieces(int posXDep, int YposDep,Graphics g)
	{
		int y = YposDep;
		String[][] grilleModele = this.frame.getGrilleModele();
		g.setColor(Color.BLUE);
		g.setFont(new Font("piece", Font.BOLD, TAILLE_CASE - 10));
		for (int i = 0; i < 8; i++) 
		{	
			int x = posXDep;
			for (int j = 0; j < 8; j++) 
			{
				g.drawString(grilleModele[i][j], x + 10, y  + TAILLE_CASE - 10);
				x += TAILLE_CASE;
			}
			y += TAILLE_CASE;
		} 
	}


	public void mouseClicked(MouseEvent e) 
	{
		int sourisX =  e.getX();
		int sourisY =  e.getY();

		

		System.out.println("je rentre bien dans le mouseCliked");

		System.out.println("sourisX : " + sourisX);
		System.out.println("sourisY : " + sourisY);
		for (int i = 0; i < this.ensRec.length; i++) 
		{
			for (int j = 0; j < this.ensRec.length;j++) 
			{
				Rectangle rec = this.ensRec[i][j];
				if(sourisX > rec.getMinX() && sourisX < rec.getMaxX() && sourisY > rec.getMinY() && sourisY < rec.getMaxY())
				{
					this.frame.deplacement(i,y)
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