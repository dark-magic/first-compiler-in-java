package pkgParser;

import java.util.ArrayList;

import pkgNameLookup.NameLookupTable;
import pkgScanner.Scanner;
import pkgScanner.TokenType;

/**
 * Block := "{" { stmt } "}" | stmt.
 */
public class Block extends Stmt {
	private boolean disallowGuards;
	private boolean containsGuards;
	private ArrayList<Stmt> stmts;
	
	
	public Block(Scanner s) {
		super(s);
		disallowGuards = false;
		containsGuards = false;
		stmts = new ArrayList<>();
	}
	
	public Block(Scanner s, boolean disallowGuards) {
		super(s);
		this.disallowGuards = disallowGuards;
		containsGuards = false;
		stmts = new ArrayList<>();
	}

	
	@Override
	public boolean parse() {
		boolean isOk = true;
		scanner.getNextToken();
		
		if(scanner.getCurrToken().isValue("{")) {
			scanner.getNextToken();
			
			while(!scanner.getCurrToken().isValue("}") && !scanner.getCurrToken().isOfType(TokenType.EOF)) {
				isOk = (isOk && parseStatement());
				scanner.getNextToken();
			}
			
			if(!scanner.getCurrToken().isValue("}")) {
				ignoreStatement("EOF before } for statement list reached");
				isOk = false;
			}
		}
		else {
			isOk = parseStatement();
		}
		
		return isOk;
	}
	
	private boolean parseStatement() {
		boolean isOk = false;
		Stmt stmt = null;
		
		if(scanner.getCurrToken().isValue("define")) {
			stmt = new DeclarationStmt(this.scanner);
			isOk = stmt.parse();
		}
		else if(scanner.getCurrToken().isValue("let")) {
			stmt = new AssignmentStmt(this.scanner);
			isOk = stmt.parse();
		}
		else if(scanner.getCurrToken().isValue("if")) {
			stmt = new IfStmt(this.scanner);
			isOk = stmt.parse();
		}
		else if(scanner.getCurrToken().isValue("unless")) {
			stmt = new UnlessStmt(this.scanner);
			isOk = stmt.parse();
		}
		else if(scanner.getCurrToken().isValue("look_at")) {
			stmt = new SwitchStmt(this.scanner);
			isOk = stmt.parse();
		}
		else if(scanner.getCurrToken().isValue("while")) {
			stmt = new WhileStmt(this.scanner);
			isOk = stmt.parse();
		}
		else if(scanner.getCurrToken().isValue("until")) {
			stmt = new UntilStmt(this.scanner);
			isOk = stmt.parse();
		}
		else if(scanner.getCurrToken().isValue("do")) {
			stmt = new DoStmt(this.scanner);
			isOk = stmt.parse();	
		}
		else if(scanner.getCurrToken().isValue("for")) {
			stmt = new ForStmt(this.scanner);
			isOk = stmt.parse();	
		}
		else if(scanner.getCurrToken().isValue("on")) {
			stmt = new GuardStmt(this.scanner);
			isOk = stmt.parse();
			containsGuards = true;
		}
		
		if (isOk && stmt != null) {
	        stmts.add(stmt);
		}
		
		return isOk;
	}

	public boolean checkSemantics(NameLookupTable nameLookupTable) {
		boolean isOk = (!disallowGuards || !containsGuards);
		int idx = 0;
		
		nameLookupTable.createScope();
		
		while(isOk && idx < stmts.size()) {
			isOk = stmts.get(idx).checkSemantics(nameLookupTable);
			idx++;
		}
		
		nameLookupTable.removeScope();
		
		return isOk;
	}
	
	@Override
	public boolean generate(int indent) {
		boolean isOk = true;
		int idx = 0;
		
		if(indent == 0) {
			System.out.println("#!/bin/sh");
		}
		
		System.out.println(getIndent(indent) + "# statements at level " + (indent / LEVEL_IND));
		
		while(isOk && idx < stmts.size()) {
			isOk = stmts.get(idx).generate(indent);
			idx++;
		}
		
		return isOk;
	}

}
