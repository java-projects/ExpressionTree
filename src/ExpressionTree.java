import expressions.*;


public class ExpressionTree {
	private ExpressionElement root;
	
	public ExpressionTree() {
		root = null;
	}
	
	private String padding(String s, int level) {
		StringBuilder builder = new StringBuilder();
		int i;
		for (i = 0; i < level; i++) {
			builder.append(s);
		}
		return builder.toString();
	}
	
	private String toString(ExpressionElement node, int level) {
		StringBuilder builder = new StringBuilder();
		if (node == null) {
			builder.append(padding("|  ", level));
			builder.append("~\n");
		} else {
			if (node.getClass().equals(Operand.class)) {
				builder.append(padding("|  ", level));
				builder.append("+--" + node + "\n");
			}
		}
		if (node.getClass().equals(Operator.class)) {
			Operator opt = (Operator) node;
			builder.append(toString(opt.getRight(), level + 1));
			builder.append(padding("|  ", level));
			builder.append("+--" + node + "\n");
			builder.append(toString(opt.getLeft(), level + 1));
		}
		return builder.toString();
	}
	
	@Override
	public String toString() {
		toString(root, 0);
		return toString(root, 0);
	}

	public ExpressionElement getRoot() {
		return root;
	}

	public void setRoot(ExpressionElement root) {
		this.root = root;
	}
}
