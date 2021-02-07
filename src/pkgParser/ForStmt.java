package pkgParser;

import pkgNameLookup.NameLookupTable;
import pkgParser.pkgExpressions.Expression;
import pkgScanner.Scanner;

/**
 * ForStmt := "for" assignmentStmt "to" expression ["by" expression] block.
 */
public class ForStmt extends Stmt {
	private AssignmentStmt assignment;
	private Expression toExpression;
	private Expression byExpression;
	private Block loopBlock;
	
	public ForStmt(Scanner s) {
		super(s);
		assignment = new AssignmentStmt(this.scanner);
		toExpression = new Expression(this.scanner);
		byExpression = null;
		loopBlock = new Block(this.scanner);
	}
	

	@Override
	public boolean parse() {
		boolean isOk = (assignment.parse(true) && scanner.getNextToken().isValue("to") && toExpression.parse());
		
		scanner.getNextToken();
		
		if(scanner.getCurrToken().isValue("by")) {
			byExpression = new Expression(this.scanner);
			isOk = (byExpression.parse());
		}
		else {
			scanner.ungetToken(scanner.getCurrToken());
		}
		
		isOk = (loopBlock.parse());
		return isOk;
	}

	public boolean checkSemantics(NameLookupTable nameLookupTable) {
		return (assignment.checkSemantics(nameLookupTable) && toExpression.checkSemantics(nameLookupTable) && toExpression.getResultingDatatype(nameLookupTable).equals("number")
				&& (byExpression == null || (byExpression.checkSemantics(nameLookupTable) && byExpression.getResultingDatatype(nameLookupTable).equals("number"))) && loopBlock.checkSemantics(nameLookupTable));
	}
	
	@Override
	public boolean generate(int indent) {
		boolean isOk = true;
		
		System.out.println();
		System.out.print(getIndent(indent) + "for (( ");
		isOk = assignment.generate(0);
		System.out.print(" ; ");
		isOk = isOk && toExpression.generate(0);
		System.out.print(" ; ");
		
		if(byExpression == null) {
			System.out.print("expr `$" + assignment.getIdentifier() + " + 1`");
		}
		else {
			isOk = isOk && byExpression.generate(0);
		}
		
		System.out.print(" ))");
		isOk = isOk && loopBlock.generate(indent + LEVEL_IND);
		System.out.println();
		return isOk;
	}

}
