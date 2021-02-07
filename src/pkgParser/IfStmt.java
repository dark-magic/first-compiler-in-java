package pkgParser;

import java.util.ArrayList;

import pkgNameLookup.NameLookupTable;
import pkgScanner.Scanner;

/**
 * IfStmt := "if" conditionalBlock { "else" "if" conditionalBlock } [ "else" block ].
 */
public class IfStmt extends Stmt {
	private ConditionalBlock thenBlock;
	private ArrayList<ConditionalBlock> elseIfs;
	private Block elseBlock;

	public IfStmt(Scanner s) {
		super(s);
		thenBlock = new ConditionalBlock(this.scanner);
		elseIfs = new ArrayList<>();
		elseBlock = null;
	}
	

	@Override
	public boolean parse() {
		boolean isOk = (thenBlock.parse());
		boolean elseParsed = false;
		
		scanner.getNextToken();
		
		while(scanner.getCurrToken().isValue("else") && !elseParsed) {
			scanner.getNextToken();
			
			if(scanner.getCurrToken().isValue("if")) {
				ConditionalBlock ifStmt = new ConditionalBlock(this.scanner);
				isOk = ifStmt.parse();
				
				if(isOk) {
					elseIfs.add(ifStmt);
				}
			}
			else {
				scanner.ungetToken(scanner.getCurrToken());
				elseBlock = new Block(this.scanner);
				isOk = elseBlock.parse();
				elseParsed = true;
			}
			
			scanner.getNextToken();
		}
		
		scanner.ungetToken(scanner.getCurrToken());
		return isOk;
	}

	public boolean checkSemantics(NameLookupTable nameLookupTable) {
		boolean isOk = (thenBlock.checkSemantics(nameLookupTable) && thenBlock.getResultingDatatype(nameLookupTable).equals("bool"));
		int idx = 0;
		
		while(isOk && idx < elseIfs.size()) {
			isOk = (elseIfs.get(idx).checkSemantics(nameLookupTable) && elseIfs.get(idx).getResultingDatatype(nameLookupTable).equals("bool"));
			idx++;
		}
		
		isOk = isOk && (elseBlock == null || elseBlock.checkSemantics(nameLookupTable));
		return isOk;
	}
	
	@Override
	public boolean generate(int indent) {
		boolean isOk = true;
		int idx = 0;
		String ind = getIndent(indent);
		
		System.out.println();
		System.out.print(ind + "if ");
		isOk = thenBlock.generate(indent);
		
		while(isOk && idx < elseIfs.size()) {
			System.out.print(ind + "elif ");
			isOk = elseIfs.get(idx).generate(indent);
			idx++;
		}
		
		if(isOk && elseBlock != null) {
			System.out.print(ind + "else ");
			isOk = elseBlock.generate(indent + LEVEL_IND);
		}
		
		System.out.println(ind + "fi");
		System.out.println();
		return isOk;
	}

}
