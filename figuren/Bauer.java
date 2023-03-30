package figuren;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import gui.Feld;
import main.Engine;

public class Bauer extends Figur{

	public Bauer(int reihe, int spalte, boolean istWeiß, Engine dieEngine) {
		this.reihe = reihe;
		this.spalte = spalte;
		this.istWeiß =istWeiß;
		this.dieEngine = dieEngine;
		//JPanel Einstellungen
		this.setVerticalAlignment(CENTER);
		this.setHorizontalAlignment(CENTER);
		//Bild laden
		if(this.istWeiß == true) {
			img = new ImageIcon("src/figuren/bilder/WBauer.png");
		}
		else {
			img = new ImageIcon("src/figuren/bilder/SBauer.png");
		}
		imageLaden(this, img);
		bestimmeVorzeichen(this.istWeiß);
	}

	@Override
	public ArrayList<Feld> getMoeglicheZuege(Feld[][] felder) {
		this.moeglicheZuege.removeAll(moeglicheZuege);
		pruefeOberes(felder);
		pruefeDoppeltes(felder);
		pruefeSchlagen(felder);
		pruefeEnPassant(felder);
		return this.moeglicheZuege;
	}
	//Überprüfung
	public void pruefeOberes(Feld[][] felder) {
		//Prüfe Oberes Feld
		if(this.reihe-1*this.vorzeichen >= 0 && this.reihe-1*this.vorzeichen<=7) {
			if(dieEngine.getFigur(this.reihe-1*this.vorzeichen, this.spalte) == null) {
				this.moeglicheZuege.add(felder[this.reihe-1*this.vorzeichen][this.spalte]);
			}
		}
	}
	public void pruefeDoppeltes(Feld[][] felder) {
		//Bauer Vorstoß
		if(this.wurdeBewegt == false) {
			if(this.reihe-2*this.vorzeichen >= 0 && this.reihe-2*this.vorzeichen<=7 && dieEngine.getFigur(this.reihe-1*this.vorzeichen, this.spalte) == null) {
				if(dieEngine.getFigur(this.reihe-2*this.vorzeichen, this.spalte) == null) {
					this.moeglicheZuege.add(felder[this.reihe-2*this.vorzeichen][this.spalte]);
				}
			}
		}
	}
	public void pruefeSchlagen(Feld[][] felder) {
		//PruefeRechtes Oberes feld
		if(this.spalte+1 <= 7) {
			if(dieEngine.getFigur(this.reihe-1*this.vorzeichen, this.spalte+1) != null) {
				if (dieEngine.getFigur(this.reihe-1*this.vorzeichen, this.spalte+1).getIstWeiß() != this.istWeiß) {
					this.moeglicheZuege.add(felder[this.reihe-1*this.vorzeichen][this.spalte+1]);
				}
			}
		}
		//Pr�fe linkes oberes Feld
		if(this.spalte-1 <= 7) {
			if(dieEngine.getFigur(this.reihe-1*this.vorzeichen, this.spalte-1) != null) {
				if (dieEngine.getFigur(this.reihe-1*this.vorzeichen, this.spalte-1).getIstWeiß() != this.istWeiß) {
					this.moeglicheZuege.add(felder[this.reihe-1*this.vorzeichen][this.spalte-1]);
				}
			}
		}
	}
	private void pruefeEnPassant(Feld[][] felder) {
		this.enPassantLinks = false;
		this.enPassantRechts = false;
		//wenn rechts Figur steht
		if(this.spalte+1 <= 7) {
			//Brett belegt?
			if(dieEngine.getFigur(this.reihe, this.spalte+1) != null) {
				//Figur = Bauer?
				if(dieEngine.getFigur(this.reihe, this.spalte+1) instanceof Bauer) {
					//Bauer = letzter Zug
					if(dieEngine.getLetzterZug() == dieEngine.getFigur(this.reihe, this.spalte+1)) {
						//Letzter zug war erster Zug des Bauern
						if(dieEngine.getLetzterZug().getAnzahlZuege()==1 && dieEngine.getLetzterZug().getBauerVorstoß()==true) {
							//this.legaleZuege.add(felder[this.reihe+1*this.vorzeichen][this.spalte+1]);
							this.setEnPassantRechts(true);
						}	
					}
				}
			}
		}
		//Wenn Links Figur steht
		if(this.spalte-1>=0) {
			if(dieEngine.getFigur(this.reihe, this.spalte-1) != null) {
				if(dieEngine.getFigur(this.reihe, this.spalte-1) instanceof Bauer) {
					if(dieEngine.getLetzterZug() == dieEngine.getFigur(this.reihe, this.spalte-1)) {
						if(dieEngine.getLetzterZug().getAnzahlZuege()==1 && dieEngine.getLetzterZug().getBauerVorstoß()==true) {
							//this.legaleZuege.add(felder[this.reihe+1*this.vorzeichen][this.spalte-1]);
							this.setEnPassantLinks(true);
						}
					}
				}
			}
		}
	}
}