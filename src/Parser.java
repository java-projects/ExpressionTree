import dataStructures.ArrayEmptyException;
import dataStructures.ArrayFullException;
import dataStructures.DList;
import dataStructures.Stack;
import expressions.Bracket;
import expressions.ExpressionElement;
import expressions.Operand;
import expressions.Operator;


public class Parser {
	private IndexStructure<Operand> symbolTable;
	private Stack<ExpressionElement> opndStack; 
	private Stack<ExpressionElement> optStack;  
	private ExpressionElement opTop;
	private Operator opThis;
	
	public Parser(IndexStructure<Operand> symbolTable) {
		this.symbolTable = symbolTable;
		 opndStack = new Stack<ExpressionElement>(1000);
		 optStack = new Stack<ExpressionElement>(1000);
	}
	
	public ExpressionElement parseExpression(DList<ExpressionElement> tokens)
			throws ParseException, Exception {
		
		for (int i = 0; i < tokens.size(); i++) {
			ExpressionElement inputItem = (ExpressionElement) tokens.get(i);
			if (inputItem.getClass().equals(Operand.class)) {
				Operand opnd = (Operand) inputItem;
				if (symbolTable.find(opnd) != null) {
					opndStack.push(symbolTable.find(opnd));
				} else {
					throw new ParseException("Konnte '" + opnd + 
							"' nicht in SymbolTabelle finden.");
				}
			}
			if (inputItem.getClass().equals(Bracket.class)) {
				Bracket bracket = (Bracket) inputItem;
				if (bracket.isOpen()) {
					optStack.push(bracket);
				} else {
					while (!optStack.isEmpty()) {
						opTop = (ExpressionElement) optStack.pop();
						if (opTop.getClass().equals(Bracket.class)) {
							if (((Bracket) opTop).isOpen()) {
								break;
							}
						} else {
							createOperatorNode();
						}
					}
				}
			}
			if (inputItem.getClass().equals(Operator.class)) {
				opThis = (Operator) inputItem;
				if (optStack.isEmpty()) {
					optStack.push(opThis);
				} else {
					while (!optStack.isEmpty()) {
						opTop = (ExpressionElement) optStack.pop();
						if (opTop.getClass().equals(Bracket.class)) {
							if (((Bracket) opTop).isOpen()) {
								optStack.push(opTop);
							}
						} else if (!Operator.isPriority(
								((Operator) opTop).getType(),
								((Operator) opThis).getType())) {
							optStack.push(opTop);
						} else {
							createOperatorNode();
						} 
						if (opTop.getClass().equals(Bracket.class)) {
							if (((Bracket) opTop).isOpen()) {
								break;
							}
						}
						if (!Operator.isPriority(
								((Operator) opTop).getType(),
								((Operator) opThis).getType())) {
								break;
						}	
					}
					optStack.push(opThis);
				}
			}
		}
		while(!optStack.isEmpty()) {
			opTop = (ExpressionElement) optStack.pop();
			try {
				createOperatorNode();
			} catch (ClassCastException e) {
				throw new ParseException("Klammerung fehlerhaft.");
			}
		}
		ExpressionElement root = (ExpressionElement) opndStack.pop();
		if (!optStack.isEmpty() || !opndStack.isEmpty()) {
			throw new ParseException("Stacks nicht leer. Ausdruck ungueltig.");
		}
		return root;
	}
	
	
	private void createOperatorNode() throws ArrayEmptyException,
												ArrayFullException {
		Operator operator = (Operator) opTop;
		operator.setRight((ExpressionElement) opndStack
				.pop());
		operator.setLeft((ExpressionElement) opndStack
				.pop());
		opndStack.push(operator);
	}
	
}
