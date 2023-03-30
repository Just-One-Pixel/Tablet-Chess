package figuren;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import gui.Feld;
import main.Engine;

public class Springer extends Figur{

	public Springer(int reihe, int spalte, boolean istWeiß, Engine dieEngine) {
		this.reihe = reihe;
		this.spalte = spalte;
		this.istWeiß =istWeiß;
		this.dieEngine = dieEngine;
		//JPanel Einstellungen
		this.setVerticalAlignment(CENTER);
		this.setHorizontalAlignment(CENTER);
		//Bild laden
		if(this.istWeiß == true) {
			img = new ImageIcon("src/figuren/bilder/WSpringer.png");
		}
		else {
			img = new ImageIcon("src/figuren/bilder/SSpringer.png");
		}
		imageLaden(this, img);
		bestimmeVorzeichen(this.istWeiß);
	}

	@Override
	public ArrayList<Feld> getMoeglicheZuege(Feld[][] felder) {
		this.moeglicheZuege.removeAll(moeglicheZuege);
		pruefeSpringer(felder);
		return this.moeglicheZuege;
	}
	//Überprüfung
	public void pruefeSpringer(Feld[][] felder) {
		//Erstes Links Oben
		if(this.reihe-1*this.vorzeichen>=0 && this.spalte-2*this.vorzeichen>=0 && this.reihe-1*this.vorzeichen<=7 && this.spalte-2*this.vorzeichen<=7) {
			if(dieEngine.getFigur(this.reihe-1*this.vorzeichen, this.spalte-2*this.vorzeichen)!=null) {
				if(dieEngine.getFigur(this.reihe-1*this.vorzeichen, this.spalte-2*this.vorzeichen).getIstWeiß()!= this.istWeiß) {
					this.moeglicheZuege.add(felder[this.reihe-1*this.vorzeichen][this.spalte-2*this.vorzeichen]);
				}
			}
			else {
				this.moeglicheZuege.add(felder[this.reihe-1*this.vorzeichen][this.spalte-2*this.vorzeichen]);
			}
		}
		//Zweites Links Oben
		if(this.reihe-2*this.vorzeichen>=0 && this.spalte-1*this.vorzeichen>=0 && this.reihe-2*this.vorzeichen<=7 && this.spalte-1*this.vorzeichen<=7) {
			if(dieEngine.getFigur(this.reihe-2*this.vorzeichen, this.spalte-1*this.vorzeichen)!=null) {
				if(dieEngine.getFigur(this.reihe-2*this.vorzeichen, this.spalte-1*this.vorzeichen).getIstWeiß()!= this.istWeiß) {
					this.moeglicheZuege.add(felder[this.reihe-2*this.vorzeichen][this.spalte-1*this.vorzeichen]);
				}
			}
			else {
				this.moeglicheZuege.add(felder[this.reihe-2*this.vorzeichen][this.spalte-1*this.vorzeichen]);
			}
		}
		//Erstes Rechts Oben
		if(this.reihe-1*this.vorzeichen>=0 && this.spalte+2*this.vorzeichen>=0 && this.reihe-1*this.vorzeichen<=7 && this.spalte+2*this.vorzeichen<=7) {
			if(dieEngine.getFigur(this.reihe-1*this.vorzeichen, this.spalte+2*this.vorzeichen)!=null) {
				if(dieEngine.getFigur(this.reihe-1*this.vorzeichen, this.spalte+2*this.vorzeichen).getIstWeiß()!= this.istWeiß) {
					this.moeglicheZuege.add(felder[this.reihe-1*this.vorzeichen][this.spalte+2*this.vorzeichen]);
				}
			}
			else {
				this.moeglicheZuege.add(felder[this.reihe-1*this.vorzeichen][this.spalte+2*this.vorzeichen]);
			}
		}
		//Zweites Rechts Oben
		if(this.reihe-2*this.vorzeichen>=0 && this.spalte+1*this.vorzeichen>=0 && this.reihe-2*this.vorzeichen<=7 && this.spalte+1*this.vorzeichen<=7) {
			if(dieEngine.getFigur(this.reihe-2*this.vorzeichen, this.spalte+1*this.vorzeichen)!=null) {
				if(dieEngine.getFigur(this.reihe-2*this.vorzeichen, this.spalte+1*this.vorzeichen).getIstWeiß()!= this.istWeiß) {
					this.moeglicheZuege.add(felder[this.reihe-2*this.vorzeichen][this.spalte+1*this.vorzeichen]);
				}
			}
			else {
				this.moeglicheZuege.add(felder[this.reihe-2*this.vorzeichen][this.spalte+1*this.vorzeichen]);
			}
		}
		//Erstes Links Unten
		if(this.reihe+1*this.vorzeichen>=0 && this.spalte-2*this.vorzeichen>=0 && this.reihe+1*this.vorzeichen<=7 && this.spalte-2*this.vorzeichen<=7) {
			if(dieEngine.getFigur(this.reihe+1*this.vorzeichen, this.spalte-2*this.vorzeichen)!=null) {
				if(dieEngine.getFigur(this.reihe+1*this.vorzeichen, this.spalte-2*this.vorzeichen).getIstWeiß()!= this.istWeiß) {
					this.moeglicheZuege.add(felder[this.reihe+1*this.vorzeichen][this.spalte-2*this.vorzeichen]);
				}
			}
			else {
				this.moeglicheZuege.add(felder[this.reihe+1*this.vorzeichen][this.spalte-2*this.vorzeichen]);
			}
		}
		//Zweites Links Unten
		if(this.reihe+2*this.vorzeichen>=0 && this.spalte-1*this.vorzeichen>=0 && this.reihe+2*this.vorzeichen<=7 && this.spalte-1*this.vorzeichen<=7) {
			if(dieEngine.getFigur(this.reihe+2*this.vorzeichen, this.spalte-1*this.vorzeichen)!=null) {
				if(dieEngine.getFigur(this.reihe+2*this.vorzeichen, this.spalte-1*this.vorzeichen).getIstWeiß()!= this.istWeiß) {
					this.moeglicheZuege.add(felder[this.reihe+2*this.vorzeichen][this.spalte-1*this.vorzeichen]);
				}
			}
			else {
				this.moeglicheZuege.add(felder[this.reihe+2*this.vorzeichen][this.spalte-1*this.vorzeichen]);
			}
		}
		//Erstes Rechts Unten
		if(this.reihe+1*this.vorzeichen>=0 && this.spalte+2*this.vorzeichen>=0 && this.reihe+1*this.vorzeichen<=7 && this.spalte+2*this.vorzeichen<=7) {
			if(dieEngine.getFigur(this.reihe+1*this.vorzeichen, this.spalte+2*this.vorzeichen)!=null) {
				if(dieEngine.getFigur(this.reihe+1*this.vorzeichen, this.spalte+2*this.vorzeichen).getIstWeiß()!= this.istWeiß) {
					this.moeglicheZuege.add(felder[this.reihe+1*this.vorzeichen][this.spalte+2*this.vorzeichen]);
				}
			}
			else {
				this.moeglicheZuege.add(felder[this.reihe+1*this.vorzeichen][this.spalte+2*this.vorzeichen]);
			}
		}
		//Zweites Rechts Unten
		if(this.reihe+2*this.vorzeichen>=0 && this.spalte+1*this.vorzeichen>=0 && this.reihe+2*this.vorzeichen<=7 && this.spalte+1*this.vorzeichen<=7) {
			if(dieEngine.getFigur(this.reihe+2*this.vorzeichen, this.spalte+1*this.vorzeichen)!=null) {
				if(dieEngine.getFigur(this.reihe+2*this.vorzeichen, this.spalte+1*this.vorzeichen).getIstWeiß()!= this.istWeiß) {
					this.moeglicheZuege.add(felder[this.reihe+2*this.vorzeichen][this.spalte+1*this.vorzeichen]);
				}
			}
			else {
				this.moeglicheZuege.add(felder[this.reihe+2*this.vorzeichen][this.spalte+1*this.vorzeichen]);
			}
		}
	}
	
}
