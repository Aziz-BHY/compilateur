package compilateur;

public class Lexicale {
	private Categorie categorie;
    private String lexeme;

    public Lexicale(Categorie categorie, String lexeme) {
        this.categorie=categorie;
        this.lexeme=lexeme;
    }

    public Categorie getCategorie() {
        return categorie;
    }
    public String getLexeme(){
        return lexeme;
    }

    public String toString() {
        return "<"+categorie.toString()+","+lexeme+">";
    }
}
