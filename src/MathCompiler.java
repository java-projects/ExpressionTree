

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import dataStructures.DList;
import exceptions.FileNotFoundException;
import exceptions.FileNotKnownException;
import exceptions.FileNotReadableException;
import expressions.Bracket;
import expressions.ExpressionElement;
import expressions.Operand;
import expressions.Operator;
import expressions.OperatorType;



public class MathCompiler {
	private static final char     ADD		  = '+';
	private static final char     SUB		  = '-';
	private static final char     DIV    	  = '/';
	private static final char     MUL   	  = '*';
	private static final char     OPEN   	  = '(';
	private static final char     CLOSED	  = ')';

	private static final int      SIZE		  = 10000;
	private static final String   PATTERN     = "[a-zA-Z_]?([a-zA-Z0-9_]*)";
	private static final String   DELIMITER   = "=";
	private static final String   SET_EMPTY   = "";
	
	private static final String   USAGE       = "Usage: Identifier=1.0";
	
	
	private IndexStructure<Operand> symbolTable;
	private Parser expParser;
	private ExpressionTree expTree;
	
	
	public MathCompiler() {
		try{
			symbolTable = new HashTable<Operand>(SIZE);
			expTree = new ExpressionTree();
			expParser = new Parser(symbolTable);
		} catch (Exception e) {
			System.out.println("SymbolTabelle konnte "
					+ "nicht initialisiert werden!");
			System.exit(1);
		}
	}
	
	public static void main(String[] args) {
		MathCompiler compiler = new MathCompiler();
		String filePath = null;
		if (args.length == 1) {
			filePath = args[0];
		} else {
			System.out.println("Bitte Dateinamen eingeben: ");
			BufferedReader stdin = new BufferedReader(
					new InputStreamReader(System.in));
			try {
				filePath = stdin.readLine();
			} catch (IOException e) {
				System.out.println("Eingabe konnte nicht gelesen werden.");
			}
		}
		compiler.run(filePath);
	}
	
	private void run(String filePath) {
		checkFile(filePath);
		DList<ExpressionElement> tokens;
		LineNumberReader input = null;
		try {
			input = new LineNumberReader(new FileReader(filePath));
			String currentLine;
			while (!(currentLine = input.readLine().trim()).isEmpty()) {
				readDeclaration(currentLine, input.getLineNumber());
			}
			currentLine = input.readLine().trim();
			tokens = tokenize(currentLine);
			checkBracketNumber(tokens);
			expTree.setRoot(expParser.parseExpression(tokens));
			printStructure(currentLine);
			currentLine = input.readLine().trim();
			if (!currentLine.isEmpty()) {
				throw new ParseException("Fehler in Zeile "
						+ input.getLineNumber() + ": \"" + currentLine
						+ "\" nicht leere Zeile nach Ausdruck erkannt");
			}
			currentLine = SET_EMPTY;
			while (currentLine != null) {
				if (!currentLine.trim().isEmpty()) {
					readAssignment(currentLine);	
				}
				readAssignmentSector(input);
				currentLine = input.readLine();
			}
		} catch (Exception e) {
			System.out.println("Error in Zeile " + input.getLineNumber() + ": "
					+ e);
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {

			}
		}
	}
		
	private void readDeclaration(String line, int lineNr) throws Exception {
		if (line.matches(PATTERN)) {
			symbolTable.add(new Operand(line));
		} else {
			throw new DeclarationException("Fehler in Zeile " + lineNr + "<< "
					+ line + " >> konnte nicht gefunden werden");
		}
	}
	
	public DList<ExpressionElement> tokenize(String expression) {
		DList<ExpressionElement> tokens = new DList<ExpressionElement>();
		expression.replaceAll("\t+", "\t").replaceAll(" +", " ");
		expression = expression.trim().replaceAll("\t", "").replaceAll(" ", "");
		StringBuffer identifier = new StringBuffer();
		char currentChar;
		for (int i = 0; i < expression.length(); i++) {
			currentChar = expression.charAt(i);
			switch (currentChar) {
			case (ADD):
				tokens.add(new Operator(OperatorType.ADD));
				break;
			case (SUB):
				tokens.add(new Operator(OperatorType.SUB));
				break;
			case (DIV):
				tokens.add(new Operator(OperatorType.DIV));
				break;
			case (MUL):
				tokens.add(new Operator(OperatorType.MUL));
				break;
			case (OPEN):
				tokens.add(new Bracket(true));
				break;
			case (CLOSED):
				tokens.add(new Bracket(false));
				break;
			default:
				if (currentChar == ' ' | currentChar == '\t') {
					if (identifier.toString() != "") {
						tokens.add(new Operand(identifier.toString()));
					}
					identifier.delete(0, identifier.length());
					break;
				}
				identifier.append(currentChar);
				try {
					if (!Character.isJavaIdentifierPart(expression.charAt(i + 1))) {
						tokens.add(new Operand(identifier.toString()));
						identifier.delete(0, identifier.length());
					}
				} catch (StringIndexOutOfBoundsException e) {
					tokens.add(new Operand(identifier.toString()));
				}
				break;
			}
		}
		return tokens;
	}
	
	private void checkBracketNumber(DList<ExpressionElement> tokens) 
			throws ParseException {
		int counter = 0;
		for (int i = 0; i < tokens.size(); i++) {
			Bracket bracket;
			try {
				bracket = (Bracket) tokens.get(i);
			} catch (ClassCastException e) {
				continue;
			}
			if (bracket == null) {
				throw new ParseException("token= null :: " + i);
			}
			if (bracket.isOpen()) {
				counter++;
			}
			if (!bracket.isOpen()) {
				counter--;
			}
		}
		if (counter < 0) {
			throw new ParseException("Fehler bei der Klammersetzung");
		} else if (counter > 0) {
			throw new ParseException("Expected \")\" but was EOF");
		}
	}
	
	
	private void readAssignmentSector(LineNumberReader input) 
								throws IOException, ParseException {
		String currentLine = input.readLine();
		while (!(currentLine.trim()).isEmpty() & currentLine != null) {
			readAssignment(currentLine);
			if ((currentLine = input.readLine()) == null) {
				break;
			}
		}
		printCalculation();
	}
	
	
	private void readAssignment(String line) throws ParseException {
		double value;
		line = line.trim();
		String[] components = line.split(DELIMITER);
		if (components.length != 2)
			throw new ParseException("Zuweisung " + line + 
					"konnte nicht geparst werden." + USAGE);
		else {
			Operand o = (Operand) symbolTable.find(
					new Operand(components[0].trim()));
			try {
					value = Double.parseDouble(
							components[1].trim());
				} catch (NumberFormatException e) {
					throw new ParseException("Zahl in Zuweisung " + line 
							+ " konnte nicht geparst werden. Kein Double Wert");
				}
			o.setValue(value);
		}
	}
	
	private void printCalculation() {
		System.out.println("SymbolTabelle:\n" + symbolTable);
		System.out.println("Expression Tree vor der Berechnung:\n" + expTree);
		expTree.getRoot().evaluate();
		System.out.println("Ergebnis = " + expTree.getRoot().getValue() + "\n");
		System.out.println("Expression Tree nach der Berechnung:\n" + expTree);
	}
	
	private void printStructure(String expression) {
		System.out.println("\nAusdruck = " + expression + "\n");
		System.out.println("Expression Tree ohne Werte:\n" + expTree);
	}
	
	private void checkFile(String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				throw new FileNotFoundException("Datei wurde nicht gefunden.");
			}
			if (!file.isFile()) {
				throw new FileNotKnownException("Datei ist keine einfache Datei.");
			}
			if (!file.canRead()) {
				throw new FileNotReadableException("Datei kann nicht gelesen werden.");
			}
		} catch (Exception e) {
			System.out.println(e);
			System.exit(1);
		}
	}
	
}
