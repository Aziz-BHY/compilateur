package compilateur;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Analyse {
	private ArrayList<Lexicale> prog;
	private Stack<String> analyse;
	private String action;
	private int car;
	private HashMap<String, String> tableIDs;
	private Stack<E> type;
	private Stack<Code> code;
	private int tempIndex = 0;
	private int labIndex = 1;
	public String[] LRGS = {
			"P->D ; I", //0
	   		"D->D ; D", //1
	   		"D->T : id ", //2
	   		"R->R , id", //3
	   		"T->entier",//4
	   		"T->reel", //5
	   		"T->booleen", //6
	   		"T->tableau [ chff ] de T", //7
	   		"T->caracteres", //8
	   		"I->I ; I", //9
	   		"I->id := E", //10
	   		"I->si E alors { I } S", //11
	   		"I->tant que E faire { I }", //12
	   		"I->pour N de N à N faire { I }", //13
	   		"I->lire ( id )", //14
	   		"I->ecrire ( E )", //15
	   		"N->id", //16
	   		"N->chff", //17
	   		"S->sinon { I }", //18
	   		"E->\" id \"", //19
	   		"E->nb", //20
	   		"E->chff", //21
	   		"E->id", //22
	   		"E->E = E", //23
	   		"E->E <> E", //24
	   		"E->E + E", //25
	   		"E->E - E", //26
	   		"E->E * E", //27
	   		"E->E / E", //28
	   		"E->E < E", //29
	   		"E->E > E", //30
	   		"E->E <= E", //31
	   		"E->E >= E", //32
	   		"E->E et E", //33
	   		"E->E ou E", //34
	   		"NB->chiffre . chiffre", //35
	   		"I->si E alors { I }", //36
	   		"E->id [ N ]" //37
	   		
	   		};
	
	public String[][] tableSLR = {
            {"etat/VT", ";", 	":", 	"id", 	",", 	"entier", 	"reel", 	"booleen", 	"tableau", 	"[", 	"]", 	"chff", "de", 	"caracteres", 	":=", 	"si", 	"alors", "{", 	"}", 	"sinon", 	"tant", "que", 	"faire","pour",	"a", 	"lire", "ecrire", 	"(", 	")", 	"\"", 	"=", 	"<>", 	"+", 	"-", 	"*", 	"/", 	"<", 	">", 	"<=", 	">=", 	"et", 	"ou", 	".", 	"$", 	"P", 	"D", 	"I" ,	"T", 	"R", 	"N", 	"S", 	"E", 	"NB" },
            {"0", 		"err", 	"err", 	"err", 	"err", 	"s4", 		"s5", 		"s6", 		"s8", 		"err", 	"err", 	"err", 	"err", 	"s7", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"1", 	"2", 	"err" ,	"3", 	"err",  "err", 	"err", 	"err", 	"err" },
            {"1", 		"err", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"ACC", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"2", 		"s9", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err",	"err", 	"err", 	"err", 	"err" },
            {"3", 		"err", 	"s10", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"4", 		"err",	"r4", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"5", 		"err", 	"r5", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"6", 		"err", 	"r6", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"7", 		"err", 	"r8", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err",	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"8", 		"err", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"s11", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"9", 		"err", 	"err", 	"s14", 	"err", 	"s4", 		"s5", 		"s6", 		"s8", 		"err", 	"err", 	"err", 	"err", 	"s7", 			"err", 	"s15", 	"err", 	"err", 	"err", 	"err", 		"s16", 	"err", 	"err", 	"s17", 	"err", 	"s18", 	"s19", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"13", 	"12" ,	"3", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"10", 		"err", 	"err", 	"s20", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"11", 		"err", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"s21", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"12",  	"s22", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"r0", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"13", 	 	"r1", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"14",  	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"s24", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"15",  	"err", 	"err", 	"s29", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"s28", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"s26", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"25", 	"27"  },
            {"16",  	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"s30", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"17", 		"err", 	"err", 	"s32", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"s33", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"31", 	"err", 	"err", 	"err" },
            {"18", 		"err", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"s34", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"19", 		"err", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"s35", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"20", 		"r2", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"36", 	"err", 	"err", 	"err", 	"err" },
            {"21", 		"err", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"s37", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"22", 		"err", 	"err", 	"s14", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"s15", 	"err", 	"err", 	"err", 	"err", 		"s16", 	"err", 	"err", 	"s17", 	"err", 	"s18", 	"s19", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"38" ,	"err", 	"err",	"err", 	"err", 	"err", 	"err" },
            {"23", 		"err", 	"err", 	"err", 	"err", 	"s4", 		"s5", 		"s6", 		"s8", 		"err", 	"err", 	"err", 	"err", 	"s7", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"13", 	"err" ,	"3", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"24", 		"err", 	"err", 	"s29", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"s28", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"s26", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"39", 	"27"  },
            {"25", 		"err", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"s40", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"s41", 	"s42", 	"s43", 	"s44", 	"s45", 	"s46", 	"s47", 	"s48", 	"s49", 	"s50", 	"s51", 	"s52", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"26", 		"err", 	"err", 	"s53", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"27", 		"r20", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"r20", 	"err", 	"r20", 	"err", 		"err", 	"err", 	"r20", 	"err", 	"err", 	"err", 	"err", 		"err", 	"r20", 	"err", 	"r20", 	"r20", 	"r20", 	"r20", 	"r20", 	"r20", 	"r20", 	"r20", 	"r20", 	"r20", 	"r20", 	"r20", 	"err", 	"r20", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"28", 		"r21", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"r21", 	"err", 	"r21", 	"err", 		"err", 	"err", 	"r21", 	"err", 	"err", 	"err", 	"err", 		"err", 	"r21", 	"err", 	"r21", 	"r21", 	"r21", 	"r21", 	"r21", 	"r21", 	"r21", 	"r21", 	"r21", 	"r21", 	"r21", 	"r21", 	"s54", 	"r21", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"29", 		"r22", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"s98", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"r22", 	"err", 	"r22", 	"err", 		"err", 	"err", 	"r22", 	"err", 	"err", 	"err", 	"err", 		"err", 	"r22", 	"err", 	"r22", 	"r22", 	"r22", 	"r22", 	"r22", 	"r22", 	"r22", 	"r22", 	"r22", 	"r22", 	"r22", 	"r22", 	"err", 	"r22", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"30", 		"err", 	"err", 	"s29", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"s28", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"s26", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"55", 	"27"  },
            {"31", 		"err", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"s56", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"32", 		"err", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"r16", 	"err", 	"r16", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"r16", 	"err", 	"r16", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"33", 		"err", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"r16", 	"err", 	"r17", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"r17", 	"err", 	"r17", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"34", 		"err", 	"err", 	"s57", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"35", 		"err", 	"err", 	"s29", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"s28", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"s26", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"58", 	"27"  },
            {"36", 		"r2", 	"err", 	"err", 	"s59", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"37", 		"err", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"s60", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"38", 	 	"r9", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"r9", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"r9", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"39", 		"r10", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"r10", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"s41", 	"s42", 	"s43", 	"s44", 	"s45", 	"s46", 	"s47", 	"s48", 	"s49", 	"s50", 	"s51", 	"s52", 	"err", 	"r10", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"40", 		"err", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"s61", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"41", 		"err", 	"err", 	"s29", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"s28", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"s26", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"62", 	"27"  },
            {"42", 		"err", 	"err", 	"s29", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"s28", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"s26", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"63", 	"27"  },
            {"43", 		"err", 	"err", 	"s29", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"s28", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"s26", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"64", 	"27"  },
            {"44", 		"err", 	"err", 	"s29", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"s28", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"s26", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"65", 	"27"  },
            {"45", 		"err", 	"err", 	"s29", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"s28", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"s26", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"66", 	"27"  },
            {"46", 		"err", 	"err", 	"s29", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"s28", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"s26", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"67", 	"27"  },
            {"47", 		"err", 	"err", 	"s29", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"s28", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"s26", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"68", 	"27"  },
            {"48", 		"err", 	"err", 	"s29", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"s28", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"s26", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"69", 	"27"  },
            {"49", 		"err", 	"err", 	"s29", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"s28", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"s26", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"70", 	"27"  },
            {"50", 		"err", 	"err", 	"s29", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"s28", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"s26", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"71", 	"27"  },
            {"51", 		"err", 	"err", 	"s29", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"s28", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"s26", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"72", 	"27"  },            
            {"52", 		"err", 	"err", 	"s29", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"s28", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"s26", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"73", 	"27"  },
            {"53", 		"err", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"s74", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"54", 		"err", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"s75", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"55", 		"err", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"s76", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"s41", 	"s42", 	"s43", 	"s44", 	"s45", 	"s46", 	"s47", 	"s48", 	"s49", 	"s50", 	"s51", 	"s52", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"56", 		"err", 	"err", 	"s32", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"s33", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"77", 	"err", 	"err", 	"err" },
            {"57", 		"err", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"s78", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"58", 		"err", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"s79", 	"err", 	"s41", 	"s42", 	"s43", 	"s44", 	"s45", 	"s46", 	"s47", 	"s48", 	"s49", 	"s50", 	"s51", 	"s52", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"59", 		"err", 	"err", 	"s80", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"60", 		"err", 	"err", 	"err", 	"err", 	"s4", 		"s5", 		"s6", 		"s8", 		"err", 	"err", 	"err", 	"err", 	"s7", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"81", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"61", 		"err", 	"err", 	"s14", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"s15", 	"err", 	"err", 	"err", 	"err", 		"s16", 	"err", 	"err", 	"s17", 	"err", 	"s18", 	"s19", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"82" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"62", 		"r23", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"r23", 	"err", 	"r23", 	"err", 		"err", 	"err", 	"r23", 	"err", 	"err", 	"err", 	"err", 		"err", 	"r23", 	"err", 	"r23", 	"r23", 	"r23", 	"r23", 	"r23", 	"r23", 	"r23", 	"r23", 	"r23", 	"r23", 	"r23", 	"r23", 	"err", 	"r23", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"63", 		"r24", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"r24", 	"err", 	"r24", 	"err", 		"err", 	"err", 	"r24", 	"err", 	"err", 	"err", 	"err", 		"err", 	"r24", 	"err", 	"r24", 	"r24", 	"r24", 	"r24", 	"r24", 	"r24", 	"r24", 	"r24", 	"r24", 	"r24", 	"r24", 	"r24", 	"err", 	"r24", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"64", 		"r25", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"r25", 	"err", 	"r25", 	"err", 		"err", 	"err", 	"r25", 	"err", 	"err", 	"err", 	"err", 		"err", 	"r25", 	"err", 	"r25", 	"r25", 	"r25", 	"r25", 	"r25", 	"r25", 	"r25", 	"r25", 	"r25", 	"r25", 	"r25", 	"r25", 	"err", 	"r25", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"r25", 	"err", 	"err", 	"err" },
            {"65", 		"r26", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"r26", 	"err", 	"r26", 	"err", 		"err", 	"err", 	"r26", 	"err", 	"err", 	"err", 	"err", 		"err", 	"r26", 	"err", 	"r26", 	"r26", 	"r26", 	"r26", 	"r26", 	"r26", 	"r26", 	"r26", 	"r26", 	"r26", 	"r26", 	"r26", 	"err", 	"r26", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"r26", 	"err", 	"err", 	"err" },
            {"66", 		"r27", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"r27", 	"err", 	"r27", 	"err", 		"err", 	"err", 	"r27", 	"err", 	"err", 	"err", 	"err", 		"err", 	"r27", 	"err", 	"r27", 	"r27", 	"r27", 	"r27", 	"r27", 	"r27", 	"r27", 	"r27", 	"r27", 	"r27", 	"r27", 	"r27", 	"err", 	"r27", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"r27", 	"err", 	"err", 	"err" },
            {"67", 		"r28", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"r28", 	"err", 	"r28", 	"err", 		"err", 	"err", 	"r28", 	"err", 	"err", 	"err", 	"err", 		"err", 	"r28", 	"err", 	"r28", 	"r28", 	"r28", 	"r28", 	"r28", 	"r28", 	"r28", 	"r28", 	"r28", 	"r28", 	"r28", 	"r28", 	"err", 	"r28", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"r28", 	"err", 	"err", 	"err" },
            {"68", 		"r29", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"r29", 	"err", 	"r29", 	"err", 		"err", 	"err", 	"r29", 	"err", 	"err", 	"err", 	"err", 		"err", 	"r29", 	"err", 	"r29", 	"r29", 	"r29", 	"r29", 	"r29", 	"r29", 	"r29", 	"r29", 	"r29", 	"r29", 	"r29", 	"r29", 	"err", 	"r29", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"r29", 	"err", 	"err", 	"err" },
            {"69", 		"r30", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"r30", 	"err", 	"r30", 	"err", 		"err", 	"err", 	"r30", 	"err", 	"err", 	"err", 	"err", 		"err", 	"r30", 	"err", 	"r30", 	"r30", 	"r30", 	"r30", 	"r30", 	"r30", 	"r30", 	"r30", 	"r30", 	"r30", 	"r30", 	"r30", 	"err", 	"r30", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"r30", 	"err", 	"err", 	"err" },
            {"70", 		"r31", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"r31", 	"err", 	"r31", 	"err", 		"err", 	"err", 	"r31", 	"err", 	"err", 	"err", 	"err", 		"err", 	"r31", 	"err", 	"r31", 	"r31", 	"r31", 	"r31", 	"r31", 	"r31", 	"r31", 	"r31", 	"r31", 	"r31", 	"r31", 	"r31", 	"err", 	"r31", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"r31", 	"err", 	"err", 	"err" },
            {"71", 		"r32", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"r32", 	"err", 	"r32", 	"err", 		"err", 	"err", 	"r32", 	"err", 	"err", 	"err", 	"err", 		"err", 	"r32", 	"err", 	"r32", 	"r32", 	"r32", 	"r32", 	"r32", 	"r32", 	"r32", 	"r32", 	"r32", 	"r32", 	"r32", 	"r32", 	"err", 	"r32", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"r32", 	"err", 	"err", 	"err" },
            {"72", 		"r33", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"r33", 	"err", 	"r33", 	"err", 		"err", 	"err", 	"r33", 	"err", 	"err", 	"err", 	"err", 		"err", 	"r33", 	"err", 	"r33", 	"r33", 	"r33", 	"r33", 	"r33", 	"r33", 	"r33", 	"r33", 	"r33", 	"r33", 	"r33", 	"r33", 	"err", 	"r33", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"r33", 	"err", 	"err", 	"err" },
            {"73", 		"r34", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"r34", 	"err", 	"r34", 	"err", 		"err", 	"err", 	"r34", 	"err", 	"err", 	"err", 	"err", 		"err", 	"r34", 	"err", 	"r34", 	"r34", 	"r34", 	"r34", 	"r34", 	"r34", 	"r34", 	"r34", 	"r34", 	"r34", 	"r34", 	"r34", 	"err", 	"r34", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"r34", 	"err", 	"err", 	"err" },
            {"74", 		"r19", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"r19", 	"err", 	"r19", 	"err", 		"err", 	"err", 	"r19", 	"err", 	"err", 	"err", 	"err", 		"err", 	"r19", 	"err", 	"r19", 	"r19", 	"r19", 	"r19", 	"r19", 	"r19", 	"r19", 	"r19", 	"r19", 	"r19", 	"r19", 	"r19", 	"err", 	"r19", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"75", 		"r35", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"r35", 	"err", 	"r35", 	"err", 		"err", 	"err", 	"r35", 	"err", 	"err", 	"err", 	"err", 		"err", 	"r35", 	"err", 	"r35", 	"r35", 	"r35", 	"r35", 	"r35", 	"r35", 	"r35", 	"r35", 	"r35", 	"r35", 	"r35", 	"r35", 	"err", 	"r35", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"76", 		"err", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"s83", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"77", 		"err", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"s84", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"78", 		"r14", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"r14", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"r14", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"79", 		"r15", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"r15", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"r15", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"80", 		"r3", 	"err", 	"err", 	"r3", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"81", 		"err", 	"r7", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"82", 		"s22", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"s85", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"83", 		"err", 	"err", 	"s14", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"s15", 	"err", 	"err", 	"err", 	"err", 		"s16", 	"err", 	"err", 	"s17", 	"err", 	"s18", 	"s19", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"86" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"84", 		"err", 	"err", 	"s32", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"s33", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"87", 	"err", 	"err", 	"err" },
            {"85", 		"r36", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"r36", 	"s89", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"r36", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"88", 	"err", 	"err" },
            {"86", 		"s22", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"s90", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"87", 		"err", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"s91", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"88", 		"r11", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"r11", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"r11", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"89", 		"err", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"s92", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"90", 		"r12", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"r12", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"r12", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"91", 		"err", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"s93", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"92", 		"err", 	"err", 	"s14", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"s15", 	"err", 	"err", 	"err", 	"err", 		"s16", 	"err", 	"err", 	"s17", 	"err", 	"s18", 	"s19", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"94" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"93", 		"err", 	"err", 	"s14", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"s15", 	"err", 	"err", 	"err", 	"err", 		"s16", 	"err", 	"err", 	"s17", 	"err", 	"s18", 	"s19", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"95" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"94", 		"s22", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"s96", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"95", 		"s22", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"s97", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"96", 		"r18", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"r18", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"r18", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"97", 		"r13", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"r13", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"r13", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
            {"98", 		"err", 	"err", 	"s32", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"s33", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"99", 	"err", 	"err", 	"err" },
            {"99", 		"err", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"s100", "err", 	"err", 	"err", 			"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 		"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
           {"100", 		"r37", 	"err", 	"err", 	"err", 	"err", 		"err", 		"err", 		"err", 		"err", 	"err", 	"err", 	"err", 	"err", 			"err", 	"err", 	"r37", 	"err", 	"r37", 	"err", 		"err", 	"err", 	"r37", 	"err", 	"err", 	"err", 	"err", 		"err", 	"r37", 	"err", 	"r37", 	"r37", 	"r37", 	"r37", 	"r37", 	"r37", 	"r37", 	"r37", 	"r37", 	"r37", 	"r37", 	"r37", 	"err", 	"r37", 	"err", 	"err", 	"err" ,	"err", 	"err", 	"err", 	"err", 	"err", 	"err" },
           };

	Analyse(ArrayList<Lexicale> prog){
		this.prog = prog;
		
		this.analyse = new Stack<>();
		this.type = new Stack<>();
		this.code = new Stack<>();
		this.tableIDs = new HashMap<>();
	}
	
	
	void init(){
	 //Initialization 
		action = "";
		analyse.push("0");
		
		for(car = 0; car< prog.size(); car++ ) {			
				analyser(prog.get(car));			
		}
	}
	
	void analyser(Lexicale next) {
		String search ; 
		if(next.getCategorie() == Categorie.ID) {search = "id"; type.add(new E(next.getLexeme(), ""));}
		else if(next.getCategorie() == Categorie.CHFF) {search = "chff"; type.add(new E(next.getLexeme(), "entier"));}
		else search = next.getLexeme();
		//System.out.println(search);
		action = getAction(search);
		//afficherSLR();

		if(action.charAt(0) == 's') {
			casS(search);
		}
		else if(action.charAt(0) == 'r') {
			casR(search);
		}else if(action == "err") {
			System.out.println("erreur syntaxique");
			System.out.println(search);
			System.exit(0);
		}
		else if(action == "ACC") {
			System.out.println("ACC");
			System.out.print(code);
			try {
				File file = new File("C:\\Users\\Aziz\\OneDrive\\Bureau\\result.c");
				file.createNewFile();
				FileWriter writer = new FileWriter(file);
				writer.write(code.pop().code);
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
	
	String getAction(String next){
		
		int index = Integer.parseInt(analyse.peek());
		for(int i = 1; i < tableSLR[0].length; i++) {
			if(tableSLR[0][i].compareTo(next) == 0) {
				return(tableSLR[index + 1][i]);
			}
		}
		return("err");
	}
	
	
	void casS(String next) {
		analyse.push(next);
		analyse.push(action.substring(1));
		action = "";
	}
	
	void casR(String next) {
		String s = LRGS[Integer.parseInt(action.substring(1)) ];
		genCode();
		analyseSementique();
		String parties[] = s.split("->");
		String partiesDroite[] = parties[1].split(" ");
		for(int i = 0; i<partiesDroite.length*2; i++) {
			analyse.pop();
		}
		action = getAction(parties[0]);
		analyse.push(parties[0]);
		analyse.push(action);
		car--;
		/*afficherSLR();*/
	}
	
	void afficherSLR(){
		System.out.println(analyse + "---" + prog + "---" + action);
	}
	
	void analyseSementique() {
		int regle = Integer.parseInt(action.substring(1));
		E temp1;
		E temp2;
		E temp3;
		String id;
		switch(regle) {
		case 0:
			break;
		case 1:
			
			break;
		case 2:
			temp1 = type.pop();
			temp2 = type.pop();
			if (tableIDs.get(temp1.val) != null) {
				Error("la variable " + temp1.val + " est deja associée à un type");
			}
				tableIDs.put(temp1.val, temp2.type);
				break;
		case 3:
			break;
		case 4:
			type.push(new E("", "entier")); break;
		case 5:
			type.push(new E("", "reel")); break;
		case 6:
			type.push(new E("", "booleen")); break;
		case 7:
			temp1 = type.pop();
			temp2 = type.pop();
			type.push(new E("","tableau [" + temp2.val +"] de " + temp1.type));
			break;
		case 8:
			type.push(new E("", "caracteres")); break;
		case 9:
			break;
		case 10:
			temp1 = type.pop();
			temp2 = type.pop();
			id = tableIDs.get(temp2.val);
			if(temp1.type.compareTo(id) != 0) {
				if(!(id == "entier" && temp1.type == "booleen")) {
					Error("type mismatch");
				}else {
					
				}
			}
			break;
		case 11:
			temp1 = type.pop();
			if(temp1.type != "booleen") Error("we need booleen here");
			break;
		case 12:
			temp1 = type.pop();
			if(temp1.type != "booleen") Error("we need booleen here");
			break;
		case 13:
			temp1 = type.pop();
			temp2 = type.pop();
			temp3 = type.pop();
			if (temp1.type != "entier" || temp2.type != "entier") Error("we need entier here");
			id = tableIDs.get(temp3.val);
			if(id == null) Error("variable not declared");
			break;
		case 14:
			temp1 = type.pop();
			id = tableIDs.get(temp1.val);
			if(id == null) Error("variable not declared");
			break;
		case 15:
			type.pop();
			break;
		case 16:
			temp1 = type.pop();
			if(temp1.type != "entier") {
				id = tableIDs.get(temp1.val);
				if(id == null) Error("variable not declared");
				if(id != "entier") Error("il faut un entier içi");
			}
			
			type.add(new E(temp1.val, "entier"));
			break;
		case 17:
			break;
		case 18:
			break;
		case 19:
			temp1 = type.pop();
			type.add(new E(temp1.val,"caracteres"));
			break;
		case 20:
			
			break;
		case 21:
			temp1 = type.pop();
			if(temp1.val.compareTo("0") == 0 || temp1.val.compareTo("1") == 0) {
				type.add(new E(temp1.val,"booleen"));
			}else type.add(new E(temp1.val,"entier"));
			break;
		case 22:
			temp1 = type.pop();
			id = tableIDs.get(temp1.val);
			if(id == null) Error("variable not declared");
			type.add(new E("", id));
			break;
		case 23:
			temp1 = type.pop();
			temp2 = type.pop();
			if(!(temp1.type == "entier" || temp1.type == "reel") || !(temp2.type == "entier" || temp2.type == "reel")) {
				Error("type mismatch");
			}
			
				 type.add(new E("", "booleen"));
				
			break;
		case 24:
			temp1 = type.pop();
			temp2 = type.pop();
			if(!(temp1.type == "entier" || temp1.type == "reel") || !(temp2.type == "entier" || temp2.type == "reel")) {
				Error("type mismatch");
			}
			
				 type.add(new E("", "booleen"));
		
			break;
		case 25:
			temp1 = type.pop();
			temp2 = type.pop();
			if((temp1.type == "reel" || temp1.type == "entier" || temp1.type == "booleen") && (temp2.type == "reel" || temp2.type == "entier" || temp2.type == "booleen"))
			if(temp1.type == temp2.type) {
				type.add(new E("" , temp1.type));
			}else if(temp1.type=="reel" || temp2.type == "reel") {type.add(new E("" , "reel")); }
		     else if(temp1.type=="booleen" || temp2.type == "booleen") {type.add(new E("" , "booleen"));}

			break;
		case 26:
			temp1 = type.pop();
			temp2 = type.pop();
			if((temp1.type == "reel" || temp1.type == "entier" || temp1.type == "booleen") && (temp2.type == "reel" || temp2.type == "entier" || temp2.type == "booleen"))
			if(temp1.type == temp2.type) {
				type.add(new E("" , temp1.type));
			}else if(temp1.type=="reel" || temp2.type == "reel") {type.add(new E("" , "reel")); }
		     else if(temp1.type=="booleen" || temp2.type == "booleen") {type.add(new E("" , "booleen"));}

			break;
		case 27:
			temp1 = type.pop();
			temp2 = type.pop();
			if((temp1.type == "reel" || temp1.type == "entier" || temp1.type == "booleen") && (temp2.type == "reel" || temp2.type == "entier" || temp2.type == "booleen"))
			if(temp1.type == temp2.type) {
				type.add(new E("" , temp1.type));
			}else if(temp1.type=="reel" || temp2.type == "reel") {type.add(new E("" , "reel")); }
		     else if(temp1.type=="booleen" || temp2.type == "booleen") {type.add(new E("" , "booleen"));}

			break;
		case 28:
			temp1 = type.pop();
			temp2 = type.pop();
			if((temp1.type == "reel" || temp1.type == "entier" || temp1.type == "booleen") && (temp2.type == "reel" || temp2.type == "entier" || temp2.type == "booleen"))
			if(temp1.type == temp2.type) {
				type.add(new E("" , temp1.type));
			}else if(temp1.type=="reel" || temp2.type == "reel") {type.add(new E("" , "reel")); }
		     else if(temp1.type=="booleen" || temp2.type == "booleen") {type.add(new E("" , "booleen"));}

			break;
		case 29:
			temp1 = type.pop();
			temp2 = type.pop();
			if(!(temp1.type == "entier" || temp1.type == "reel") || !(temp2.type == "entier" || temp2.type == "reel")) {
				Error("type mismatch");
			}
			
				 type.add(new E("", "booleen"));
				
			break;
		case 30:
			temp1 = type.pop();
			temp2 = type.pop();
			if(!(temp1.type == "entier" || temp1.type == "reel") || !(temp2.type == "entier" || temp2.type == "reel")) {
				Error("type mismatch");
			}
			
				 type.add(new E("", "booleen"));
				
			break;
		case 31:
			temp1 = type.pop();
			temp2 = type.pop();
			if(!(temp1.type == "entier" || temp1.type == "reel") || !(temp2.type == "entier" || temp2.type == "reel")) {
				Error("type mismatch");
			}
			
				 type.add(new E("", "booleen"));
				
			break;
		case 32:
			temp1 = type.pop();
			temp2 = type.pop();
			if(!(temp1.type == "entier" || temp1.type == "reel") || !(temp2.type == "entier" || temp2.type == "reel")) {
				Error("type mismatch");
			}
			
				 type.add(new E("", "booleen"));
				
			break;
		case 33:
			temp1 = type.pop();
			temp2 = type.pop();
			if(temp1.type != "booleen" || temp2.type != "booleen") {
				Error("type mismatch");
			}
			
				 type.add(new E("", "booleen"));
				
			break;
		case 34:
			temp1 = type.pop();
			temp2 = type.pop();
			if(temp1.type != "booleen" || temp2.type != "booleen") {
				Error("type mismatch");
			}
			
				 type.add(new E("", "booleen"));
				
			break;
		case 35:
			temp1 = type.pop();
			temp2 = type.pop();
			type.add(new E(temp1+"."+temp2 , "reel"));
			break;
		case 36:
			temp1 = type.pop();
			if(temp1.type != "booleen") {
				Error("we need booleen here");
			}
			break;
		
		case 37:
			temp1 = type.pop();
			temp2 = type.pop();
			if(temp1.type != "entier") Error("need entier here");
			id = tableIDs.get(temp2.val);
			if(id == null) Error("variable not declared");
			if(id.startsWith("tableau")) {
				int x = id.indexOf("de");
				type.add(new E("", id.substring(x+3)));
			}
			else Error("ceci n'est pas un tableau");
			break;
		}
			
	}
	
	String Temp() {
		tempIndex++;
		return "temp" + tempIndex;
	}
	void genCode() {
		int regle = Integer.parseInt(action.substring(1));
		Code temp1;
		Code temp2;
		Code temp3;
		Code temp4;
		E Etemp;
		E Etemp2;
		String newTemp;

		switch(regle) {
		case 0:
			temp1 = code.pop();
			temp2 = code.pop();
			code.add(new Code(temp2.code  +"; \n"+ temp1.code, "" ));
			break;
		case 1:
			temp1 = code.pop();
			temp2 = code.pop();
			code.add(new Code(temp2.code  +"; \n"+ temp1.code, "" ));
			break;
		case 2:
			//System.out.println(type);
			temp1 = code.pop();
			code.add(new Code(temp1.code+ " " + type.peek().val, "" ));
			break;
		case 3:
			break;
		case 4:
			code.add(new Code("int", ""));
			 break;
		case 5:
			code.add(new Code("float", ""));
			break;
		case 6:
			code.add(new Code("int", ""));
			break;
		case 7:
			Etemp = type.pop();
			Etemp2 = type.pop();
			code.add(new Code(code.pop().code+"["+Etemp2.val+"]", ""));
			
			type.add(Etemp2);
			type.add(Etemp);
			break;
		case 8:
			code.add(new Code("char*", ""));
			 break;
		case 9:
			temp1 = code.pop();
			temp2 = code.pop();
			code.add(new Code(temp2.code  + temp1.code, "" ));
			break;
		case 10:
			temp1 = code.pop();
			Etemp = type.pop();
			code.add(new Code(temp1.code + " "+ type.peek().val + "=" + temp1.place + ";\n", ""));
			type.add(Etemp);
			break;
		case 11:
			temp1 = code.pop();
			temp2 = code.pop();
			temp3 = code.pop();
			code.add(new Code(temp3.code + "\n"
					+ "if( "+temp3.place+" == 1 ) goto LVRAI"+labIndex+" \n"
					+ temp1.code
					+ "\n goto LFIN"+labIndex+" \n"
					+ "LVRAI"+labIndex+": " + temp2.code
					+ "\n LFIN"+labIndex+":"
					,""));
			labIndex++;
			break;
		case 12:
			temp1 = code.pop();
			temp2 = code.pop();
			code.add(new Code(temp2.code
					+"LBOUCLE"+labIndex+": if("+temp2.place+" == 0) goto LFIN"+labIndex+" \n"
					+temp1.code
					+"\n goto LBOUCLE"+labIndex+" \n"+
					"LFIN"+labIndex+": ",
					""));
			labIndex++;
			break;
		case 13:
			temp1 = code.pop();
			temp2 = code.pop();
			temp3 = code.pop();
			temp4 = code.pop();
			code.add(new Code(
					temp4.place + " = " + temp3.place+ "\n"
					+"LBOUCLE"+labIndex+": if("+temp4.place+" >= " + temp2.place+") goto LFIN"+labIndex+" \n"
					+ temp4.place + "++; \n"
					+ temp1.code + "\n"
					+"goto LBOUCLE"+labIndex+" \n"
					+"LFIN"+labIndex+":"
					, ""));
			labIndex++;
			break;
		case 14:
			code.add(new Code("gets("+type.peek().val+")", ""));
			break;
		case 15:
			temp1 = code.pop();
			code.add(new Code(temp1.code+" gets("+temp1.place+");", ""));
			break;
		case 16:
			code.add(new Code("", type.peek().val));
			break;
		case 17:
			code.add(new Code("", type.peek().val));
			break;
		case 18:
			break;
		case 19:
			code.add(new Code("", "\""+type.peek().val+"\""));
			break;
		case 20:
			temp1 = code.pop();
			code.add(new Code("", temp1.place));
			break;
		case 21:
			code.add(new Code("", type.peek().val));
			break;
		case 22:
			code.add(new Code("", type.peek().val));
			break;
		case 23:
			temp1 = code.pop();
			temp2 = code.pop();
			newTemp = Temp();
			code.add(new Code(temp2.code + " " + temp1.code +
					"if( "+ temp2.place + " = " + temp1.place+ ") goto LVRAI"+labIndex+"\n"+
					newTemp + "= 0 \n"+
					"goto LFIN"+labIndex+" \n"+
					"LVRAI"+labIndex+": "+newTemp + "= 1 \n LFIN"+labIndex+": \n"
					
					, newTemp));
			labIndex++;
			break;
		case 24:
			temp1 = code.pop();
			temp2 = code.pop();
			newTemp = Temp();
			code.add(new Code(temp2.code + " " + temp1.code +
					"if( "+ temp2.place + " != " + temp1.place+ ") goto LVRAI"+labIndex+"\n"+
					newTemp + "= 0 \n"+
					"goto LFIN"+labIndex+" \n"+
					"LVRAI"+labIndex+": "+newTemp + "= 1 \n LFIN"+labIndex+": \n"
					
					, newTemp));
			labIndex++;
			break;
		case 25:
			temp1 = code.pop();
			temp2 = code.pop();
			newTemp = Temp();
			code.add(new Code(temp2.code + " " + temp1.code + " "+ newTemp + "=" + temp2.place + "+" + temp1.place+ ";\n", newTemp));
			break;
		case 26:
			temp1 = code.pop();
			temp2 = code.pop();
			newTemp = Temp();
			code.add(new Code(temp2.code + " " + temp1.code + " "+ newTemp + "=" + temp2.place + "-" + temp1.place+ ";\n", newTemp));
			break;
		case 27:
			temp1 = code.pop();
			temp2 = code.pop();
			newTemp = Temp();
			code.add(new Code(temp2.code + " " + temp1.code + " "+ newTemp + "=" + temp2.place + "*" + temp1.place+ ";\n", newTemp));
			break;
		case 28:
			temp1 = code.pop();
			temp2 = code.pop();
			newTemp = Temp();
			code.add(new Code(temp2.code + " " + temp1.code + " "+ newTemp + "=" + temp2.place + "/" + temp1.place+ ";\n", newTemp));
			break;
	//  must change
		case 29:
			temp1 = code.pop();
			temp2 = code.pop();
			newTemp = Temp();
			code.add(new Code(temp2.code + " " + temp1.code +
					"if( "+ temp2.place + " < " + temp1.place+ ") goto LVRAI"+labIndex+"\n"+
					newTemp + "= 0 \n"+
					"goto LFIN"+labIndex+" \n"+
					"LVRAI"+labIndex+": "+newTemp + "= 1 \n LFIN"+labIndex+": \n"
					
					, newTemp));
			labIndex++;
			break;
		case 30:
			temp1 = code.pop();
			temp2 = code.pop();
			newTemp = Temp();
			code.add(new Code(temp2.code + " " + temp1.code +
					"if( "+ temp2.place + " > " + temp1.place+ ") goto LVRAI"+labIndex+"\n"+
					newTemp + "= 0 \n"+
					"goto LFIN"+labIndex+" \n"+
					"LVRAI"+labIndex+": "+newTemp + "= 1 \n LFIN"+labIndex+": \n"
					
					, newTemp));
			labIndex++;
			break;
		case 31:
			temp1 = code.pop();
			temp2 = code.pop();
			newTemp = Temp();
			code.add(new Code(temp2.code + " " + temp1.code +
					"if( "+ temp2.place + " <= " + temp1.place+ ") goto LVRAI"+labIndex+"\n"+
					newTemp + "= 0 \n"+
					"goto LFIN"+labIndex+" \n"+
					"LVRAI"+labIndex+": "+newTemp + "= 1 \n LFIN"+labIndex+": \n"
					
					, newTemp));
			labIndex++;
			break;
		case 32:
			temp1 = code.pop();
			temp2 = code.pop();
			newTemp = Temp();
			code.add(new Code(temp2.code + " " + temp1.code +
					"if( "+ temp2.place + " >= " + temp1.place+ ") goto LVRAI"+labIndex+" \n"+
					newTemp + "= 0 \n"+
					"goto LFIN"+labIndex+" \n"+
					"LVRAI"+labIndex+": "+newTemp + "= 1 \n LFIN"+labIndex+": \n"
					
					, newTemp));
			labIndex++;
			break;
		case 33:
			temp1 = code.pop();
			temp2 = code.pop();
			newTemp = Temp();
			code.add(new Code(temp2.code + " " + temp1.code +
					"if( "+ temp2.place + " && " + temp1.place+ ") goto LVRAI"+labIndex+"\n"+
					newTemp + "= 0 \n"+
					"goto LFIN"+labIndex+" \n"+
					"LVRAI"+labIndex+": "+newTemp + "= 1 \n LFIN"+labIndex+": \n"
					
					, newTemp));
			labIndex++;
			break;
		case 34:
			temp1 = code.pop();
			temp2 = code.pop();
			newTemp = Temp();
			code.add(new Code(temp2.code + " " + temp1.code +
					"if( "+ temp2.place + " || " + temp1.place+ ") goto LVRAI"+labIndex+"\n"+
					newTemp + "= 0 \n"+
					"goto LFIN"+labIndex+" \n"+
					"LVRAI"+labIndex+": "+newTemp + "= 1 \n LFIN"+labIndex+": \n"
					
					, newTemp));
			labIndex++;
			break;
		case 35:
			Etemp = type.pop();
			code.add(new Code("", type.peek().val +"."+ Etemp.val));
			type.add(Etemp);
			break;
		case 36:
			temp1 = code.pop();
			temp2 = code.pop();
			code.add(new Code(temp2.code + "\n"
					+ "if( "+temp2.place+" == 0 ) goto LFIN"+labIndex+" \n"
					+ temp1.code
					+ "\n LFIN"+labIndex+":"
					,""));
			labIndex++;
			break;
		
		case 37:
			Etemp = type.pop();
			code.pop();
			code.add(new Code("",type.peek().val+"["+Etemp.val+"]"));
			type.add(Etemp);
			break;
		}
			
	}
	
	void Error(String error) {
		System.out.println(error);
		System.exit(0);
	}
	
	
}
