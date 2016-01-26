package expressions;

@SuppressWarnings("rawtypes")
public class Operand  implements Comparable, ExpressionElement {
	private static final String OPERAND_STRING_IS_NULL = 
			"angegebener Operand  \"null\" ungueltig";

	private static final String OPERAND_STRING_IS_EMPTY = 
			"Operand String ist leer";
	private String operand;
	private double value;
	
	public Operand(String s) {
		if (s == null)
			throw new IllegalArgumentException(OPERAND_STRING_IS_NULL);
		if (s.isEmpty()) 
			throw new IllegalArgumentException(OPERAND_STRING_IS_EMPTY);
		this.operand = s;
	}

	public int compareTo(Object obj) {
		try {
			String comp = ((Operand) obj).getOperand();
			return operand.compareTo(comp);
		} catch (ClassCastException e) {
			return -1;
		}
	}

	@Override
	public int hashCode() {
		int hashValue = 0;
		for (int i = 0; i < operand.length(); i++) {
			hashValue += operand.charAt(i)*(i+1);
		}
		return hashValue;
	}

	@Override
	public boolean equals(Object obj) {
		try {
		return (operand.equals(((Operand)obj).getOperand()));
		} catch (ClassCastException e) {
			return false;
		}
	}
	
	public double evaluate() {
		return value;
	}

	public String getOperand() {
		return operand;
	}


	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	public String toString() {
		return "{" + operand + "::" + value + "}";
	}

}
