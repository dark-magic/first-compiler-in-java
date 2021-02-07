package pkgParser;

import pkgNameLookup.AccessType;
import pkgNameLookup.NameLookupTable;
import pkgScanner.Scanner;
import pkgScanner.TokenType;

/**
 * DeclarationStmt := "define" identifier "as_a" datatype ".".
 */
public class DeclarationStmt extends Stmt {
	private String identifier;
	private String datatype;
	
	public DeclarationStmt(Scanner s) {
		super(s);
		identifier = "";
		datatype = "invalid";
	}

	@Override
	public boolean parse() {
		boolean isOk = (scanner.getNextToken().isOfType(TokenType.identifier));
		
		if(isOk) {
			identifier = scanner.getCurrToken().getValue();
			isOk = (scanner.getNextToken().isValue("as_a") && scanner.getNextToken().isOfType(TokenType.datatype));
			
			if(isOk) {
				datatype = scanner.getCurrToken().getValue();
				isOk = (scanner.getNextToken().isValue("."));
			}
		}
		
		return isOk;
	}

	public boolean checkSemantics(NameLookupTable nameLookupTable) {
		boolean isOk = (!nameLookupTable.containsIdentifier(identifier));
		
		if(isOk) {
			nameLookupTable.addIdentifier(identifier, datatype, AccessType.readWrite);
		}
		
		return isOk;
	}
	
	@Override
	public boolean generate(int indent) {
		boolean isOk = true;
		String ind = getIndent(indent);
		
		switch(datatype) {
			case "bool":
				System.out.println(ind + identifier + " = false");
				break;
				
			case "number":
				System.out.println(ind + identifier + " = 0");
				break;
				
			case "string":
				System.out.println(ind + identifier + " = \"\"");
				break;
				
			default:
				isOk = false;
		}
		
		return isOk;
	}

}
