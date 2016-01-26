package expressions;

public enum OperatorType {
	ADD(0, '+'), 
	SUB(0, '-'), 
	MUL(1, '*'), 
	DIV(1, '/');
	
	private int value;
	private char token;
	
	private OperatorType(int value, char token) {
		this.value = value;
		this.token = token;
	}
	
	public int getValue() {
		return value;
	}
	
	public String toString() {
		return (String.valueOf(token));
	}
}
