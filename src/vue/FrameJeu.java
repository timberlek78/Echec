package vue;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;


import controleur.Controleur;
import metier.Piece;

public class FrameJeu extends JFrame {
	private final int HEIGHT;
	private final int WIDTH;
	private PanelPlateau panelPlateau;
	private Controleur ctrl;


	public FrameJeu(Controleur ctrl) {
		Dimension taille = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		this.HEIGHT = (int) taille.getHeight();
		this.WIDTH = (int) taille.getWidth();

		this.ctrl = ctrl;

		this.setTitle("Échec et mât'Hair !");
		this.setSize(this.WIDTH, this.HEIGHT - 50);
		this.setLocation(0, 0);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setBackground(new Color(159, 117, 80));

		


		panelPlateau = new PanelPlateau(this);
		
		this.add(this.panelPlateau);

	
		this.setVisible(true);
	}

	public void majIHM() {this.panelPlateau.majIHM();}
	public void pieceManger(Piece p){this.panelPlateau.pieceManger(p);}

	public void setPieceSelect(int coordX, int coordY) {
		this.ctrl.setPieceSelect(coordX, coordY);
	}

	public void setDestination(int coordX, int coordY) {
		this.ctrl.setDestination(coordX, coordY);
	}

	public int getHeight() {
		return this.HEIGHT;
	}

	public int getWidth() {
		return this.WIDTH;
	}

	public Piece[][] getGrillePiece() {
		return this.ctrl.getGrillePiece();
	}
}