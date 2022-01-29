package compilateur;

public class E {
	public String val;
	public String type;
	
	E(String val , String type){
		this.val = val;
		this.type = type;
	}
	
	public String toString() {
		return "[val="+this.val+", type="+this.type+"]";
	}
}
