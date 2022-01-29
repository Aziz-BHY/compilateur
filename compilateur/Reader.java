package compilateur;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Reader {
	private BufferedReader myReader;
	private int index;
	private char courant;
	private ArrayList<Character> liste;
	private ArrayList<Lexicale> lexical;
	private boolean EOF = false;
	Reader(){
    		myReader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	Reader(String fileName){
    	try {
    		myReader = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	ArrayList<Lexicale> getcontent() {
		liste = new ArrayList<Character>();
		index = -1;
		lexical = new ArrayList<Lexicale>();
		int car;
	        try {
				while ((car = myReader.read()) > 0) {
				  liste.add((char) car);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		lexical();
		System.out.println(lexical);
		return lexical;
	}
	void suivant(){
		index++;
		if(index < liste.size()) {
			courant = liste.get(index);
		}else EOF = true;
	}
	void lexical() {
		
		while(index < liste.size() && !EOF) {
			suivant();
			skip();
			if(EOF) {
				lexical.add(new Lexicale(Categorie.EOF, "$"));
				return;
			}
			if(Character.isLetter(courant)) {
				String mot = getMot().toLowerCase();
				if(mot.compareTo("si") == 0) lexical.add(new Lexicale(Categorie.SI, "si"));
				else if(mot.compareTo("sinon") == 0) lexical.add(new Lexicale(Categorie.SINON, "sinon"));
				else if(mot.compareTo("alors") == 0) lexical.add(new Lexicale(Categorie.ALORS, "alors"));
				else if(mot.compareTo("pour") == 0) lexical.add(new Lexicale(Categorie.POUR, "pour"));
				else if(mot.compareTo("tant") == 0) lexical.add(new Lexicale(Categorie.TANT, "tant"));
				else if(mot.compareTo("que") == 0) lexical.add(new Lexicale(Categorie.QUE, "que"));
				else if(mot.compareTo("faire") == 0) lexical.add(new Lexicale(Categorie.FAIRE, "faire"));
				else if(mot.compareTo("de") == 0) lexical.add(new Lexicale(Categorie.DE, "de"));
				else if(mot.compareTo("a") == 0) lexical.add(new Lexicale(Categorie.A, "a"));
				else if(mot.compareTo("entier") == 0) lexical.add(new Lexicale(Categorie.TYPE, "entier"));
				else if(mot.compareTo("reel") == 0) lexical.add(new Lexicale(Categorie.TYPE, "reel"));
				else if(mot.compareTo("booleen") == 0) lexical.add(new Lexicale(Categorie.TYPE, "booleen"));
				else if(mot.compareTo("tableau") == 0) lexical.add(new Lexicale(Categorie.TYPE, "tableau"));
				else if(mot.compareTo("caracteres") == 0) lexical.add(new Lexicale(Categorie.TYPE, "caracteres"));
				else if(mot.compareTo("lire") == 0) lexical.add(new Lexicale(Categorie.LI, "lire"));
				else if(mot.compareTo("ecrire") == 0) lexical.add(new Lexicale(Categorie.EC, "ecrire"));
				else if(mot.compareTo("et") == 0) lexical.add(new Lexicale(Categorie.ET, "et"));
				else if(mot.compareTo("ou") == 0) lexical.add(new Lexicale(Categorie.OU, "ou"));
				else lexical.add(new Lexicale(Categorie.ID, mot));
			}
			else if(Character.isDigit(courant)) {
				lexical.add(getNum());
			}
			else if(courant == '.') lexical.add(new Lexicale(Categorie.POINT, "."));
			else if(courant == ',') lexical.add(new Lexicale(Categorie.V, ","));
			else if(courant == '\"') lexical.add(new Lexicale(Categorie.GU, "\""));
			else if(courant == ';') lexical.add(new Lexicale(Categorie.PV, ";"));
			else if(courant == '}') lexical.add(new Lexicale(Categorie.SMF, "}"));
			else if(courant == '{') lexical.add(new Lexicale(Categorie.SMO, "{"));
			else if(courant == '[') lexical.add(new Lexicale(Categorie.CO, "["));
			else if(courant == ']') lexical.add(new Lexicale(Categorie.CF, "]"));
			else if(courant == '(') lexical.add(new Lexicale(Categorie.PO, "("));
			else if(courant == ')') lexical.add(new Lexicale(Categorie.PF, ")"));
			else if(courant == ':') lexical.add(getOPAFF());
			else if(courant == '=') lexical.add(new Lexicale(Categorie.OPAFF, "="));
			else if (courant == '+' || courant == '-' || courant == '*' || courant =='/') lexical.add(new Lexicale(Categorie.CALC, courant+""));
			else if (courant == '<') lexical.add(OPRELL());
			else if (courant == '>') lexical.add(OPRELL());
			else showError();
			
		}

	}
	
	private void skip() {
		while(Character.isWhitespace(courant)) { 
			if (EOF) break;
			suivant();
		}
		
	}

	private String getMot() {
		int etat = 0;
		StringBuffer sb=new StringBuffer();
		while(true) {
			switch(etat) {
				case 0 : etat=1; 
						 sb.append(courant); 
						 break;
				case 1 : suivant();
						 if(EOF)
							 etat=3;
						 else
							 if(Character.isLetterOrDigit(courant)) 
								 sb.append(courant);
							 else
								 etat=2;
						 break;
				case 2 : reculer();
						 return sb.toString();
				case 3 : return sb.toString();
			}
		}
		
	}

	private Lexicale getNum() {
		int etat=0;
		StringBuffer sb=new StringBuffer();
		while(true) {
			switch(etat) {
			case 0 : etat=1; 
					 sb.append(courant); 
					 break;
			case 1 : suivant();
					 if(EOF)
						 etat=3;
					 else
						 if(Character.isDigit(courant)) 
							 sb.append(courant);
						 else
							 etat=2;
					 break;
			case 2 : reculer();
					 return new Lexicale(Categorie.CHFF, sb.toString());
			case 3 : return new Lexicale(Categorie.CHFF, sb.toString());
			}
		}
	}

	private void reculer() {
		if(index>0)
            index--;
		
	}

	private void showError() {
		System.out.print("erreur lexical: le caractére " + courant + " n'appartient pas ici" + index);
		System.out.println(lexical);
		System.exit(0);
		
	}

	Lexicale getOPAFF() {
		int etat = 0; 
		StringBuffer sb=new StringBuffer();
		while(true) {
			switch(etat) {
			case 0: 
				if(courant == ':') {
					sb.append(courant);
					suivant();
					etat = 1;
				}else break;
			
			case 1:
				if(courant == '=') {
					sb.append(courant);
					suivant();
					etat = 2;
				}else return new Lexicale(Categorie.PP, sb.toString());
			case 2: 
				return new Lexicale(Categorie.OPAFF, sb.toString());
				
			}
		}
	}
	
	Lexicale OPRELL() {
		int etat = 0; 
		StringBuffer sb=new StringBuffer();
		while(true) {
			switch(etat) {
			case 0: 
				sb.append(courant);
				suivant();
				if(courant == '<') {
					etat = 1;
				}
				else if (courant == '>') {
					etat = 2;
				}
			case 1:
				if(EOF) return new Lexicale(Categorie.OPREL, sb.toString());
				if(courant == '>') {
					sb.append(courant);
					return new Lexicale(Categorie.OPREL, sb.toString()); 
				}
				else if(courant == '=') {
					sb.append(courant);
					return new Lexicale(Categorie.OPREL, sb.toString());
				}
				else {
					reculer();
					return new Lexicale(Categorie.OPREL, sb.toString());
				}
				
			case 2:
				if(courant == '=') {
					sb.append(courant);
					return new Lexicale(Categorie.OPREL, sb.toString());
				}
				else {
					reculer();
					return new Lexicale(Categorie.OPREL, sb.toString());
				}
			}
		}
	}
}
