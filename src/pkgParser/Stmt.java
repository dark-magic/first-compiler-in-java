package pkgParser;

import pkgNameLookup.NameLookupTable;
import pkgScanner.Scanner;

public abstract class Stmt {
	public final static String NEW_LINE = System.getProperty("line.separator", "\n");
	protected static int LEVEL_IND = 3;

	protected Scanner scanner;

	public Stmt(Scanner s) {
		scanner = s;
	}

	public abstract boolean parse();

	public abstract boolean checkSemantics(NameLookupTable nameLookupTable);

	public abstract boolean generate(int indent);

	protected void ignoreStatement(String reason) {
		System.out.println("   *ERR* in line " + scanner.getLineNr() + " " + reason + "', statement ignored");
	}

	protected String getIndent(int indent) {
		String indy = "";
		
		for (int i = 0; i < indent; i++) {
			indy += " ";
		}
		
		return indy;
	}
}
