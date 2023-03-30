package gui;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import main.Engine;



public class GUI {
	//Variablen
	private Engine dieEngine;
	private Brett dasBrett;
	private SeitenPanel dasSeitenPanel;
	
	private JFrame frame;
	private Image icon = new ImageIcon("figuren/bilder/WSpringer.png").getImage();
	
	
	
	public GUI(Brett dasBrett, Engine dieEngine) {
		this.dieEngine = dieEngine;
		this.dasBrett = dasBrett;
		dasSeitenPanel = new SeitenPanel(this.dieEngine);
		initialize();
	}
	private void initialize() {
		////////JFrame initialisierung////////
		frame = new JFrame();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.requestFocus();
		frame.setIconImage(icon);
		frame.setTitle("Schach by Lutz");
		//frame.setBounds(600, 50, 614, 637);
		frame.setBounds(300, 50, 815, 639);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Wichtig für Brett
		frame.setLayout(null);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(Color.black);
		//SeitenPanel hinzufügen
		frame.getContentPane().add(dasSeitenPanel);
		//ContentPane extern
		frame.getContentPane().add(dasBrett);
		
		aktualisereFenster();
	}
	public void addRow() {
		dasSeitenPanel.addRow();
	}
	public void setValue(String zug, int r, int c) {
		dasSeitenPanel.setValue(zug, r,c);
	}
	public void loescheZuege() {
		dasSeitenPanel.loescheZuege();
	}
	public String getSpielerNamen(boolean istWeiß) {
		return dasSeitenPanel.getSpielerNamen(istWeiß);
	}
	public String getFEN() {
		return dasSeitenPanel.getFEN();
	}
	public void setStatus(String status) {
		dasSeitenPanel.setStatus(status);
	}
	public void setEingabe() {
		dasSeitenPanel.setEingabe();
	}
	public void aktualisereFenster() {
		frame.validate();
		frame.repaint();
	}
}
