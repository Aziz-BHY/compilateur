package compilateur;



public class main {
    public static void main(String[] args) {
    	Reader read = new Reader("C:\\Users\\Aziz\\OneDrive\\Bureau\\me.txt");    	
    	Analyse analyse = new Analyse(read.getcontent());
    	analyse.init();
    }	

}
