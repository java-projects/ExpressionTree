package expressions;

public class Bracket implements ExpressionElement {
	private static final boolean OPENED = true;

	private boolean type;
	
	public Bracket(boolean type) {
		this.type = type;
	}
	
	public boolean isOpen() {
		return type;
	}
	
	public String toString() {
		if (type == OPENED) {
			return "(";
		}
		return ")";
	}
	
	public double evaluate() {
		return 0;
	}
	
	public double getValue() {
		return 0;
	}
}
