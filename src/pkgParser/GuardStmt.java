package pkgParser;

import pkgNameLookup.NameLookupTable;
import pkgParser.pkgExpressions.Expression;
import pkgScanner.Scanner;
import pkgScanner.TokenType;

/**
 * GuardStmt := "on" identifier ["at" literal] ["for" literal] [block] "check" expression.
 */
public class GuardStmt extends Stmt {
	private String identifierToCheck;
	private String startTime;
	private String duration;
	private Block stmts;
	private Expression condition;

	public GuardStmt(Scanner s) {
		super(s);
		identifierToCheck = "?";
		startTime = "00:00";
		duration = "-1";
		stmts = null;
		condition = new Expression(s);
	}

	@Override
	public boolean parse() {
		boolean isOk = (scanner.getNextToken().isOfType(TokenType.identifier));

		if(isOk) {
			identifierToCheck = scanner.getCurrToken().getValue();

			scanner.getNextToken();

			if(scanner.getCurrToken().isValue("at")) {
				isOk = (scanner.getNextToken().isOfType(TokenType.literal) && scanner.isTimeLiteral(scanner.getCurrToken().getValue()));

				if(isOk) {
					startTime = scanner.getCurrToken().getValue();
				}
			}
			else {
				scanner.ungetToken(scanner.getCurrToken());
			}

			scanner.getNextToken();

			if(isOk && scanner.getCurrToken().isValue("for")) {
				isOk = (scanner.getNextToken().isOfType(TokenType.literal) && scanner.isNumberLiteral(scanner.getCurrToken().getValue()));

				if(isOk) {
					duration = scanner.getCurrToken().getValue();
				}
			}
			else {
				scanner.ungetToken(scanner.getCurrToken());
			}

			scanner.getNextToken();

			if(isOk && !scanner.getCurrToken().isValue("check")) {
				stmts = new Block(this.scanner, true);
				scanner.ungetToken(scanner.getCurrToken());
				isOk = stmts.parse();
			}
			else {
				scanner.ungetToken(scanner.getCurrToken());
			}

			if(isOk) {
				isOk = (scanner.getNextToken().isValue("check") && condition.parse());
			}
		}

		return isOk;
	}

	@Override
	public boolean checkSemantics(NameLookupTable nameLookupTable) {
		boolean isOk = (nameLookupTable.containsIdentifier(identifierToCheck));

		// check duration
		if(isOk && !duration.equals("-1")) {
			int mins = Integer.parseInt(startTime.substring(0, 2)) * 60;
			mins += Integer.parseInt(startTime.substring(3));
			mins += Integer.parseInt(duration);

			isOk = (mins < 24 * 60);
		}

		//check no guard in block

		return (isOk && (stmts == null || stmts.checkSemantics(nameLookupTable)) && condition.checkSemantics(nameLookupTable) && condition.getResultingDatatype(nameLookupTable).equals("bool"));
	}

	@Override
	public boolean generate(int indent) {
		System.out.println("Nothing to do here!");
		return true;
	}

}
