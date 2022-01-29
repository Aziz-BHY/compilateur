package compilateur;

public class Code {
	public String code;
	public String place;
	Code(String code, String place){
		this.code = code;
		this.place = place;
	}
	public String toString() {
		return "[code="+this.code+", place="+this.place+"]";
	}
}
