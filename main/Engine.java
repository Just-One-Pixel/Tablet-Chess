package main;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import figuren.Bauer;
import figuren.Dame;
import figuren.Figur;
import figuren.Koenig;
import figuren.Laeufer;
import figuren.Springer;
import figuren.Turm;
import gui.Brett;
import gui.Feld;
import gui.GUI;


public class Engine {
	private GUI dieGUI;

	//private String mattFEN = "k7/3Q4/5Q2/8/8/8/8/7K w - - 0 1";
	//private String rochadeFEN = "rnbqk2r/pppppppp/8/8/8/8/PPPPPPPP/RNBQK2R w KQkq - 0 1";
	//private String testFEN = "7k/Q7/8/8/8/8/8/K7 w - - 0 1";
	//private String schachFEN = "5k2/8/8/8/8/8/8/4K2R w K - 0 1";
	//EntwicklungsFEN "3k4/1P6/8/8/8/8/6p1/4K3 w - - 0 1"
	private String standardFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
	private String ausgewaehltFEN = standardFEN;

	private Spieler weißerSpieler = new Spieler (true, this);
	private Spieler schwarzerSpieler = new Spieler (false, this);
	private Spieler aktuellerSpieler = weißerSpieler;
	private Spieler naechsterSpieler = schwarzerSpieler;
	//Für Brett
	private Brett dasBrett;
	//Für Fokus
	private Feld geklicktesFeld = null;
	private Figur aktiveFigur = null;
	private Figur zielFigur = null;
	//
	private Figur letzterZug = null;
	private Feld altesAusgangsFeld = null;
	//Für fuehreZugAus
	private ArrayList<Feld> moeglicheZuegeSpieler = new ArrayList<Feld>();
	private boolean zugMoeglich = false; 
	private Feld[][] felder;
	//Sonstige
	private boolean schwarzBeginnt = false;
	private int zugNummer = 0;
	private int interneZugNummer = 1;
	private int halbzuege = 0;
	private ArrayList<String> zugListe = new ArrayList<String>();
	private boolean remis = false;
	private boolean kurzeRochade;
	private boolean langeRochade;
	private boolean figurAuswahl = false;

	public static void main(String[] args) {
		new Engine();
	}

	public Engine() {
		this.dasBrett = new Brett(this);
		this.dieGUI = new GUI(dasBrett, this);
		this.felder = dasBrett.getFelder();
	}
	private void fuehreZugAus(int reihe, int spalte) {
		System.out.println("Führe Zug aus!");
		//Altes Feld Markierung setzen
		if(this.altesAusgangsFeld != null) {
			dasBrett.removeMarkierung(altesAusgangsFeld.getReihe(), altesAusgangsFeld.getSpalte());
		}
		this.altesAusgangsFeld = dasBrett.getFeld(aktiveFigur.getReihe(), aktiveFigur.getSpalte());
		dasBrett.getFeld(altesAusgangsFeld.getReihe(), altesAusgangsFeld.getSpalte()).setBackground(Color.blue);

		//Rochade prüfen
		boolean rochade = pruefeRochade();
		if (rochade == false) {
			//Zielfigur entfernen
			if(this.zielFigur != null) {
				if(this.zielFigur.getIstWeiß() == true) {
					System.out.println("Löschen..."+this.zielFigur);
					weißerSpieler.getFiguren().remove(this.zielFigur);
				}
				else {
					System.out.println("Löschen...");
					schwarzerSpieler.getFiguren().remove(this.zielFigur);
				}
			}

			//pruefe Bauer entwicklung
			pruefeBauernEntwicklung();
			//Aktive Figur setzen
			aktiveFigur.setReihe(reihe);
			aktiveFigur.setSpalte(spalte);
		}
		//Für die HalbZüge
		if(this.zielFigur!=null || aktiveFigur instanceof Bauer) {
			this.halbzuege = 0;
		}
		if(aktiveFigur instanceof Bauer == false) {
			this.halbzuege = this.halbzuege +1;
		}
		//EnPassant prüfen
		pruefeEnPassant();
		//Pruefe bauerVorstoß
		pruefeBauerVorstoß();
		//Für Entwicklung
		if(this.figurAuswahl) {
			dasBrett.openFigurAuswahl(aktiveFigur.getIstWeiß(), this.geklicktesFeld.getReihe(), this.geklicktesFeld.getSpalte());
		}
		else {
			//Figur wurde bewegt
			aktiveFigur.setWurdeBewegt();
			aktiveFigur.incAnzahlZuege();
			//Neuer Zug Markieren
			if(this.letzterZug != null) {
				dasBrett.removeMarkierung(letzterZug.getReihe(), letzterZug.getSpalte());
			}
			//LetzterZug setzen
			this.letzterZug = aktiveFigur;
			dasBrett.getFeld(letzterZug.getReihe(), letzterZug.getSpalte()).setBackground(Color.blue);
			//Pruefe Schach
			pruefeSchach();
			showSchach();
			//ZugMoelgich setzen
			this.zugMoeglich = false;
			//ZugNummer erhöhen
			if(schwarzerSpieler.getIstDran()) {
				this.zugNummer = this.zugNummer +1;
				this.interneZugNummer = this.interneZugNummer+1;
			}
			//Spieler istDran setzen
			weißerSpieler.setIstDran(!weißerSpieler.getIstDran());
			schwarzerSpieler.setIstDran(!schwarzerSpieler.getIstDran());
			//Spieler zuweisen
			bestimmeAktuellerSpieler();
			dieGUI.setStatus(aktuellerSpieler.getName()+" is dran!");
			//Auf Schachmatt prüfen
			pruefeSchachmatt();
			//Zug notieren
			notiereZug(altesAusgangsFeld, aktiveFigur, zielFigur, langeRochade, kurzeRochade, aktuellerSpieler.getImSchach(), aktuellerSpieler.getSchachmatt(), null);
			//PruefeHalbZüge
			pruefeHalbzuege();
			//Zug zeichnen
			dasBrett.zeichneBrett();
			//Sound abspielen
			if(this.zielFigur == null) {
				playSound("src/main/move.wav");
			}
			else {
				playSound("src/main/capture.wav");
			}
			//Fokus löschen
			this.aktiveFigur = null;
			this.zielFigur = null;
			this.geklicktesFeld = null;
		}
	}
	private ArrayList<Feld> filtereMoeglicheZuege(ArrayList<Feld> moeglicheZuegeAktuellerSpieler, Figur aktuelleFigur) {
		ArrayList<Feld> moeglicheZuegeGegner = new ArrayList<Feld>();
		ArrayList<Feld> illegaleZuege = new ArrayList<Feld>();
		int richtigeReihe = 0;
		int richtigeSpalte = 0;
		richtigeReihe = aktuelleFigur.getReihe();
		richtigeSpalte = aktuelleFigur.getSpalte();
		Figur testZielfigur = null;
		//Für jeden moeglichen Zug
		for (int x = 0;x<moeglicheZuegeAktuellerSpieler.size();x++) {
			//Zielfigur ermitteln
			if(getFigur(moeglicheZuegeAktuellerSpieler.get(x).getReihe(), moeglicheZuegeAktuellerSpieler.get(x).getSpalte()) != null ) {
				if(getFigur(moeglicheZuegeAktuellerSpieler.get(x).getReihe(), moeglicheZuegeAktuellerSpieler.get(x).getSpalte()).getIstWeiß() == schwarzerSpieler.getIstDran()) {
					testZielfigur = getFigur(moeglicheZuegeAktuellerSpieler.get(x).getReihe(), moeglicheZuegeAktuellerSpieler.get(x).getSpalte());
				}
			}
			else {
				testZielfigur = null;
			}
			//Schein Zug ausführen
			aktuelleFigur.setReihe(moeglicheZuegeAktuellerSpieler.get(x).getReihe());
			aktuelleFigur.setSpalte(moeglicheZuegeAktuellerSpieler.get(x).getSpalte());
			if(testZielfigur != null) {
				this.naechsterSpieler.getFiguren().remove(testZielfigur);
			}
			moeglicheZuegeGegner.removeAll(moeglicheZuegeGegner);
			//Züge des Gegners holen
			for(int e = 0;e< this.naechsterSpieler.getFiguren().size();e++) {
				moeglicheZuegeGegner.addAll(this.naechsterSpieler.getFiguren().get(e).getMoeglicheZuege(dasBrett.getFelder()));
			}
			//Überprüfung
			for(int j = 0;j<moeglicheZuegeGegner.size();j++) {
				if(moeglicheZuegeGegner.get(j).getReihe() == this.aktuellerSpieler.getKoenig().getReihe() && moeglicheZuegeGegner.get(j).getSpalte() == this.aktuellerSpieler.getKoenig().getSpalte()) {
					illegaleZuege.add(moeglicheZuegeAktuellerSpieler.get(x));
				}
			}
			//Schein Zug rückgängig machen
			aktuelleFigur.setReihe(richtigeReihe);
			aktuelleFigur.setSpalte(richtigeSpalte);

			if(testZielfigur != null) {
				this.naechsterSpieler.getFiguren().add(testZielfigur);
			}
		}
		//Besondere Zuege-------------------------------------------------------
		//Rochade------------------------------------------------------- Rochade
		if(aktuelleFigur instanceof Koenig) {
			//Für Kurze
			if(aktuelleFigur.getKurzeRochadeMoeglich()) {
				//Scheinzug ausführen
				testZielfigur = getFigur(aktuelleFigur.getReihe(),aktuelleFigur.getSpalte()+3);
				testZielfigur.setSpalte(5);
				aktuelleFigur.setSpalte(6);
				//Gegner Züge holen
				moeglicheZuegeGegner.removeAll(moeglicheZuegeGegner);
				//Züge des Gegners holen
				for(int e = 0;e< this.naechsterSpieler.getFiguren().size();e++) {
					moeglicheZuegeGegner.addAll(this.naechsterSpieler.getFiguren().get(e).getMoeglicheZuege(dasBrett.getFelder()));
				}
				//Überprüfung
				for(int j = 0;j<moeglicheZuegeGegner.size();j++) {
					if(moeglicheZuegeGegner.get(j).getReihe() == aktuelleFigur.getReihe() && (moeglicheZuegeGegner.get(j).getSpalte() == 4 || moeglicheZuegeGegner.get(j).getSpalte() == 5 || moeglicheZuegeGegner.get(j).getSpalte() == 6|| moeglicheZuegeGegner.get(j).getSpalte() == 7)) {
						aktuelleFigur.setKurzeRochadeMoeglich(false);
					}
				}
				if(aktuelleFigur.getKurzeRochadeMoeglich()) {
					//Hinzufügen von den Zügen
					this.moeglicheZuegeSpieler.add(felder[aktuelleFigur.getReihe()][6]);
					this.moeglicheZuegeSpieler.add(felder[aktuelleFigur.getReihe()][7]);
				}
				//Scheinzug rückgängig
				testZielfigur.setSpalte(7);
				aktuelleFigur.setSpalte(richtigeSpalte);
			}
			//Für lange
			if(aktuelleFigur.getLangeRochadeMoeglich()) {
				//Scheinzug ausführen
				testZielfigur = getFigur(aktuelleFigur.getReihe(),aktuelleFigur.getSpalte()-4);
				testZielfigur.setSpalte(3);
				aktuelleFigur.setSpalte(2);
				//Gegner Züge holen
				moeglicheZuegeGegner.removeAll(moeglicheZuegeGegner);
				//Züge des Gegners holen
				for(int e = 0;e< this.naechsterSpieler.getFiguren().size();e++) {
					moeglicheZuegeGegner.addAll(this.naechsterSpieler.getFiguren().get(e).getMoeglicheZuege(dasBrett.getFelder()));
				}
				//Überprüfung
				for(int j = 0;j<moeglicheZuegeGegner.size();j++) {
					if(moeglicheZuegeGegner.get(j).getReihe() == aktuelleFigur.getReihe() && (moeglicheZuegeGegner.get(j).getSpalte() == 0 || moeglicheZuegeGegner.get(j).getSpalte() == 1 || moeglicheZuegeGegner.get(j).getSpalte() == 2|| moeglicheZuegeGegner.get(j).getSpalte() == 3|| moeglicheZuegeGegner.get(j).getSpalte() == 4)) {
						aktuelleFigur.setLangeRochadeMoeglich(false);
					}
				}
				if(aktuelleFigur.getLangeRochadeMoeglich()) {
					//Hinzufügen von den Zügen
					this.moeglicheZuegeSpieler.add(felder[aktuelleFigur.getReihe()][0]);
					this.moeglicheZuegeSpieler.add(felder[aktuelleFigur.getReihe()][2]);
				}
				//Scheinzug rückgängig
				testZielfigur.setSpalte(0);
				aktuelleFigur.setSpalte(richtigeSpalte);

			}
		}
		//EnPassant------------------------------------------------------- EnPassant
		if(aktuelleFigur instanceof Bauer) {
			//Für Links
			if(aktuelleFigur.getEnPassantLinks()) {
				//Scheinzug ausführen
				aktuelleFigur.setReihe(richtigeReihe-1*aktuelleFigur.getVorzeichen());
				aktuelleFigur.setSpalte(richtigeSpalte-1);

				testZielfigur = getFigur(aktuelleFigur.getReihe()+1*aktuelleFigur.getVorzeichen(),aktuelleFigur.getSpalte());
				this.naechsterSpieler.getFiguren().remove(testZielfigur);
				//Gegner Züge holen
				moeglicheZuegeGegner.removeAll(moeglicheZuegeGegner);
				//Züge des Gegners holen
				for(int e = 0;e< this.naechsterSpieler.getFiguren().size();e++) {
					moeglicheZuegeGegner.addAll(this.naechsterSpieler.getFiguren().get(e).getMoeglicheZuege(dasBrett.getFelder()));
				}
				//Überprüfung
				for(int j = 0;j<moeglicheZuegeGegner.size();j++) {
					if(moeglicheZuegeGegner.get(j).getReihe() == this.aktuellerSpieler.getKoenig().getReihe() && moeglicheZuegeGegner.get(j).getSpalte() == this.aktuellerSpieler.getKoenig().getSpalte()) {
						aktuelleFigur.setEnPassantLinks(false);
					}
				}

				if(aktuelleFigur.getEnPassantLinks()) {
					this.moeglicheZuegeSpieler.add(felder[richtigeReihe-1*aktuelleFigur.getVorzeichen()][richtigeSpalte-1]);
				}
				//Scheinzug rückgängig
				aktuelleFigur.setReihe(richtigeReihe);
				aktuelleFigur.setSpalte(richtigeSpalte);
				this.naechsterSpieler.getFiguren().add(testZielfigur);
			}
			//Für Rechts
			if(aktuelleFigur.getEnPassantRechts()) {
				//Scheinzug ausführen
				aktuelleFigur.setReihe(richtigeReihe-1*aktuelleFigur.getVorzeichen());
				aktuelleFigur.setSpalte(richtigeSpalte+1);

				testZielfigur = getFigur(aktuelleFigur.getReihe()+1*aktuelleFigur.getVorzeichen(),aktuelleFigur.getSpalte());
				this.naechsterSpieler.getFiguren().remove(testZielfigur);
				//Gegner Züge holen
				moeglicheZuegeGegner.removeAll(moeglicheZuegeGegner);
				//Züge des Gegners holen
				for(int e = 0;e< this.naechsterSpieler.getFiguren().size();e++) {
					moeglicheZuegeGegner.addAll(this.naechsterSpieler.getFiguren().get(e).getMoeglicheZuege(dasBrett.getFelder()));
				}
				//Überprüfung
				for(int j = 0;j<moeglicheZuegeGegner.size();j++) {
					if(moeglicheZuegeGegner.get(j).getReihe() == this.aktuellerSpieler.getKoenig().getReihe() && moeglicheZuegeGegner.get(j).getSpalte() == this.aktuellerSpieler.getKoenig().getSpalte()) {
						aktuelleFigur.setEnPassantRechts(false);
					}
				}
				if(aktuelleFigur.getEnPassantRechts()) {
					this.moeglicheZuegeSpieler.add(felder[richtigeReihe-1*aktuelleFigur.getVorzeichen()][richtigeSpalte+1]);
				}
				//Scheinzug rückgängig
				aktuelleFigur.setReihe(richtigeReihe);
				aktuelleFigur.setSpalte(richtigeSpalte);
				this.naechsterSpieler.getFiguren().add(testZielfigur);
			}
		}

		//++++++++++++++++++++Entfernen+++++++++++++++++++++++++//
		//Züge entfernen
		for (int i = 0; i<illegaleZuege.size();i++) {
			moeglicheZuegeAktuellerSpieler.remove(illegaleZuege.get(i));
		}
		return moeglicheZuegeAktuellerSpieler;
	}
	private void pruefeBauernEntwicklung() {
		//Für Bauern entwicklung
		if(aktiveFigur.getClass().equals(Bauer.class) && (this.geklicktesFeld.getReihe() == 0 || this.geklicktesFeld.getReihe() == 7)) {
			this.figurAuswahl = true;
			if(letzterZug != null) {
				dasBrett.removeMarkierung(letzterZug.getReihe(), letzterZug.getSpalte());
			}
		}
	}
	private void pruefeBauerVorstoß() {
		if(this.aktiveFigur.getWurdeBewegt()==false && (geklicktesFeld.getReihe() == 3 || geklicktesFeld.getReihe() == 4)) {
			aktiveFigur.setBauerVorstoß(true);
		}
	}
	private boolean pruefeRochade() {
		boolean rochadeAusgewaehlt = false;
		this.kurzeRochade = false;
		this.langeRochade = false;
		//Für Kurze
		if(this.aktiveFigur.getKurzeRochadeMoeglich()) {
			if(this.geklicktesFeld == dasBrett.getFeld(7, 6) || this.geklicktesFeld == dasBrett.getFeld(7, 7)|| this.geklicktesFeld == dasBrett.getFeld(0, 6) || this.geklicktesFeld == dasBrett.getFeld(0, 7)) {
				rochadeAusgewaehlt = true;
				this.kurzeRochade = true;
				//König setzen
				aktiveFigur.setSpalte(6);
				//Turm setzen
				this.zielFigur = getFigur(aktiveFigur.getReihe(), 7);
				zielFigur.setSpalte(5);
			}
		}
		//Für Lange
		if(this.aktiveFigur.getLangeRochadeMoeglich()) {
			if(this.geklicktesFeld == dasBrett.getFeld(7, 0) || this.geklicktesFeld == dasBrett.getFeld(7, 2) || this.geklicktesFeld == dasBrett.getFeld(0, 0) || this.geklicktesFeld == dasBrett.getFeld(0, 2)) {
				this.langeRochade = true;
				//König setzen
				aktiveFigur.setSpalte(2);
				//Turm setzen
				this.zielFigur = getFigur(aktiveFigur.getReihe(), 0);
				zielFigur.setSpalte(3);
				rochadeAusgewaehlt = true;
			}
		}
		return rochadeAusgewaehlt;
	}
	private void pruefeEnPassant() {
		//Für Links und Rechts
		if(this.aktiveFigur.getEnPassantLinks() || this.aktiveFigur.getEnPassantRechts()) {
			if(this.geklicktesFeld.getReihe() == this.aktiveFigur.getReihe() && this.geklicktesFeld.getSpalte() == this.aktiveFigur.getSpalte()) {
				this.naechsterSpieler.getFiguren().remove(getFigur(this.aktiveFigur.getReihe()+1*aktiveFigur.getVorzeichen(), this.aktiveFigur.getSpalte()));
			}
		}
	}
	private void pruefeSchach() {
		ArrayList<Feld> moeglicheZuegeTest = new ArrayList<Feld>();
		// Schach für weiß
		for (int i = 0; i < schwarzerSpieler.getFiguren().size();i++) {
			moeglicheZuegeTest = schwarzerSpieler.getFiguren().get(i).getMoeglicheZuege(dasBrett.getFelder());
			for (int j = 0; j<moeglicheZuegeTest.size();j++) {
				if(moeglicheZuegeTest.get(j).getReihe() == weißerSpieler.getKoenig().getReihe() && moeglicheZuegeTest.get(j).getSpalte() == weißerSpieler.getKoenig().getSpalte()) {
					weißerSpieler.setImSchach(true);
					break;
				}
				else {
					weißerSpieler.setImSchach(false);
				}
			}
			if(weißerSpieler.getImSchach()) {
				break;
			}
		}
		// Schach für schwarz
		for (int i = 0; i < weißerSpieler.getFiguren().size();i++) {
			moeglicheZuegeTest.removeAll(moeglicheZuegeTest);
			moeglicheZuegeTest = weißerSpieler.getFiguren().get(i).getMoeglicheZuege(dasBrett.getFelder());
			for (int j = 0; j<moeglicheZuegeTest.size();j++) {
				if( moeglicheZuegeTest.get(j).getReihe() == schwarzerSpieler.getKoenig().getReihe() && moeglicheZuegeTest.get(j).getSpalte() == schwarzerSpieler.getKoenig().getSpalte()) {
					schwarzerSpieler.setImSchach(true);

					break;
				}
				else {
					schwarzerSpieler.setImSchach(false);
				}
			}
			if(schwarzerSpieler.getImSchach()) {
				break;
			}
		}
	}
	private void pruefeSchachmatt() {
		ArrayList<Feld> aktuelleZuege = new ArrayList<Feld>();
		ArrayList<Feld> gefilterteZuege = new ArrayList<Feld>();
		//Von Jeder Figur Züge holen
		for(int i = 0;i<aktuellerSpieler.getFiguren().size();i++) {
			aktuelleZuege = aktuellerSpieler.getFiguren().get(i).getMoeglicheZuege(felder);
			gefilterteZuege.addAll(filtereMoeglicheZuege(aktuelleZuege,aktuellerSpieler.getFiguren().get(i)));
		}
		if(gefilterteZuege.size() == 0) {
			if(aktuellerSpieler.getImSchach()) {
				System.out.println("SCHACHMATTT!!!!");
				dieGUI.setStatus("Schachmatt! "+naechsterSpieler.getName()+" gewinnt!");
				aktuellerSpieler.setSchachmatt(true);
				dieGUI.setEingabe();
				playSound("src/main/notify.wav");
			}
			else {
				System.out.println("PATT!!!!");
				dieGUI.setStatus("½–½ | Remis - Patt");
				dieGUI.setEingabe();
				playSound("src/main/notify.wav");
			}
		}
	}
	private void pruefeHalbzuege() {
		if(this.halbzuege >= 75) {
			System.out.println("Remi aufgrund von Halbzügen!!!!");
			this.remis = true;
			dieGUI.setStatus("½–½ | Remis - Halbzuege = 75");
			dieGUI.setEingabe();
			playSound("src/main/notify.wav");
		}
	}
	private void ueberpruefeZugMoeglich() {
		if(this.moeglicheZuegeSpieler != null) {
			for(int i = 0; i<this.moeglicheZuegeSpieler.size();i++) {
				if(this.moeglicheZuegeSpieler.get(i).getReihe() == this.geklicktesFeld.getReihe() && this.moeglicheZuegeSpieler.get(i).getSpalte() == this.geklicktesFeld.getSpalte()) {
					this.zugMoeglich = true;
				}
			}
		}
		else {
			this.zugMoeglich = false;
		}
	}
	private void bestimmeAktuellerSpieler() {
		if(weißerSpieler.getIstDran()==true) {
			this.aktuellerSpieler= weißerSpieler;
			this.naechsterSpieler = schwarzerSpieler;
		}
		else {
			this.aktuellerSpieler= schwarzerSpieler;
			this.naechsterSpieler = weißerSpieler;
		}
	}
	private void showMoeglicheZuege(boolean show) {
		if(show) {
			if(this.moeglicheZuegeSpieler != null) {
				for(int i = 0; i<this.moeglicheZuegeSpieler.size();i++) {
					dasBrett.setMarkierung(this.moeglicheZuegeSpieler.get(i).getReihe(), this.moeglicheZuegeSpieler.get(i).getSpalte());
				}
			}
		}
		else {
			if(this.moeglicheZuegeSpieler != null) {
				for(int i = 0; i<this.moeglicheZuegeSpieler.size();i++) {
					dasBrett.removeMarkierung(this.moeglicheZuegeSpieler.get(i).getReihe(), this.moeglicheZuegeSpieler.get(i).getSpalte());
				}
			}
		}
	}
	private void showSchach() {
		if(weißerSpieler.getImSchach()) {
			dasBrett.getFeld(weißerSpieler.getKoenig().getReihe(), weißerSpieler.getKoenig().getSpalte()).setBackground(Color.red);
		}
		else {
			if(weißerSpieler.getKoenig() != this.letzterZug) {
				dasBrett.getFeld(weißerSpieler.getKoenig().getReihe(), weißerSpieler.getKoenig().getSpalte()).clearBackground();
			}
		}
		if(schwarzerSpieler.getImSchach()) {
			dasBrett.getFeld(schwarzerSpieler.getKoenig().getReihe(), schwarzerSpieler.getKoenig().getSpalte()).setBackground(Color.red);
		}
		else {
			if(schwarzerSpieler.getKoenig() != this.letzterZug) {
				dasBrett.getFeld(schwarzerSpieler.getKoenig().getReihe(), schwarzerSpieler.getKoenig().getSpalte()).clearBackground();
			}
		}
	}
	private void notiereZug(Feld altesAusgangsFeld, Figur aktiveFigur,Figur zielFigur, boolean rochadeLang, boolean rochadeKurz, boolean gegnerSchach, boolean gegnerMatt, Figur entwicklung) {
		String spalteZiel = bestimmeBuchstabenFeld(aktiveFigur.getSpalte());
		String reiheZiel = bestimmeZahlFeld(aktiveFigur.getReihe());
		String spalteStart = bestimmeBuchstabenFeld(altesAusgangsFeld.getSpalte());
		String reiheStart = bestimmeZahlFeld(altesAusgangsFeld.getReihe());
		String zug = null;

		if(!rochadeLang && !rochadeKurz) {
			if(zielFigur == null) {
				zug = bestimmeBuchstabenFigur(aktiveFigur)+spalteStart+reiheStart+"–"+spalteZiel+reiheZiel;
			}
			if(zielFigur != null) {
				zug = bestimmeBuchstabenFigur(aktiveFigur)+spalteStart+reiheStart+"x"+bestimmeBuchstabenFigur(zielFigur)+spalteZiel+reiheZiel;
			}
		}
		if(rochadeLang) {
			zug = "O-O-O";
		}
		if(rochadeKurz) {
			zug = "O-O";
		}
		if(gegnerSchach && !gegnerMatt) {
			zug = zug+"+";
		}
		if(gegnerMatt) {
			zug = zug+"#";
		}
		if(entwicklung != null) {
			if(entwicklung instanceof Dame) {
				zug = zug+"=D";
			}
			if(entwicklung instanceof Laeufer) {
				zug = zug+"=L";
			}
			if(entwicklung instanceof Springer) {
				zug = zug+"=S";
			}
			if(entwicklung instanceof Turm) {
				zug = zug+"=T";
			}
		}
		if(weißerSpieler.getIstDran() == false) {
			zugListe.add(this.zugNummer+". "+zug);
			zug = this.zugNummer+". "+zug;
			dieGUI.addRow();
			dieGUI.setValue(zug, this.interneZugNummer-1, 0);
		}
		else {
			if(this.interneZugNummer == 2 && schwarzBeginnt == true) {
				dieGUI.addRow();
				dieGUI.setValue(String.valueOf(this.zugNummer-1)+".", this.interneZugNummer-2, 0);
				dieGUI.setValue(zug, this.interneZugNummer-2, 1);
			}
			else {
				dieGUI.setValue(zug, this.interneZugNummer-2, 1);
			}
		}
	}
	private String bestimmeBuchstabenFeld(int spalte) {
		String spalteString = "";
		switch(spalte) {
		case 0:
			spalteString = "a";
			break;
		case 1:
			spalteString = "b";
			break;
		case 2:
			spalteString = "c";
			break;
		case 3:
			spalteString = "d";
			break;
		case 4:
			spalteString = "e";
			break;
		case 5:
			spalteString = "f";
			break;
		case 6:
			spalteString = "g";
			break;
		case 7:
			spalteString = "h";
			break;
		}
		return spalteString;
	}
	private String bestimmeBuchstabenFigur(Figur figur) {
		String figurString = "";
		if(figur instanceof Bauer) {
			figurString = "";
		}
		if(figur instanceof Turm) {
			figurString = "T";
		}
		if(figur instanceof Springer) {
			figurString = "S";
		}
		if(figur instanceof Laeufer) {
			figurString = "L";
		}
		if(figur instanceof Dame) {
			figurString = "D";
		}
		if(figur instanceof Koenig) {
			figurString = "K";
		}
		return figurString;
	}
	private String bestimmeZahlFeld(int zahl) {
		String zahlString = "";
		switch(zahl) {
		case 0:
			zahlString = "8";
			break;
		case 1:
			zahlString = "7";
			break;
		case 2:
			zahlString = "6";
			break;
		case 3:
			zahlString = "5";
			break;
		case 4:
			zahlString = "4";
			break;
		case 5:
			zahlString = "3";
			break;
		case 6:
			zahlString = "2";
			break;
		case 7:
			zahlString = "1";
			break;
		}
		return zahlString;
	}
	public void loadFEN() {
		int abschnitt = 0;
		int aktuelleReihe = 0;
		int aktuelleSpalte = 0;
		weißerSpieler.getFiguren().removeAll(weißerSpieler.getFiguren());
		schwarzerSpieler.getFiguren().removeAll(schwarzerSpieler.getFiguren());
		for(int i = 0; i< ausgewaehltFEN.length();i++) {
			//Abschnitt wechseln
			if(ausgewaehltFEN.charAt(i) == ' ') {
				abschnitt = abschnitt +1;
				i = i+1;
			}
			//Figuren aufstellen
			if(abschnitt == 0) {
				switch(ausgewaehltFEN.charAt(i)) {
				case 'r':
					schwarzerSpieler.getFiguren().add(new Turm(aktuelleReihe, aktuelleSpalte, false, this));
					aktuelleSpalte = aktuelleSpalte+1;
					break;
				case 'n':
					schwarzerSpieler.getFiguren().add(new Springer(aktuelleReihe, aktuelleSpalte, false, this));
					aktuelleSpalte = aktuelleSpalte+1;
					break;
				case 'b':
					schwarzerSpieler.getFiguren().add(new Laeufer(aktuelleReihe, aktuelleSpalte, false, this));
					aktuelleSpalte = aktuelleSpalte+1;
					break;
				case 'q':
					schwarzerSpieler.getFiguren().add(new Dame(aktuelleReihe, aktuelleSpalte, false, this));
					aktuelleSpalte = aktuelleSpalte+1;
					break;
				case 'k':
					schwarzerSpieler.getFiguren().add(new Koenig(aktuelleReihe, aktuelleSpalte, false, this));
					aktuelleSpalte = aktuelleSpalte+1;
					break;
				case 'p':
					schwarzerSpieler.getFiguren().add(new Bauer(aktuelleReihe, aktuelleSpalte, false, this));
					aktuelleSpalte = aktuelleSpalte+1;
					break;
				case 'R':
					weißerSpieler.getFiguren().add(new Turm(aktuelleReihe, aktuelleSpalte, true, this));
					aktuelleSpalte = aktuelleSpalte+1;
					break;
				case 'N':
					weißerSpieler.getFiguren().add(new Springer(aktuelleReihe, aktuelleSpalte, true, this));
					aktuelleSpalte = aktuelleSpalte+1;
					break;
				case 'B':
					weißerSpieler.getFiguren().add(new Laeufer(aktuelleReihe, aktuelleSpalte, true, this));
					aktuelleSpalte = aktuelleSpalte+1;
					break;
				case 'Q':
					weißerSpieler.getFiguren().add(new Dame(aktuelleReihe, aktuelleSpalte, true, this));
					aktuelleSpalte = aktuelleSpalte+1;
					break;
				case 'K':
					weißerSpieler.getFiguren().add(new Koenig(aktuelleReihe, aktuelleSpalte, true, this));
					aktuelleSpalte = aktuelleSpalte+1;
					break;
				case 'P':
					weißerSpieler.getFiguren().add(new Bauer(aktuelleReihe, aktuelleSpalte, true, this));
					aktuelleSpalte = aktuelleSpalte+1;
					break;
				case '/':
					aktuelleReihe = aktuelleReihe + 1;
					aktuelleSpalte = 0;
					break;
				default:
					//Fals Char Zahl ist
					if(Character.isDigit(ausgewaehltFEN.charAt(i)) == true) {
						int zahl = Integer.parseInt(String.valueOf(ausgewaehltFEN.charAt(i)));
						aktuelleSpalte = aktuelleSpalte+zahl;
						if(aktuelleSpalte == 8) {
							aktuelleSpalte = 0;
						}
					}
				}
			}
			//Spieler am Zug
			if(abschnitt == 1) {
				switch(ausgewaehltFEN.charAt(i)) {
				case 'w':
					weißerSpieler.setIstDran(true);
					schwarzerSpieler.setIstDran(false);
					this.schwarzBeginnt = false;
					break;
				case 'b':
					weißerSpieler.setIstDran(false);
					schwarzerSpieler.setIstDran(true);
					this.schwarzBeginnt = true;
					break;
				}
			}
			//Rochade Moeglich
			if(abschnitt == 2) {
				switch(ausgewaehltFEN.charAt(i)) {
				case 'K':
					weißerSpieler.getKoenig().setKurzeRochadeMoeglichFEN(true);
					break;
				case 'k':
					schwarzerSpieler.getKoenig().setKurzeRochadeMoeglichFEN(true);
					break;
				case 'Q':
					weißerSpieler.getKoenig().setLangeRochadeMoeglichFEN(true);
					break;
				case 'q':
					schwarzerSpieler.getKoenig().setLangeRochadeMoeglichFEN(true);
					break;
				}
			}
			//EnPassant möglich
			if(abschnitt == 3) {
			}
			//Halbzüge
			if(abschnitt == 4) {
				if(Character.isDigit(ausgewaehltFEN.charAt(i+1)) == true){
					this.halbzuege = Integer.valueOf("" + ausgewaehltFEN.charAt(i) + ausgewaehltFEN.charAt(i+1));
					i = i+1;
				}
				else {
					this.halbzuege = Integer.parseInt(String.valueOf(ausgewaehltFEN.charAt(i)));
				}

			}
			//Zug Nummer
			if(abschnitt == 5) {
				if(i+1<ausgewaehltFEN.length()) {
					if(i+2<ausgewaehltFEN.length()) {
						this.zugNummer = Integer.valueOf("" + ausgewaehltFEN.charAt(i)+ausgewaehltFEN.charAt(i+1)+ausgewaehltFEN.charAt(i+2));
						break;
					}
					else {
						this.zugNummer = Integer.valueOf("" + ausgewaehltFEN.charAt(i)+ausgewaehltFEN.charAt(i+1));
						break;
					}
				}
				else {
					this.zugNummer = Integer.valueOf("" + ausgewaehltFEN.charAt(i));
					break;
				}
			}
		}
		bestimmeAktuellerSpieler();
		dasBrett.zeichneBrett();
	}
	public void neuesSpiel() {
		this.aktiveFigur = null;
		this.geklicktesFeld = null;
		this.moeglicheZuegeSpieler.removeAll(moeglicheZuegeSpieler);
		this.letzterZug = null;
		this.altesAusgangsFeld = null;
		this.remis = false;
		this.zugListe.clear();
		this.zugNummer = 0;
		this.figurAuswahl = false;
		this.interneZugNummer = 1;
		weißerSpieler.setImSchach(false);
		weißerSpieler.setSchachmatt(false);
		weißerSpieler.setIstDran(true);
		schwarzerSpieler.setImSchach(false);
		schwarzerSpieler.setSchachmatt(false);
		schwarzerSpieler.setIstDran(false);
		dieGUI.loescheZuege();
		bestimmeAktuellerSpieler();
		ausgewaehltFEN = dieGUI.getFEN();
		if( ausgewaehltFEN.length()< 10) {
			ausgewaehltFEN = standardFEN;
		}
		loadFEN();
		//Für Status
		weißerSpieler.setName(this.dieGUI.getSpielerNamen(true));
		schwarzerSpieler.setName(this.dieGUI.getSpielerNamen(false));
		dieGUI.setStatus(aktuellerSpieler.getName()+" ist dran!");

		removeAlleMarkierungen();
		
		pruefeSchach();
		showSchach();
		pruefeSchachmatt();
	}
	private void removeAlleMarkierungen() {
		for (int i = 0; i< 8;i++) {
			for (int j = 0; j<8;j++) {
				dasBrett.removeMarkierung(i, j);
			}
		}
	}
	//Setter
	public void setFokus(int reihe, int spalte) {
		if(remis == false && weißerSpieler.getSchachmatt() == false && schwarzerSpieler.getSchachmatt() == false && figurAuswahl == false) {
			if(weißerSpieler.getSchachmatt() == false && schwarzerSpieler.getSchachmatt() == false && this.remis == false) {
				this.geklicktesFeld = dasBrett.getFeld(reihe, spalte);
				//Ziel Feld erkennen
				if(this.aktiveFigur != null ) {
					//Fokus Markierung entfernen
					dasBrett.removeMarkierung(aktiveFigur.getReihe(), aktiveFigur.getSpalte());
					//Moegliche Zuege Markierung entfernen
					showMoeglicheZuege(false);
					showSchach();
					//Zielfigur erkennen
					if(getFigur(geklicktesFeld.getReihe(), geklicktesFeld.getSpalte())!= null) {
						this.zielFigur = getFigur(geklicktesFeld.getReihe(), geklicktesFeld.getSpalte());
					}
					//Überprüfe zugMoeglich
					this.ueberpruefeZugMoeglich();
					//Fuehre Zug aus
					if(this.zugMoeglich == true) {
						fuehreZugAus(reihe, spalte);
					}
					else {
						this.aktiveFigur = null;
						this.zielFigur = null;
					}
				}
				//Aktive Figur erkennen
				if(this.aktiveFigur == null && this.geklicktesFeld != null) {
					System.out.println("Fokus:");
					if(this.getFigur(this.geklicktesFeld.getReihe(), this.geklicktesFeld.getSpalte()) != null) {
						if(weißerSpieler.getIstDran() == this.getFigur(this.geklicktesFeld.getReihe(), this.geklicktesFeld.getSpalte()).getIstWeiß()) {
							//AusgangsFigur erkennen
							this.aktiveFigur = this.getFigur(this.geklicktesFeld.getReihe(), this.geklicktesFeld.getSpalte());
							dasBrett.setFokusMarkierung(reihe, spalte, Color.green);
							//Schach Überpruefen
							pruefeSchach();
							showSchach();
							//Moegliche Zuege bekommen
							this.moeglicheZuegeSpieler = this.aktiveFigur.getMoeglicheZuege(dasBrett.getFelder());
							//Moegliche Züge filtern, falls Koenig im Schach steht
							this.moeglicheZuegeSpieler = filtereMoeglicheZuege(this.moeglicheZuegeSpieler, aktiveFigur);
							showMoeglicheZuege(true);

						}
					}
					System.out.println("Aktive Figur: "+aktiveFigur);
				}
			}
		}
	}
	public void setRemis() {
		this.remis = true;
	}
	public void aktualisierFenster() {
		dieGUI.aktualisereFenster();
	}
	public void setFigur(Figur figur) {	
		Figur alteFigur = this.aktiveFigur;
		if(figur.getIstWeiß()) {
			weißerSpieler.getFiguren().remove(alteFigur);
			weißerSpieler.getFiguren().add(figur);
			aktiveFigur = figur;
		}
		else {
			schwarzerSpieler.getFiguren().remove(alteFigur);
			schwarzerSpieler.getFiguren().add(figur);
			aktiveFigur = figur;
		}
		
		////Aus fuehreZugAus()-----------------------
		//Figur wurde bewegt
		aktiveFigur.setWurdeBewegt();
		aktiveFigur.incAnzahlZuege();
		//Neuer Zug Markieren
		if(this.letzterZug != null) {
			dasBrett.removeMarkierung(letzterZug.getReihe(), letzterZug.getSpalte());
		}
		//LetzterZug setzen
		this.letzterZug = aktiveFigur;
		dasBrett.getFeld(letzterZug.getReihe(), letzterZug.getSpalte()).setBackground(Color.blue);
		//Pruefe Schach
		pruefeSchach();
		showSchach();
		//ZugMoelgich setzen
		this.zugMoeglich = false;
		//ZugNummer erhöhen
		if(schwarzerSpieler.getIstDran()) {
			this.zugNummer = this.zugNummer +1;
			this.interneZugNummer = this.interneZugNummer+1;
		}
		//Spieler istDran setzen
		weißerSpieler.setIstDran(!weißerSpieler.getIstDran());
		schwarzerSpieler.setIstDran(!schwarzerSpieler.getIstDran());
		//Spieler zuweisen
		bestimmeAktuellerSpieler();
		dieGUI.setStatus(aktuellerSpieler.getName()+" is dran!");
		//Auf Schachmatt prüfen
		pruefeSchachmatt();
		//Zug notieren
		notiereZug(altesAusgangsFeld, aktiveFigur, zielFigur, langeRochade, kurzeRochade, aktuellerSpieler.getImSchach(), aktuellerSpieler.getSchachmatt(), aktiveFigur);
		//PruefeHalbZüge
		pruefeHalbzuege();
		//Zug zeichnen
		dasBrett.zeichneBrett();
		//Sound abspielen
		if(this.zielFigur == null) {
			playSound("src/main/move.wav");
		}
		else {
			playSound("src/main/capture.wav");
		}
		//Fokus löschen
		this.aktiveFigur = null;
		this.zielFigur = null;
		this.geklicktesFeld = null;
		
		this.figurAuswahl = false;
	}
	//Getter
	public Figur getFigur(int reihe, int spalte) {
		//Weiße
		for (Figur figur : weißerSpieler.getFiguren()) {
			if (figur.getReihe() == reihe && figur.getSpalte() == spalte) {
				return figur;
			}
		}
		//Schwarze
		for (Figur figur : schwarzerSpieler.getFiguren()) {
			if (figur.getReihe() == reihe && figur.getSpalte() == spalte) {
				return figur;
			}
		}
		return null;
	}
	public ArrayList<Figur> getFiguren(boolean istWeiß){
		if(istWeiß) {
			return weißerSpieler.getFiguren();
		}
		else {
			return schwarzerSpieler.getFiguren();
		}
	}
	public Brett getBrett() {
		return dasBrett;
	}
	public Figur getLetzterZug() {
		return this.letzterZug;
	}
	public ArrayList<Feld> getZuege(){
		return this.moeglicheZuegeSpieler;
	}
	public void playSound(String sound) {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(sound).getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch(Exception ex) {
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}
}
