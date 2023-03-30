package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import figuren.Dame;
import figuren.Laeufer;
import figuren.Springer;
import figuren.Turm;
import main.Engine;

public class Brett extends JPanel {

	private Engine dieEngine;
	//Spielbrett
	private Feld felder[][] = new Feld[8][8];
	private int feld_width = 600/8;

	private JLabel dameLabel;
	private JLabel turmLabel;
	private JLabel springerLabel;
	private JLabel laeuferLabel;
	private int vorzeichen;
	
	private Color farbe;
	//Konstruktor
	public Brett(Engine dieEngine) {
		this.dieEngine = dieEngine;
		//JPanel EInstellungen
		this.setBounds(0, 0, 600, 600);
		//Schachbrett 8x8
		this.setLayout(new GridLayout(8, 8));
		//Optional | Wird von den Feldern bedeckt!!!
		this.setBackground(Color.black);
		this.setBorder(new LineBorder(Color.black));
		this.setVisible(true);
		//Felder erstellen
		erstelleFelder();
		this.addMouseListener(mouseAdapter);
		//Brett zeichnen
		zeichneBrett();
	}
	private MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {
			//Feld erfassen
			int x = e.getX();
			int y = e.getY();  
			int reihe = (y / feld_width);
			int spalte = x / feld_width;
			//Fokus setzen
			dieEngine.setFokus(reihe, spalte);
		}
		@Override
		public void mouseDragged(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {}	
	};
	private void erstelleFelder(){
		////////Erstellt Schachbrett////////
		for(int i = 0;i<=7;i++) {
			for(int j = 0; j<=7; j++) {
				//Untersuchung auf Schwarzes bzw. Wei�es Feld
				if((i % 2 == 0 && j % 2 == 0) || (i % 2 != 0 && j % 2 != 0)) {
					farbe = Color.white;
				}
				else {
					farbe = Color.decode("#2B5178");
				}
				//Feld wird erzeugt und in Array abgelegt
				felder[i][j] = new Feld(i, j, farbe);
				//Feld[i][j] zu dasBrett hinzuf�gen --> Gridlayout
				this.add(felder[i][j]);
			}
		}
	}
	public void zeichneBrett() {
		brettRaeumen();		
		//Für Weiße
		for (int i = 0; i < dieEngine.getFiguren(true).size();i++){
			felder[dieEngine.getFiguren(true).get(i).getReihe()][dieEngine.getFiguren(true).get(i).getSpalte()].add(dieEngine.getFiguren(true).get(i));
		}
		for (int i = 0; i < dieEngine.getFiguren(false).size();i++) {
			felder[dieEngine.getFiguren(false).get(i).getReihe()][dieEngine.getFiguren(false).get(i).getSpalte()].add(dieEngine.getFiguren(false).get(i));
		}
		this.validate();
		this.repaint();
	}
	public void brettRaeumen() {
		for(int i = 0; i < 8;i++) {
			for(int j = 0; j < 8;j++) {
				felder[i][j].removeAll();
			}
		}
		this.validate();
	}
	public void openFigurAuswahl(boolean istWeiß, int reihe, int spalte) {
		//this.figurAuswahl = true;
		ImageIcon dame;
		ImageIcon turm;
		ImageIcon springer;
		ImageIcon laeufer;
		if(istWeiß) {
			dame = new ImageIcon("figuren/bilder/WDame.png");
			springer = new ImageIcon("figuren/bilder/WSpringer.png");
			laeufer = new ImageIcon("figuren/bilder/WLaeufer.png");
			turm = new ImageIcon("figuren/bilder/WTurm.png");
			vorzeichen = 1;
		}
		else {
			dame = new ImageIcon("figuren/bilder/SDame.png");
			springer = new ImageIcon("figuren/bilder/SSpringer.png");
			laeufer = new ImageIcon("figuren/bilder/SLaeufer.png");
			turm = new ImageIcon("figuren/bilder/STurm.png");
			vorzeichen = -1;
		}
		//1Feld
		dameLabel = new JLabel(dame);
		dameLabel.setOpaque(true);
		dameLabel.setBackground(Color.orange);
		dameLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				dieEngine.setFigur(new Dame(reihe, spalte, istWeiß, dieEngine));
			}
		});
		felder[reihe][spalte].removeAll();
		felder[reihe][spalte].add(dameLabel);
		
		springerLabel = new JLabel(springer);
		springerLabel.setOpaque(true);
		springerLabel.setBackground(Color.orange);
		springerLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				dieEngine.setFigur(new Springer(reihe, spalte, istWeiß, dieEngine));
			}
		});
		felder[reihe+1*vorzeichen][spalte].removeAll();
		felder[reihe+1*vorzeichen][spalte].add(springerLabel);
		
		laeuferLabel = new JLabel(laeufer);
		laeuferLabel.setOpaque(true);
		laeuferLabel.setBackground(Color.orange);
		laeuferLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				dieEngine.setFigur(new Laeufer(reihe, spalte, istWeiß, dieEngine));
			}
		});
		felder[reihe+2*vorzeichen][spalte].removeAll();
		felder[reihe+2*vorzeichen][spalte].add(laeuferLabel);
		
		turmLabel = new JLabel(turm);
		turmLabel.setOpaque(true);
		turmLabel.setBackground(Color.orange);
		turmLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				dieEngine.setFigur(new Turm(reihe, spalte, istWeiß, dieEngine));
			}
		});
		felder[reihe+3*vorzeichen][spalte].removeAll();
		felder[reihe+3*vorzeichen][spalte].add(turmLabel);
	}
	//Setter
	public void setMarkierung(int reihe, int spalte) {
		if(dieEngine.getFigur(reihe, spalte) == null) {
			felder[reihe][spalte].setBorder(new LineBorder(Color.green, 5));
		}
		else {
			felder[reihe][spalte].setBorder(new LineBorder(Color.green, 5));
		}
	}
	public void setFokusMarkierung(int reihe, int spalte, Color farbe) {
		felder[reihe][spalte].setBackground(farbe);

	}
	public void removeMarkierung(int reihe, int spalte) {
		if(dieEngine.getFigur(reihe, spalte) == null) {
			felder[reihe][spalte].setBorder(null);
		}
		else {
			felder[reihe][spalte].setBorder(null);
		}
		felder[reihe][spalte].setBackground(felder[reihe][spalte].getFarbe());
	}
	//Geter
	public Feld getFeld(int reihe, int spalte) {
		return this.felder[reihe][spalte];
	}
	public Feld[][] getFelder() {
		return this.felder;
	}
}
