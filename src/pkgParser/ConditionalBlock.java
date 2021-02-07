package pkgParser;

import pkgNameLookup.NameLookupTable;
import pkgParser.pkgExpressions.Expression;
import pkgScanner.Scanner;

/**
 * ConditionalBlock := expression block.
 */
public class ConditionalBlock extends Stmt {
	private Expression condition;
	private Block block;
	
	
	public ConditionalBlock(Scanner s) {
		super(s);
		condition = new Expression(this.scanner);
		block = new Block(this.scanner);
	}
	

	@Override
	public boolean parse() {
		return (condition.parse() && block.parse());
	}

	public boolean checkSemantics(NameLookupTable nameLookupTable) {
		return (condition.checkSemantics(nameLookupTable) && block.checkSemantics(nameLookupTable));
	}
	
	public String getResultingDatatype(NameLookupTable nlt) {
		return condition.getResultingDatatype(nlt);
	}
	
	@Override
	public boolean generate(int indent) {
		boolean isOk = condition.generate(0);
		
		System.out.println("; then");
		isOk = block.generate(indent + LEVEL_IND);
		
		return isOk;
	}

	public boolean generateLoopBlock(int indent) {
		boolean isOk = condition.generate(0);
		
		System.out.println("; do");
		isOk = block.generate(indent + LEVEL_IND);
		System.out.println(getIndent(indent) + "done");
		System.out.println();
		return isOk;
	}
	
	public boolean generateCaseBlock(int indent) {
		boolean isOk = true;
		
		System.out.print(getIndent(indent));
		isOk = condition.generate(0);
		System.out.println(")");
		isOk = block.generate(indent + LEVEL_IND);
		System.out.println(getIndent(indent) + ";;");
		return isOk;
	}
}
