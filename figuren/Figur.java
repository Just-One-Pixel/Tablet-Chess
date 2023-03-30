package figuren;

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


import gui.Feld;
import main.Engine;

public abstract class Figur extends JLabel{
	////Variablen////
	protected int reihe;
	protected int spalte;
	protected boolean istWeiß;
	protected ImageIcon img;
	protected int vorzeichen;
	protected int anzahlZuege = 0;
	protected boolean wurdeBewegt = false;
	
	//Für Bauer
	protected boolean bauerVorstoß = false;
	protected boolean enPassantLinks = false;
	protected boolean enPassantRechts = false;
	
	//Für Koenig
	protected boolean langeRochadeMoeglich = false;
	protected boolean kurzeRochadeMoeglich = false;
	protected boolean langeRochadeMoeglichFEN = false;
	protected boolean kurzeRochadeMoeglichFEN = false;

	protected Engine dieEngine;

	protected ArrayList<Feld> moeglicheZuege = new ArrayList<Feld>();


	//Image Laden
	protected void imageLaden(Figur figur, ImageIcon img) {
		figur.setIcon(img);
		figur.validate();
		figur.repaint();
	}
	protected void bestimmeVorzeichen(boolean istWeiß) {
		if (istWeiß == true) {
			this.vorzeichen = 1;
		}
		else {
			this.vorzeichen = -1;
		}
	}
	/*
	public ArrayList<Feld> getMoeglicheZuege(Feld[][] felder) {
		this.moeglicheZuege.removeAll(moeglicheZuege);
		System.out.println("Moegliche Zuege: ");
		this.moeglicheZuege.add(felder[0][0]);
		System.out.println(this.moeglicheZuege);
		return this.moeglicheZuege;
	}
	 */
	public abstract ArrayList<Feld> getMoeglicheZuege(Feld[][] felder);
	//Setter
	public void setReihe(int reihe) {
		this.reihe = reihe;
	}
	public void setSpalte(int spalte) {
		this.spalte = spalte;
	}
	public void setWurdeBewegt() {
		this.wurdeBewegt = true;
	}
	public void setBauerVorstoß(boolean bauerVorstoß) {
		this.bauerVorstoß = bauerVorstoß;
	}
	public void setLangeRochadeMoeglich(boolean moeglich) {
		this.langeRochadeMoeglich = moeglich;
	}
	public void setKurzeRochadeMoeglich(boolean moeglich) {
		this.kurzeRochadeMoeglich = moeglich;
	}
	public void setEnPassantLinks(boolean enPassantLinks) {
		this.enPassantLinks = enPassantLinks;
	}
	public void setEnPassantRechts(boolean enPassantRechts) {
		this.enPassantRechts = enPassantRechts;
	}
	public void incAnzahlZuege() {
		this.anzahlZuege = this.anzahlZuege +1;
	}
	public void setKurzeRochadeMoeglichFEN(boolean moeglich) {
		this.kurzeRochadeMoeglichFEN = moeglich;
	}
	public void setLangeRochadeMoeglichFEN(boolean moeglich) {
		this.langeRochadeMoeglichFEN = moeglich;
	}
	//Getter
	public int getReihe() {
		return this.reihe;
	}
	public int getSpalte() {
		return this.spalte;
	}
	public boolean getIstWeiß() {
		return this.istWeiß;
	}
	public boolean getWurdeBewegt() {
		return this.wurdeBewegt;
	}
	public boolean getKurzeRochadeMoeglich() {
		return this.kurzeRochadeMoeglich;
	}
	public boolean getLangeRochadeMoeglich() {
		return this.langeRochadeMoeglich;
	}
	public boolean getEnPassantLinks() {
		return this.enPassantLinks;
	}
	public boolean getEnPassantRechts() {
		return this.enPassantRechts;
	}
	public int getAnzahlZuege() {
		return this.anzahlZuege;
	}
	public boolean getBauerVorstoß() {
		return this.bauerVorstoß;
	}
	public int getVorzeichen() {
		return this.vorzeichen;
	}
	//To String
	@Override
	public String toString() {
		return this.getClass().getName()+": "+this.reihe+"|"+this.spalte;
	}
	//Für Moegliche Zuege
	public void pruefeVertikal(Feld[][] felder) {
		//Nach Oben
		for(int i = 1; this.reihe-i*this.vorzeichen >= 0 && this.reihe-i*this.vorzeichen <= 7; i++) {
			if(dieEngine.getFigur(this.reihe-i*this.vorzeichen, this.spalte) != null) {
				if (dieEngine.getFigur(this.reihe-i*this.vorzeichen, this.spalte).getIstWeiß() != this.istWeiß) {
					this.moeglicheZuege.add(felder[this.reihe-i*this.vorzeichen][this.spalte]);
				}
				break;
			}
			else {
				this.moeglicheZuege.add(felder[this.reihe-i*this.vorzeichen][this.spalte]);
			}
		}
		//Nach Unten
		for(int i = 1; this.reihe+i*this.vorzeichen >= 0 && this.reihe+i*this.vorzeichen <= 7; i++) {
			if(dieEngine.getFigur(this.reihe+i*this.vorzeichen, this.spalte) != null) {
				if (dieEngine.getFigur(this.reihe+i*this.vorzeichen, this.spalte).getIstWeiß() != this.istWeiß) {
					this.moeglicheZuege.add(felder[this.reihe+i*this.vorzeichen][this.spalte]);
				}
				break;
			}
			else {
				this.moeglicheZuege.add(felder[this.reihe+i*this.vorzeichen][this.spalte]);
			}
		}
	}
	public void pruefeHorizontal(Feld[][] felder) {
		//Nach Rechts
		for(int i = 1; this.spalte+i*this.vorzeichen >= 0 && this.spalte+i*this.vorzeichen <= 7; i++) {
			if(dieEngine.getFigur(this.reihe, this.spalte+i*this.vorzeichen) != null) {
				if (dieEngine.getFigur(this.reihe, this.spalte+i*this.vorzeichen).getIstWeiß() != this.istWeiß) {
					this.moeglicheZuege.add(felder[this.reihe][this.spalte+i*this.vorzeichen]);
				}
				break;
			}
			else {
				this.moeglicheZuege.add(felder[this.reihe][this.spalte+i*this.vorzeichen]);
			}
		}
		//Nach Links
		for(int i = 1; this.spalte-i*this.vorzeichen >= 0 && this.spalte-i*this.vorzeichen <= 7; i++) {
			if(dieEngine.getFigur(this.reihe, this.spalte-i*this.vorzeichen) != null) {
				if (dieEngine.getFigur(this.reihe, this.spalte-i*this.vorzeichen).getIstWeiß() != this.istWeiß) {
					this.moeglicheZuege.add(felder[this.reihe][this.spalte-i*this.vorzeichen]);
				}
				break;
			}
			else {
				this.moeglicheZuege.add(felder[this.reihe][this.spalte-i*this.vorzeichen]);
			}
		}
	}
	public void pruefeDiagonal(Feld[][] felder) {
		//Nach rechts oben
		for(int i = 1; this.reihe-i*this.vorzeichen >= 0  && this.reihe-i*this.vorzeichen <=7 && this.spalte+i*this.vorzeichen <=7 && this.spalte+i*this.vorzeichen >= 0;i++) {
			if(dieEngine.getFigur(this.reihe-i*this.vorzeichen, this.spalte+i*this.vorzeichen) != null) {
				if (dieEngine.getFigur(this.reihe-i*this.vorzeichen, this.spalte+i*this.vorzeichen).getIstWeiß() != this.istWeiß) {
					this.moeglicheZuege.add(felder[this.reihe-i*this.vorzeichen][this.spalte+i*this.vorzeichen]);
				}
				break;
			}
			else {
				this.moeglicheZuege.add(felder[this.reihe-i*this.vorzeichen][this.spalte+i*this.vorzeichen]);
			}
		}
		//Nach rechts unten
		for(int i = 1; this.reihe+i*this.vorzeichen >= 0  && this.reihe+i*this.vorzeichen <=7 && this.spalte+i*this.vorzeichen <=7 && this.spalte+i*this.vorzeichen >= 0;i++) {
			if(dieEngine.getFigur(this.reihe+i*this.vorzeichen, this.spalte+i*this.vorzeichen) != null) {
				if (dieEngine.getFigur(this.reihe+i*this.vorzeichen, this.spalte+i*this.vorzeichen).getIstWeiß() != this.istWeiß) {
					this.moeglicheZuege.add(felder[this.reihe+i*this.vorzeichen][this.spalte+i*this.vorzeichen]);
				}
				break;
			}
			else {
				this.moeglicheZuege.add(felder[this.reihe+i*this.vorzeichen][this.spalte+i*this.vorzeichen]);
			}
		}
		//Nach links unten
		for(int i = 1; this.reihe+i*this.vorzeichen >= 0  && this.reihe+i*this.vorzeichen <=7 && this.spalte-i*this.vorzeichen <=7 && this.spalte-i*this.vorzeichen >= 0;i++) {
			if(dieEngine.getFigur(this.reihe+i*this.vorzeichen, this.spalte-i*this.vorzeichen) != null) {
				if (dieEngine.getFigur(this.reihe+i*this.vorzeichen, this.spalte-i*this.vorzeichen).getIstWeiß() != this.istWeiß) {
					this.moeglicheZuege.add(felder[this.reihe+i*this.vorzeichen][this.spalte-i*this.vorzeichen]);
				}
				break;
			}
			else {
				this.moeglicheZuege.add(felder[this.reihe+i*this.vorzeichen][this.spalte-i*this.vorzeichen]);
			}
		}
		//Nach links oben
		for(int i = 1; this.reihe-i*this.vorzeichen >= 0  && this.reihe-i*this.vorzeichen <=7 && this.spalte-i*this.vorzeichen <=7 && this.spalte-i*this.vorzeichen >= 0;i++) {
			if(dieEngine.getFigur(this.reihe-i*this.vorzeichen, this.spalte-i*this.vorzeichen) != null) {
				if (dieEngine.getFigur(this.reihe-i*this.vorzeichen, this.spalte-i*this.vorzeichen).getIstWeiß() != this.istWeiß) {
					this.moeglicheZuege.add(felder[this.reihe-i*this.vorzeichen][this.spalte-i*this.vorzeichen]);
				}
				break;
			}
			else {
				this.moeglicheZuege.add(felder[this.reihe-i*this.vorzeichen][this.spalte-i*this.vorzeichen]);
			}
		}
	}
}
