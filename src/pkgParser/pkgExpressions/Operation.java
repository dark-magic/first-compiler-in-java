package pkgParser.pkgExpressions;

/**
 * combination of an operator and the rhs of the operation.
 */
public class Operation {
	private String op;
	private AbstrExpression rhs;
	
	public Operation(String op, AbstrExpression rhs) {
		super();
		setOp(op);
		setRhs(rhs);
	}

	public String getOp() {
		return op;
	}

	private void setOp(String op) {
		this.op = op;
	}

	public AbstrExpression getRhs() {
		return rhs;
	}

	private void setRhs(AbstrExpression rhs) {
		this.rhs = rhs;
	}
}
