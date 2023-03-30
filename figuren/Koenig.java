package figuren;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import gui.Feld;
import main.Engine;

public class Koenig extends Figur{

	public Koenig(int reihe, int spalte, boolean istWeiß, Engine dieEngine) {
		this.reihe = reihe;
		this.spalte = spalte;
		this.istWeiß =istWeiß;
		this.dieEngine = dieEngine;
		//JPanel Einstellungen
		this.setVerticalAlignment(CENTER);
		this.setHorizontalAlignment(CENTER);
		//Bild laden
		if(this.istWeiß == true) {
			img = new ImageIcon("src/figuren/bilder/WKoenig.png");
		}
		else {
			img = new ImageIcon("src/figuren/bilder/SKoenig.png");
		}
		imageLaden(this, img);
		bestimmeVorzeichen(this.istWeiß);
	}

	@Override
	public ArrayList<Feld> getMoeglicheZuege(Feld[][] felder) {
		//this.moeglicheZuege.removeAll(moeglicheZuege);
		this.moeglicheZuege.clear();
		pruefeUmfeld(felder);
		if(this.kurzeRochadeMoeglichFEN) {
			pruefeKurzeRochade(felder);
		}
		if(this.langeRochadeMoeglichFEN) {
			pruefeLangeRochade(felder);
		}
		return this.moeglicheZuege;
	}
	//Überprüfung
	private void pruefeLangeRochade(Feld[][] felder) {
		this.langeRochadeMoeglich = true;
		if (this.wurdeBewegt == false && dieEngine.getFigur(this.reihe,0)!= null) {
			if(dieEngine.getFigur(this.reihe,0).getWurdeBewegt() == false && dieEngine.getFigur(this.reihe,0) instanceof Turm) {
				for (int i = 1; i<=3;i++) {
					if(dieEngine.getFigur(this.reihe,this.spalte-i) != null) {
						this.langeRochadeMoeglich = false;
						break;
					}
				}
			}
			else {
				this.langeRochadeMoeglich = false;
			}
		}
		else {
			this.langeRochadeMoeglich = false;
		}
	}
	private void pruefeKurzeRochade(Feld[][] felder) {
		this.kurzeRochadeMoeglich = true;
		if (this.wurdeBewegt == false && dieEngine.getFigur(this.reihe, 7) != null) {
			if(dieEngine.getFigur(this.reihe, 7).getWurdeBewegt() == false && dieEngine.getFigur(this.reihe,7) instanceof Turm) {
				for (int i = 1; i<=2;i++) {
					if(dieEngine.getFigur(this.reihe, this.spalte+i) != null) {
						this.kurzeRochadeMoeglich = false;
						break;
					}
				}
			}
			else {
				this.kurzeRochadeMoeglich = false;
			}
		}
		else {
			this.kurzeRochadeMoeglich = false;
		}
	}
	private void pruefeUmfeld(Feld[][] felder) {
		//Prüfe Oberes Feld
		if(this.reihe-1*this.vorzeichen >= 0 && this.reihe-1*this.vorzeichen<=7) {
			if(dieEngine.getFigur(this.reihe-1*this.vorzeichen, this.spalte) != null) {
				if(dieEngine.getFigur(this.reihe-1*this.vorzeichen, this.spalte).getIstWeiß() != this.istWeiß ){
					this.moeglicheZuege.add(felder[this.reihe-1*this.vorzeichen][this.spalte]);
				}
			}
			else {
				this.moeglicheZuege.add(felder[this.reihe-1*this.vorzeichen][this.spalte]);
			}
		}
		//Prüfe Unteres Feld
		if(this.reihe+1*this.vorzeichen >= 0 && this.reihe+1*this.vorzeichen<=7) {
			if(dieEngine.getFigur(this.reihe+1*this.vorzeichen, this.spalte) != null) {
				if(dieEngine.getFigur(this.reihe+1*this.vorzeichen, this.spalte).getIstWeiß() != this.istWeiß ){
					this.moeglicheZuege.add(felder[this.reihe+1*this.vorzeichen][this.spalte]);
				}
			}
			else {
				this.moeglicheZuege.add(felder[this.reihe+1*this.vorzeichen][this.spalte]);
			}
		}
		//Prüfe Rechtes Feld
		if(this.spalte+1*this.vorzeichen >= 0 && this.spalte+1*this.vorzeichen<=7) {
			if(dieEngine.getFigur(this.reihe, this.spalte+1*this.vorzeichen) != null) {
				if(dieEngine.getFigur(this.reihe, this.spalte+1*this.vorzeichen).getIstWeiß() != this.istWeiß ){
					this.moeglicheZuege.add(felder[this.reihe][this.spalte+1*this.vorzeichen]);
				}
			}
			else {
				this.moeglicheZuege.add(felder[this.reihe][this.spalte+1*this.vorzeichen]);
			}
		}
		//Prüfe Linkes Feld
		if(this.spalte-1*this.vorzeichen >= 0 && this.spalte-1*this.vorzeichen<=7) {
			if(dieEngine.getFigur(this.reihe, this.spalte-1*this.vorzeichen) != null) {
				if(dieEngine.getFigur(this.reihe, this.spalte-1*this.vorzeichen).getIstWeiß() != this.istWeiß ){
					this.moeglicheZuege.add(felder[this.reihe][this.spalte-1*this.vorzeichen]);
				}
			}
			else {
				this.moeglicheZuege.add(felder[this.reihe][this.spalte-1*this.vorzeichen]);
			}
		}
		//Prüfe Rechts Oben
		if(this.reihe-1*this.vorzeichen >= 0 && this.reihe-1*this.vorzeichen<=7 && this.spalte+1*this.vorzeichen >= 0 && this.spalte+1*this.vorzeichen <= 7) {
			if(dieEngine.getFigur(this.reihe-1*this.vorzeichen, this.spalte+1*this.vorzeichen) != null) {
				if(dieEngine.getFigur(this.reihe-1*this.vorzeichen, this.spalte+1*this.vorzeichen).getIstWeiß() != this.istWeiß ){
					this.moeglicheZuege.add(felder[this.reihe-1*this.vorzeichen][this.spalte+1*this.vorzeichen]);
				}
			}
			else {
				this.moeglicheZuege.add(felder[this.reihe-1*this.vorzeichen][this.spalte+1*this.vorzeichen]);
			}
		}
		//Prüfe Rechts Unten
		if(this.reihe+1*this.vorzeichen >= 0 && this.reihe+1*this.vorzeichen<=7 && this.spalte+1*this.vorzeichen >= 0 && this.spalte+1*this.vorzeichen <= 7) {
			if(dieEngine.getFigur(this.reihe+1*this.vorzeichen, this.spalte+1*this.vorzeichen) != null) {
				if(dieEngine.getFigur(this.reihe+1*this.vorzeichen, this.spalte+1*this.vorzeichen).getIstWeiß() != this.istWeiß ){
					this.moeglicheZuege.add(felder[this.reihe+1*this.vorzeichen][this.spalte+1*this.vorzeichen]);
				}
			}
			else {
				this.moeglicheZuege.add(felder[this.reihe+1*this.vorzeichen][this.spalte+1*this.vorzeichen]);
			}
		}
		//Prüfe Links Unten
		if(this.reihe+1*this.vorzeichen >= 0 && this.reihe+1*this.vorzeichen<=7 && this.spalte-1*this.vorzeichen >= 0 && this.spalte-1*this.vorzeichen <= 7) {
			if(dieEngine.getFigur(this.reihe+1*this.vorzeichen, this.spalte-1*this.vorzeichen) != null) {
				if(dieEngine.getFigur(this.reihe+1*this.vorzeichen, this.spalte-1*this.vorzeichen).getIstWeiß() != this.istWeiß ){
					this.moeglicheZuege.add(felder[this.reihe+1*this.vorzeichen][this.spalte-1*this.vorzeichen]);
				}
			}
			else {
				this.moeglicheZuege.add(felder[this.reihe+1*this.vorzeichen][this.spalte-1*this.vorzeichen]);
			}
		}
		//Prüfe Links Oben
		if(this.reihe-1*this.vorzeichen >= 0 && this.reihe-1*this.vorzeichen<=7 && this.spalte-1*this.vorzeichen >= 0 && this.spalte-1*this.vorzeichen <= 7) {
			if(dieEngine.getFigur(this.reihe-1*this.vorzeichen, this.spalte-1*this.vorzeichen) != null) {
				if(dieEngine.getFigur(this.reihe-1*this.vorzeichen, this.spalte-1*this.vorzeichen).getIstWeiß() != this.istWeiß ){
					this.moeglicheZuege.add(felder[this.reihe-1*this.vorzeichen][this.spalte-1*this.vorzeichen]);
				}
			}
			else {
				this.moeglicheZuege.add(felder[this.reihe-1*this.vorzeichen][this.spalte-1*this.vorzeichen]);
			}
		}
	}

}
