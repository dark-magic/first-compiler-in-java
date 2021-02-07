package pkgParser;

import pkgNameLookup.NameLookupTable;
import pkgScanner.Scanner;

/**
 * UntilStmt := "until" conditionalBlock.
 */
public class UntilStmt extends Stmt {
	private ConditionalBlock loopBlock;
	
	public UntilStmt(Scanner s) {
		super(s);
		loopBlock = new ConditionalBlock(this.scanner);
	}
	

	@Override
	public boolean parse() {
		return (loopBlock.parse());
	}

	public boolean checkSemantics(NameLookupTable nameLookupTable) {
		return (loopBlock.checkSemantics(nameLookupTable) && loopBlock.getResultingDatatype(nameLookupTable).equals("bool"));
	}
	
	@Override
	public boolean generate(int indent) {
		System.out.println();
		System.out.print(getIndent(indent) + "until ");
		return loopBlock.generateLoopBlock(indent);
	}

}
