package pkgParser.pkgExpressions;

import pkgNameLookup.NameLookupTable;
import pkgScanner.Scanner;

/**
 * UnaryExpr := "not" primaryExpr | primaryExpr.
 */
public class UnaryExpr extends AbstrExpression {
	private String op;
	
	public UnaryExpr(Scanner s) {
		super(s);
		op = "nop";
		lhs = new PrimaryExpr(this.scanner);
	}

	@Override
	public boolean parse() {
		boolean isOk;
		
		scanner.getNextToken();
		
		if(scanner.getCurrToken().isValue("not")) {
			op = "not";
			isOk = lhs.parse();
		}
		else {
			scanner.ungetToken(scanner.getCurrToken());
			isOk = lhs.parse();
		}
		
		return isOk;
	}

	@Override
	public boolean checkSemantics(NameLookupTable nameLookupTable) {
		return (lhs.checkSemantics(nameLookupTable) && (!op.equals("not") || lhs.getResultingDatatype(nameLookupTable).equals("bool")));
	}

	@Override
	public String getResultingDatatype(NameLookupTable nlt) {
		return lhs.getResultingDatatype(nlt);
	}
	
	@Override
	public boolean generate(int indent) {
		boolean isOk = true;
		int nextIndent = indent;
		
		if(op.equals("nop")) {
			isOk = lhs.generate(indent);
		}
		else {
			if(indent == 0 || indent == 2) {
				System.out.print("[ ");
				nextIndent += 1;
			}
			
			System.out.print("! ");
			isOk = lhs.generate(nextIndent);
			
			if(indent == 0 || indent == 2) {
				System.out.print(" ]");
			}
		}
		
		return isOk;
	}

}
