package pkgParser.pkgExpressions;

import pkgNameLookup.NameLookupTable;
import pkgScanner.Scanner;
import pkgScanner.Token;
import pkgScanner.TokenType;

/**
 * PrimaryExpr := identifier | literal | "(" expression ")".
 */
public class PrimaryExpr extends AbstrExpression {
	private Token lhs_tok;
	private boolean isKlammert;
	
	public PrimaryExpr(Scanner s) {
		super(s);
		lhs_tok = null;
		isKlammert = false;
	}


	@Override
	public boolean parse() {
		boolean isOk;
		
		scanner.getNextToken();
		
		if(scanner.getCurrToken().isValue("(")) {
			isKlammert = true;
			lhs = new Expression(this.scanner);
			isOk = (lhs.parse() && scanner.getNextToken().isValue(")"));
		}
		else {
			isOk = (scanner.getCurrToken().isOfType(TokenType.identifier) || scanner.getCurrToken().isOfType(TokenType.literal));
			
			if(isOk) {
				lhs_tok = scanner.getCurrToken();
			}
		}
		
		return isOk;
	}

	@Override
	public boolean checkSemantics(NameLookupTable nameLookupTable) {
		boolean isOk = true;
		
		if(lhs_tok != null && lhs_tok.isOfType(TokenType.identifier)) {
			isOk = nameLookupTable.containsIdentifier(lhs_tok.getValue());
		}
		else if(lhs != null) {
			isOk = lhs.checkSemantics(nameLookupTable);
		}
		
		return isOk;
	}

	@Override
	public String getResultingDatatype(NameLookupTable nlt) {
		String datatype = "invalid";
		
		if(lhs_tok != null) {
			if(lhs_tok.isOfType(TokenType.identifier)) {
				datatype = nlt.getByIdentifier(lhs_tok.getValue()).getType();
			}
			else if(scanner.isBooleanLiteral(lhs_tok.getValue())) {
				datatype = "bool";
			}
			else if(scanner.isNumberLiteral(lhs_tok.getValue())) {
				datatype = "number";
			}
			else if(scanner.isStringLiteral(lhs_tok.getValue())) {
				datatype = "string";
			}
		}
		else if(lhs != null) {
			datatype = lhs.getResultingDatatype(nlt);
		}
		
		return datatype;
	}
	
	@Override
	public boolean generate(int indent) {
		boolean isOk = true;
		
		if(isKlammert && indent != 0) {
			System.out.print("(");
		}
		
		if(lhs_tok != null) {
			if(lhs_tok.isOfType(TokenType.identifier)) {
				System.out.print("$");
			}
			
			System.out.print(lhs_tok.getValue());
		}
		else {
			isOk = lhs.generate(indent);
		}
		
		if(isKlammert && indent != 0) {
			System.out.print(")");
		}
		
		return isOk;
	}

}
