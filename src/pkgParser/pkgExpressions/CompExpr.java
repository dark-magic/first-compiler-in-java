package pkgParser.pkgExpressions;

import pkgNameLookup.NameLookupTable;
import pkgScanner.Scanner;
import pkgScanner.TokenType;

/**
 * CompExpr := addSubExpr { comparing_op addSubExpr }.		// NOTE: comparing_op, see Scanner; NOTE: equal, not equal: Ich definiere meine priorities wie ich will ;)
 */
public class CompExpr extends AbstrExpression {

	public CompExpr(Scanner s) {
		super(s);
		lhs = new AddSubExpr(this.scanner);
	}

	@Override
	public boolean parse() {
		boolean isOk = (lhs.parse());
		
		if(isOk) {
			scanner.getNextToken();
			
			while(scanner.getCurrToken().isOfType(TokenType.comparingOp)) {
				String op = scanner.getCurrToken().getValue();
				AbstrExpression rhs_expr = new AddSubExpr(this.scanner);
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
			String datatype = lhs.getResultingDatatype(nameLookupTable);
			int idx = 0;
			
			while(isOk && idx < rhs.size()) {
				AbstrExpression rhs_expr = rhs.get(idx).getRhs(); 
				isOk = (rhs_expr.checkSemantics(nameLookupTable) && rhs_expr.getResultingDatatype(nameLookupTable).equals(datatype));
				idx++;
			}
		}
		
		return isOk;
	}

	@Override
	public String getResultingDatatype(NameLookupTable nlt) {
		return (rhs.size() > 0) ? "bool" : lhs.getResultingDatatype(nlt);
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
			
			if(indent == 0 || indent == 2) {
				System.out.print("[ ");
				nextIndent += 1;
			}
			
			isOk = lhs.generate(nextIndent);
			
			while(isOk && idx < rhs.size()) {
				switch(rhs.get(idx).getOp()) {
					case "is":
						System.out.print(" -eq ");
						break;
						
					case "is_not":
						System.out.print(" -ne ");
						break;
						
					case "is_less_than":
						System.out.print(" -lt ");
						break;
						
					case "is_more_than":
						System.out.print(" -gt ");
						break;
						
					case "is_or_is_less_than":
						System.out.print(" -le ");
						break;
						
					case "is_or_is_more_than":
						System.out.print(" -ge ");
						break;
						
					default:
						isOk = false;
				}
				
				isOk = rhs.get(idx).getRhs().generate(nextIndent);
				idx++;
			}
			
			if(indent == 0 || indent == 2) {
				System.out.print(" ]");
			}
		}
		
		return isOk;
	}

}
