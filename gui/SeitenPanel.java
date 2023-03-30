package gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import main.Engine;

public class SeitenPanel extends JPanel {

	//private Engine dieEngine;
	private JButton button;
	private JButton buttonRemis;
	private JScrollPane scrollPanel;
	private JLabel weißerSpielerText;
	private JTextField weißerSpielerField;
	private JLabel schwarzerSpielerText;
	private JTextField schwarzerSpielerField;
	private JTextField fenField;
	private JLabel fenText;
	private JLabel statusText;
	private JLabel spieler1;
	private JLabel spieler2;
	private Font font = new Font("",1,12);
	private JTable jt = new JTable();
	private DefaultTableModel model = new DefaultTableModel();
	private Object[] empty = {"", ""};
	private StyleContext context = new StyleContext();
	private javax.swing.text.Style style = context.getStyle(StyleContext.DEFAULT_STYLE);

	

	public SeitenPanel(Engine dieEngine) {
		//this.dieEngine = dieEngine;
		//JPanel EInstellungen
		this.setBounds(600, 0, 200, 600);
		this.setBackground(Color.decode("#263661"));
		this.setBorder(new LineBorder(Color.black));
		this.setLayout(null);
		this.setVisible(true);

		//Neues Spiel Button
		button = new JButton("Neues Spiel");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dieEngine.neuesSpiel();
				dieEngine.playSound("src/main/notify.wav");
			}
		});
		button.setBounds(0, 0, 200, 50);
		
		//Remis-Button
		buttonRemis = new JButton("Remis erklären");
		buttonRemis.setVisible(false);
		buttonRemis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dieEngine.setRemis();
				setEingabe();
				setStatus("½–½ | Remis - Durch Spieler");
				dieEngine.playSound("src/main/notify.wav");
			}
		});
		buttonRemis.setBounds(0, 55, 200, 50);
		
		StyleConstants.setAlignment(style, StyleConstants.ALIGN_CENTER);
		//WeißerSpieler JTextPane/JTextField Spieler1
		spieler1 = new JLabel();
		spieler1.setBounds(0, 195, 200, 25);
		spieler1.setText("Weißer Spieler:");
		spieler1.setOpaque(true);
		spieler1.setVerticalAlignment(JLabel.CENTER);
		spieler1.setBackground(Color.decode("#263661"));
		spieler1.setForeground(Color.white);
		spieler1.setVisible(true);
		
		weißerSpielerText = new JLabel();
		weißerSpielerText.setBounds(0,220,200,25);
		weißerSpielerText.setText("WeißerSpieler");
		weißerSpielerText.setOpaque(true);
		weißerSpielerText.setVerticalAlignment(JLabel.CENTER);
		weißerSpielerText.setHorizontalAlignment(JLabel.CENTER);
		weißerSpielerText.setVisible(false);
		
		weißerSpielerField = new JTextField();
		weißerSpielerField.setText("Spieler 1");
		weißerSpielerField.setBounds(0, 220, 200, 25);
		weißerSpielerField.setVisible(true);
		//SchwarzerSpieler JTextPane/JTextField Spieler 2
		spieler2 = new JLabel();
		spieler2.setBounds(0, 120, 200, 25);
		spieler2.setText("Schwarzer Spieler:");
		spieler2.setOpaque(true);
		spieler2.setVerticalAlignment(JLabel.CENTER);
		spieler2.setBackground(Color.decode("#263661"));
		spieler2.setForeground(Color.white);
		spieler2.setVisible(true);
		
		schwarzerSpielerText = new JLabel();
		schwarzerSpielerText.setBounds(0,145,200,25);
		schwarzerSpielerText.setText("Schwarzer Spieler");
		schwarzerSpielerText.setBackground(Color.black);
		schwarzerSpielerText.setForeground(Color.white);
		schwarzerSpielerText.setOpaque(true);
		schwarzerSpielerText.setVerticalAlignment(JLabel.CENTER);
		schwarzerSpielerText.setHorizontalAlignment(JLabel.CENTER);
		schwarzerSpielerText.setVisible(false);
		
		schwarzerSpielerField = new JTextField();
		schwarzerSpielerField.setText("Spieler 2");
		schwarzerSpielerField.setBounds(0, 145, 200, 25);
		schwarzerSpielerField.setVisible(true);
		//Status
		statusText = new JLabel();
		statusText.setBounds(0, 300, 200, 25);
		statusText.setText("Spieler Namen eingeben...");
		statusText.setBackground(Color.decode("#263661"));
		statusText.setForeground(Color.white);
		statusText.setBorder(new LineBorder(Color.black));
		statusText.setOpaque(true);
		statusText.setVerticalAlignment(JLabel.CENTER);
		statusText.setHorizontalAlignment(JLabel.CENTER);
		//Fen Liste
		fenText = new JLabel();
		fenText.setBounds(0,50,200,25);
		fenText.setText("FEN (leer=standard): ");
		fenText.setBackground(Color.decode("#263661"));
		fenText.setForeground(Color.white);
		fenText.setOpaque(true);
		fenText.setVisible(true);
		//FenFIeld
		fenField = new JTextField();
		fenField.setBounds(0, 75, 200, 25);
		fenField.setVisible(true);
		fenField.setText("");
		//JTable
		jt.setVisible(true);
		jt.setBackground(Color.decode("#261660"));
		jt.setForeground(Color.white);
		//jt.setBounds(0, 200, 200, 50);
		jt.setFocusable(false);
		jt.addMouseListener(new MouseAdapter()  
		{  
			public void mouseClicked( java.awt.event.MouseEvent e)  
			{  
				jt.setFocusable(false);
				button.requestFocus(true);
				button.grabFocus();
				button.transferFocus();
			}  

		});
		Object[] columns = {"Weiß", "Schwarz"};
		model.setColumnIdentifiers(columns);
		jt.setModel(model);
		jt.setFont(font);
		jt.setRowHeight(20);
		jt.setLayout(new FlowLayout());
		jt.setCellSelectionEnabled(false);
		jt.setRowSelectionAllowed(false);
		jt.setCellSelectionEnabled(false);
		jt.setColumnSelectionAllowed(false);;
		jt.setRowSelectionAllowed(false);
		loescheZuege();

		//Scroll Panel
		scrollPanel=new JScrollPane(jt);   
		scrollPanel.setBounds(0,350,200,250);
		scrollPanel.setAutoscrolls(true);
		
		this.add(button); 
		this.add(buttonRemis);
		this.add(spieler1);
		this.add(weißerSpielerText);
		this.add(weißerSpielerField);
		this.add(spieler2);
		this.add(schwarzerSpielerText);
		this.add(schwarzerSpielerField);
		this.add(statusText);
		this.add(scrollPanel);
		this.add(fenText);
		this.add(fenField);
	}
	public void addRow() {
		this.model.addRow(empty);
	}
	public void setValue(String zug, int r, int c) {
		model.setValueAt(zug, r, c);
	}
	public void loescheZuege() {
		int zaehler = model.getRowCount();
		for(int i=0;i < zaehler;i++) {
			model.removeRow(0);
		}
	}
	public String getSpielerNamen(boolean istWeiß) {
		String name ="";
		if(istWeiß) {
			name = weißerSpielerField.getText();
			weißerSpielerText.setText("Weiß: "+name);
			weißerSpielerField.setVisible(false);
			weißerSpielerText.setVisible(true);
			spieler1.setVisible(false);
		}
		else {
			name = schwarzerSpielerField.getText();
			schwarzerSpielerText.setText("Schwarz: "+name);
			schwarzerSpielerField.setVisible(false);
			schwarzerSpielerText.setVisible(true);
			spieler2.setVisible(false);
		}
		fenText.setVisible(false);
		fenField.setVisible(false);
		buttonRemis.setVisible(true);
		button.setVisible(false);
		return name;
	}
	public String getFEN(){
		String text = fenField.getText();
		fenField.setText("");
		return text;
	}
	public void setStatus(String status) {
		this.statusText.setText(status);
		if(status.contains("Schachmatt") || status.contains("Remis")) {
			this.statusText.setBackground(Color.red);
		}
		else {
			this.statusText.setBackground(Color.decode("#263661"));
		}
	}
	public void setEingabe() {
		spieler1.setVisible(true);
		weißerSpielerText.setVisible(false);
		weißerSpielerField.setVisible(true);
		spieler2.setVisible(true);
		schwarzerSpielerText.setVisible(false);
		schwarzerSpielerField.setVisible(true);
		fenText.setVisible(true);
		fenField.setVisible(true);
		button.setVisible(true);
		buttonRemis.setVisible(false);
	}
}