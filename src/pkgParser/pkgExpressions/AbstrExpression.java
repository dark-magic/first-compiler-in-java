package pkgParser.pkgExpressions;

import java.util.ArrayList;

import pkgNameLookup.NameLookupTable;
import pkgParser.Stmt;
import pkgScanner.Scanner;

/**
 * the base class for all priority-levels in the expression parse hierachy
 */
public abstract class AbstrExpression extends Stmt {
	protected AbstrExpression lhs;
	protected ArrayList<Operation> rhs;
	
	public AbstrExpression(Scanner s) {
		super(s);
		lhs = null;
		rhs = new ArrayList<>();
	}
	
	public abstract String getResultingDatatype(NameLookupTable nlt);
}
