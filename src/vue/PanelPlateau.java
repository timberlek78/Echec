package vue;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class PanelPlateau extends JPanel
{
	private final int TAILLE_CASE = 85;
	private FrameJeu frame;

	public PanelPlateau(FrameJeu frame)
	{
		this.frame = frame;
		this.repaint();
	}


	public void paintComponent(Graphics g)
	{
		int XposDep = this.frame.getWidth() / 4;
		int YposDep = 50;
		echequier( XposDep,YposDep,g);
		
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
				x += TAILLE_CASE;
			}
			y += TAILLE_CASE;
		} 
	}

	public void affichageDesPieces(int posXDep, int YposDep,Graphics g)
	{
		int y = YposDep;

		for (int i = 0; i < 8; i++) 
		{	
			int x = posXDep;
			for (int j = 0; j < 8; j++) 
			{
				if((i + j) % 2 == 0)
					g.setColor(Color.WHITE);
				else
					g.setColor(Color.BLACK);

				g.fillRect(x, y, TAILLE_CASE, TAILLE_CASE);
				x += TAILLE_CASE;
			}
			y += TAILLE_CASE;
		} 
	}
}