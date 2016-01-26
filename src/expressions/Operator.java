package expressions;

public class Operator implements ExpressionElement {
	private ExpressionElement left, right;
	private OperatorType type;
	private double value;
	
	
	
	public Operator(ExpressionElement left, OperatorType type, ExpressionElement right){
		this.left = left;
		this.type = type;
		this.right = right;
	}
	
	public Operator(OperatorType type) {
		this (null, type ,null);
	}

	public ExpressionElement getLeft() {
		return left;
	}

	public void setLeft(ExpressionElement left) {
		this.left = left;
	}

	public ExpressionElement getRight() {
		return right;
	}

	public void setRight(ExpressionElement right) {
		this.right = right;
	}

	public OperatorType getType() {
		return type;
	}

	public double getValue() {
		return value;
	}

	public static boolean isPriority(OperatorType first, OperatorType second) {
		return (first.getValue() > second.getValue());
	}

	public double evaluate()  {
		switch (type) {
		case ADD: 
			value = left.evaluate() + right.evaluate();
			return value;
		case SUB: 
			value = left.evaluate() - right.evaluate();
			return value;
		case MUL: 
			value = left.evaluate() * right.evaluate();
			return value;
		case DIV: 
			value = left.evaluate() / right.evaluate();
			return value;
		}
		return 0;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(type);
		builder.append(":");
		builder.append(value);
		builder.append("]");
		return builder.toString();
	}
}
