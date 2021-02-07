package pkgParser;

import java.util.ArrayList;

import pkgNameLookup.NameLookupTable;
import pkgParser.pkgExpressions.Expression;
import pkgScanner.Scanner;

/**
 * SwitchStmt := "look_at" expression { "if_is" conditionalBlock } [ "else" block ].
 */
public class SwitchStmt extends Stmt {
	private Expression expressionToEvaluate;
	private ArrayList<ConditionalBlock> cases;
	private Block elseBlock;
	
	public SwitchStmt(Scanner s) {
		super(s);
		expressionToEvaluate = new Expression(this.scanner);
		cases = new ArrayList<>();
		elseBlock = new Block(this.scanner);
	}
	

	@Override
	public boolean parse() {
		boolean isOk = (expressionToEvaluate.parse() && scanner.getNextToken().isValue("{"));
		
		scanner.getNextToken();
		
		while(scanner.getCurrToken().isValue("if_is")) {
			ConditionalBlock caseBlock = new ConditionalBlock(this.scanner);
			isOk = (caseBlock.parse());
			
			if(isOk) {
				cases.add(caseBlock);
			}
			
			scanner.getNextToken();
		}
		
		if(scanner.getCurrToken().isValue("else")) {
			isOk = (elseBlock.parse());
		}
		else {
			scanner.ungetToken(scanner.getCurrToken());
		}
		
		return isOk;
	}

	public boolean checkSemantics(NameLookupTable nameLookupTable) {
		boolean isOk = (expressionToEvaluate.checkSemantics(nameLookupTable) && (elseBlock == null || elseBlock.checkSemantics(nameLookupTable)));
		int idx = 0;
		String datatype = expressionToEvaluate.getResultingDatatype(nameLookupTable);
		
		while(isOk && idx < cases.size()) {
			isOk = (cases.get(idx).checkSemantics(nameLookupTable) && cases.get(idx).getResultingDatatype(nameLookupTable).equals(datatype));
			idx++;
		}
		
		return isOk;
	}
	
	@Override
	public boolean generate(int indent) {
		boolean isOk = true;
		String ind = getIndent(indent);
		String innerInd = getIndent(indent + LEVEL_IND);
		int idx = 0;
		
		System.out.println();
		
		System.out.print(ind + "case ");
		isOk = expressionToEvaluate.generate(0);
		System.out.println(ind + " in");
		
		while(isOk && idx < cases.size()) {
			isOk = cases.get(idx).generateCaseBlock(indent + LEVEL_IND);
			idx++;
		}
		
		if(isOk && elseBlock != null) {
			System.out.println(innerInd + "*) ");
			isOk = elseBlock.generate(indent + LEVEL_IND + LEVEL_IND);
			System.out.println(innerInd + ";;");
		}
		
		System.out.println(ind + "esac");
		System.out.println();
		
		return isOk;
	}

}
