package figuren;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import gui.Feld;
import main.Engine;

public class Laeufer extends Figur{


	public Laeufer(int reihe, int spalte, boolean istWeiß, Engine dieEngine) {
		this.reihe = reihe;
		this.spalte = spalte;
		this.istWeiß =istWeiß;
		this.dieEngine = dieEngine;
		this.dieEngine = dieEngine;
		//JPanel Einstellungen
		this.setVerticalAlignment(CENTER);
		this.setHorizontalAlignment(CENTER);
		//Bild laden
		if(this.istWeiß == true) {
			img = new ImageIcon("figuren/bilder/WLaeufer.png");
		}
		else {
			img = new ImageIcon("figuren/bilder/SLaeufer.png");
		}
		imageLaden(this, img);
		bestimmeVorzeichen(this.istWeiß);
	}

	@Override
	public ArrayList<Feld> getMoeglicheZuege(Feld[][] felder) {
		this.moeglicheZuege.removeAll(moeglicheZuege);
		pruefeDiagonal(felder);
		return this.moeglicheZuege;
	}
	
}
