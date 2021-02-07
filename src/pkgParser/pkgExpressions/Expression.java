package pkgParser.pkgExpressions;

import pkgNameLookup.NameLookupTable;
import pkgScanner.Scanner;

/**
 * Expression := orExpr.
 * orExpr := andExpr { "or" andExpr }.
 * andExpr := compExpr { "and" compExpr }.
 * compExpr := addSubExpr { comparing_op addSubExpr }.				// NOTE: comparing_op, see Scanner; NOTE: equal, not equal: Ich definiere meine priorities wie ich will ;)
 * addSubExpr := multDivExpr { ("plus" | "minus") multDivExpr }.
 * multDivExpr := powerExpr { ("times" | "divided_by" | "modulo") powerExpr }.
 * powerExpr := unaryExpr { "to_the_power_of" unaryExpr }.
 * unaryExpr := "not" primaryExpr | primaryExpr.
 * primaryExpr := identifier | literal | "(" expression ")".
 */
public class Expression extends AbstrExpression {

	public Expression(Scanner s) {
		super(s);
		lhs = new OrExpr(this.scanner);
	}

	@Override
	public boolean parse() {
		return (lhs.parse());
	}

	@Override
	public boolean checkSemantics(NameLookupTable nameLookupTable) {
		return lhs.checkSemantics(nameLookupTable);
	}

	@Override
	public String getResultingDatatype(NameLookupTable nlt) {
		return lhs.getResultingDatatype(nlt);
	}
	
	@Override
	public boolean generate(int indent) {		
		return lhs.generate(indent);
	}

}
