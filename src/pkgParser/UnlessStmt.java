package pkgParser;

import pkgNameLookup.NameLookupTable;
import pkgScanner.Scanner;

/**
 * UnlessStmt := "unless" conditionalBlock [ "else" block ].
 */
public class UnlessStmt extends Stmt {
	private ConditionalBlock thenBlock;
	private Block elseBlock;
	
	public UnlessStmt(Scanner s) {
		super(s);
		thenBlock = new ConditionalBlock(this.scanner);
		elseBlock = null;
	}
	

	@Override
	public boolean parse() {
		boolean isOk = (thenBlock.parse());
		
		scanner.getNextToken();
		
		if(scanner.getCurrToken().isValue("else")) {
			elseBlock = new Block(this.scanner);
			isOk = (elseBlock.parse());
		}
		else {
			scanner.ungetToken(scanner.getCurrToken());
		}
		
		return isOk;
	}

	public boolean checkSemantics(NameLookupTable nameLookupTable) {
		return (thenBlock.checkSemantics(nameLookupTable) && thenBlock.getResultingDatatype(nameLookupTable).equals("bool") && (elseBlock == null || elseBlock.checkSemantics(nameLookupTable)));
	}
	
	@Override
	public boolean generate(int indent) {
		boolean isOk = true;
		String ind = getIndent(indent);
		
		System.out.println();
		System.out.print(ind + "if !");
		isOk = thenBlock.generate(indent);
		
		if(isOk && elseBlock != null) {
			System.out.print(ind + "else ");
			isOk = elseBlock.generate(indent + LEVEL_IND);
		}
		
		System.out.println(ind + "fi");
		System.out.println();
		return isOk;
	}

}
