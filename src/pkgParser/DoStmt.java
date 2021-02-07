package pkgParser;

import pkgNameLookup.NameLookupTable;
import pkgParser.pkgExpressions.Expression;
import pkgScanner.Scanner;

/**
 * DoStmt := "do" block ("while" | "until") expression.
 */
public class DoStmt extends Stmt {
	private Block loopBlock;
	private boolean isWhile;
	private Expression condition;
	
	public DoStmt(Scanner s) {
		super(s);
		loopBlock = new Block(this.scanner);
		isWhile = false;
		condition = new Expression(this.scanner);
	}
	

	@Override
	public boolean parse() {
		boolean isOk = (loopBlock.parse());
		
		if(isOk) {
			scanner.getNextToken();
			isWhile = (scanner.getCurrToken().isValue("while"));
			
			isOk = ( (isWhile || scanner.getCurrToken().isValue("until")) && condition.parse() && scanner.getNextToken().isValue("."));
		}
		
		return isOk;
	}

	public boolean checkSemantics(NameLookupTable nameLookupTable) {
		return (loopBlock.checkSemantics(nameLookupTable) && condition.checkSemantics(nameLookupTable) && condition.getResultingDatatype(nameLookupTable).equals("bool"));
	}
	
	@Override
	public boolean generate(int indent) {
		System.out.println();
		System.out.println(getIndent(indent) + "# do-statements are only available in the premium edition, buy it now for $3.59");
		System.out.println();
		return true;
	}

}
