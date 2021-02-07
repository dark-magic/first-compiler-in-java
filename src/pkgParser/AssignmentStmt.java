package pkgParser;

import pkgNameLookup.AccessType;
import pkgNameLookup.NameLookupTable;
import pkgParser.pkgExpressions.Expression;
import pkgScanner.Scanner;
import pkgScanner.TokenType;

/**
 * assignmentStmt := "let" identifier "be" expression ["."].
 */
public class AssignmentStmt extends Stmt {
	private String identifier;
	private Expression expression;
	private boolean inline;

	public AssignmentStmt(Scanner s) {
		super(s);
		expression = new Expression(this.scanner);
		inline = false;
	}

	@Override
	public boolean parse() {
		return parse(false);
	}

	public boolean parse(boolean inline) {
		boolean isOk = (scanner.getNextToken().isOfType(TokenType.identifier));
		this.inline = inline;

		if (isOk) {
			identifier = scanner.getCurrToken().getValue();
			isOk = (scanner.getNextToken().isValue("be") && expression.parse());

			if (isOk && !inline) {
				isOk = (scanner.getNextToken().isValue("."));
			}
		}

		return isOk;
	}

	public boolean checkSemantics(NameLookupTable nameLookupTable) {
		boolean isOk = ((!inline && nameLookupTable.containsIdentifier(identifier)
				&& nameLookupTable.getByIdentifier(identifier).getAccess() == AccessType.readWrite
				&& expression.checkSemantics(nameLookupTable)
				&& nameLookupTable.getByIdentifier(identifier).getType()
						.equals(expression.getResultingDatatype(nameLookupTable)))
				|| inline && !nameLookupTable.containsIdentifier(identifier)
						&& expression.checkSemantics(nameLookupTable)
						&& expression.getResultingDatatype(nameLookupTable).equals("number"));

		if (isOk && inline) {
			nameLookupTable.addIdentifier(identifier, "number", AccessType.readonly);
		}

		return isOk;
	}

	@Override
	public boolean generate(int indent) {
		boolean isOk = true;
		String ind = getIndent(indent);
		
		System.out.print(ind + identifier + " = ");
		isOk = expression.generate(0);
		
		if(!inline) {
			System.out.println();
		}

		return isOk;
	}

	public String getIdentifier() {
		return identifier;
	}
}
