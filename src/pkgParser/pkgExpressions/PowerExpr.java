package pkgParser.pkgExpressions;

import pkgNameLookup.NameLookupTable;
import pkgScanner.Scanner;

/**
 * PowerExpr := unaryExpr { "to_the_power_of" unaryExpr }.
 */
public class PowerExpr extends AbstrExpression {

	public PowerExpr(Scanner s) {
		super(s);
		lhs = new UnaryExpr(this.scanner);
	}

	@Override
	public boolean parse() {
		boolean isOk = (lhs.parse());
		
		if(isOk) {
			scanner.getNextToken();
			
			while(scanner.getCurrToken().isValue("to_the_power_of")) {
				String op = scanner.getCurrToken().getValue();
				AbstrExpression rhs_expr = new UnaryExpr(this.scanner);
				boolean thisOk = (rhs_expr.parse());
				
				if(thisOk) {
					rhs.add(new Operation(op, rhs_expr));
				}
				
				isOk = (isOk && thisOk);
				scanner.getNextToken();
			}
			
			scanner.ungetToken(scanner.getCurrToken());
		}
		
		return isOk;
	}

	@Override
	public boolean checkSemantics(NameLookupTable nameLookupTable) {
		boolean isOk = lhs.checkSemantics(nameLookupTable);
		
		if(rhs.size() > 0) {
			int idx = 0;
			isOk = lhs.getResultingDatatype(nameLookupTable).equals("number");
			
			while(isOk && idx < rhs.size()) {
				AbstrExpression rhs_expr = rhs.get(idx).getRhs(); 
				isOk = (rhs_expr.checkSemantics(nameLookupTable) && rhs_expr.getResultingDatatype(nameLookupTable).equals("number"));
				idx++;
			}
		}
		
		return isOk;
	}

	@Override
	public String getResultingDatatype(NameLookupTable nlt) {
		return lhs.getResultingDatatype(nlt);
	}
	
	@Override
	public boolean generate(int indent) {
		boolean isOk = true;
		int nextIndent = indent;
		
		if(rhs.size() == 0) {
			isOk = lhs.generate(indent);
		}
		else {
			int idx = 0;
			
			if(indent == 0 || indent == 1) {
				System.out.print("`expr ");
				nextIndent += 2;
			}
			
			isOk = lhs.generate(nextIndent);
			
			while(isOk && idx < rhs.size()) {
				System.out.print(" ** ");
				isOk = rhs.get(idx).getRhs().generate(nextIndent);
				idx++;
			}
			
			if(indent == 0 || indent == 1) {
				System.out.print("`");
			}
		}
		
		return isOk;
	}

}
