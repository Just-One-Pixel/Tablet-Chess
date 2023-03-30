package main;

import java.util.ArrayList;
import figuren.Figur;
import figuren.Koenig;

public class Spieler {
	//Variblen
	private boolean istWeiß = false;
	private boolean istDran = false;
	private String name = "";
	//private Engine dieEngine;
	private boolean schachMatt = false;
	private boolean imSchach = false;
	private Figur koenig = null;
	private ArrayList<Figur> figurenSpieler = new ArrayList<Figur>();
	
	public Spieler(boolean istWeiß, Engine dieEngine) {
		//Zuordnung
		this.istWeiß = istWeiß;
		//this.dieEngine = dieEngine;
		//Beginner festlegen
		if(istWeiß == true) {
			this.istDran = true;
		}
	}
	//Methoden

	//Setter
	public void setIstDran(boolean istDran) {
		this.istDran = istDran;
	}
	public void setSchachmatt(boolean schachmatt) {
		this.schachMatt = schachmatt;
	}
	public void setImSchach(boolean imSchach) {
		this.imSchach = imSchach;
	}
	public void setName(String name) {
		this.name = name;
		if(name.length() < 1) {
			if(istWeiß == true) {
				this.name = "Weiß";
			}
			else {
				this.name = "Schwarz";
			}
		}
	}

	//Getter
	public ArrayList<Figur> getFiguren() {
		return this.figurenSpieler;
	}
	public boolean getIstDran() {
		return this.istDran;
	}
	public Figur getKoenig() {
		for (int i = 0;i<figurenSpieler.size();i++) {
			if(figurenSpieler.get(i) instanceof Koenig) {
				this.koenig = figurenSpieler.get(i);
			}
		}
		return this.koenig;
	}
	public boolean getSchachmatt() {
		return this.schachMatt;
	}
	public boolean getImSchach() {
		return this.imSchach;
	}
	public String getName() {
		return this.name;
	}
}
