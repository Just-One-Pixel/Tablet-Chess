package figuren;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import gui.Feld;
import main.Engine;

public class Turm extends Figur{

	public Turm(int reihe, int spalte, boolean istWeiß, Engine dieEngine) {
		this.reihe = reihe;
		this.spalte = spalte;
		this.istWeiß =istWeiß;
		this.dieEngine = dieEngine;
		//JPanel Einstellungen
		this.setVerticalAlignment(CENTER);
		this.setHorizontalAlignment(CENTER);
		//Bild laden
		if(this.istWeiß == true) {
			img = new ImageIcon("src/figuren/bilder/WTurm.png");
		}
		else {
			img = new ImageIcon("src/figuren/bilder/STurm.png");
		}
		imageLaden(this, img);
		bestimmeVorzeichen(this.istWeiß);
	}

	@Override
	public ArrayList<Feld> getMoeglicheZuege(Feld[][] felder) {
		this.moeglicheZuege.removeAll(moeglicheZuege);
		pruefeVertikal(felder);
		pruefeHorizontal(felder);
		return this.moeglicheZuege;
	}
	
}
