package gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

public class Feld extends JPanel{
	private int reihe;
	private int spalte;
	private Color farbe;
	
	public Feld(int reihe, int spalte, Color farbe) {
		this.reihe = reihe;
		this.spalte = spalte;
		this.farbe = farbe;
		//JPanel Einstellungen
		this.setBackground(farbe);
		//Layout wichtig für Ausrichtung JLabel(Figuren)
		this.setLayout(new BorderLayout());
		//MouseListener
	}
	//Getter
	public Color getFarbe() {
		return this.farbe;
	}
	public int getReihe() {
		return this.reihe;
	}
	public int getSpalte() {
		return this.spalte;
	}
	//Setter
	public void clearBackground() {
		this.setBackground(this.farbe);
	}
	@Override
	public String toString() {
		return "Feld: "+this.reihe+"|"+this.spalte;
	}
	
}
