package pkgScanner;

import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.PushbackReader;
import java.util.Arrays;

/**
 * token := { keyword | datatype | arithmetic_op | logical_op | comparing_op | bracket | identifier | literal | "." }.
 * keyword := "define" | "as_a" | "let" | "be" | "look_at" | "if_is" | "if" | "unless" | "else" | "do" | "while" | "until" | "for" | "to" | "by".
 * datatype := "bool" | "number" | "string".
 * arithmetic_op := "plus" | "minus" | "times" | "divided_by" | "to_the_power_of" | "modulo".
 * logical_op := "and" | "or" | "not".
 * comparing_op := "is" | "is_not" | "is_or_is_less_than" | "is_or_is_more_than" | "is_less_than" | "is_more_than".
 * bracket := "{" | "(" | ")" | "}".
 * identifier := (? alpha char ? | "_") { ? alphanum char ? | "_" }.
 * literal := (["+" | "-"] ? num char ? {? num char ?} ["." ? num char ? {? num char ?}]) | ('"' ? any char ? '"') | ("true" | "false") | time.
 */
public class Scanner {
	private static char NEW_LINE = System.getProperty("line.separator", "\n").charAt(0);
	private static char EOF = '\0';
	
	private static String[] DELIMITERS = { "{", "(", ".", ")", "}" };
	
	private static String[] KEYWORDS = { "define", "as_a", "let", "be", "look_at", "if_is", "if", "unless", "else", "do", "while", "until", "for", "to", "by" };
	private static String[] DATATYPES = { "bool", "number", "string" };
	private static String[] ARITHMETIC_OPS = { "plus", "minus", "times", "divided_by", "to_the_power_of", "modulo" };
	private static String[] LOGICAL_OPS = { "and", "or", "not" };
	private static String[] COMPARING_OPS = { "is", "is_not", "is_or_is_less_than", "is_or_is_more_than", "is_less_than", "is_more_than" };
	private static String[] BRACKETS = { "{", "(", ")", "}" };

	private PushbackReader rd;
	private int lineNr;
	private Token currToken;
	private Token ungottenToken;

	
	public Scanner(String filename) throws java.io.FileNotFoundException {
		rd = new PushbackReader(new FileReader(new File(filename)));
		lineNr = 1;
	}

	/**
	 * gets the next token
	 */
	public Token getNextToken() {
		if (ungottenToken != null) {
			currToken = ungottenToken;
			ungottenToken = null;
			
			System.out.println("Scanner re-read: '" + currToken.getValue() + "' of type: " + currToken.getTokenType());
		} 
		else if (currToken == null || currToken.getTokenType() != TokenType.EOF) {
			try {
				String tokenValue = readToken();
				TokenType tokenType = getTokenType(tokenValue);

				currToken = new Token(tokenValue, tokenType);
			} catch (EOFException ex) {
				currToken = new Token("", TokenType.EOF);
			}
			
			System.out.println("Scanner read:    '" + currToken.getValue() + "' of type: " + currToken.getTokenType());
		}

		return new Token(currToken);
	}

	/**
	 * reads a token from the file
	 * @return the token-value
	 * @throws EOFException
	 */
	private String readToken() throws EOFException {
		String token = "";
		char currChar;

		ignoreTillTokenstart();

		currChar = readChar();
		
		if(isDelimiter(currChar)) {
			token += currChar;
		}
		else {
			do {
				token += currChar;
				currChar = readChar();
			}
			while(!isDelimiter(currChar));
			
			unreadChar(currChar);
		}

		return token;
	}
	
	/**
	 * skips whitespace and comments
	 * @throws EOFException
	 */
	private void ignoreTillTokenstart() throws EOFException {
		char currChar;
		boolean commentRead;
		
		do {
			commentRead = false;
			currChar = readChar();
			
			if(currChar == EOF) {
				throw new EOFException();
			}
			else if(currChar == NEW_LINE) {
				lineNr++;
			}
			else if(currChar == '/') {
				char nextChar = readChar();
				
				if(nextChar == '/') {
					
					while(currChar != NEW_LINE) {
						currChar = readChar();
						
						if(currChar == EOF) {
							throw new EOFException();
						}
					}
					
					lineNr++;
					commentRead = true;
				}
				else if(nextChar == '*') {
					boolean possibleEndOfComment;
					
					do {
						possibleEndOfComment = false;
						
						if(currChar == '*') {
							possibleEndOfComment = true;
						}
						
						currChar = readChar();
						
						if(currChar == EOF) {
							throw new EOFException();
						}
						else if(currChar == NEW_LINE) {
							lineNr++;
						}
					}
					while(!possibleEndOfComment && currChar != '/');
					
					commentRead = true;
				}
				else {
					unreadChar(nextChar);
				}
			}
		}
		while(Character.isWhitespace(currChar) || commentRead);
		
		unreadChar(currChar);
	}
	
	private boolean isDelimiter(char c) {
		return (Character.isWhitespace(c) || c == EOF || Arrays.asList(DELIMITERS).contains("" + c));
	}

	private char readChar() {
		int c = -1;
		char ch = EOF;

		try {
			if ((rd != null) && ((c = rd.read()) != -1))
				ch = (char) c;
		} catch (java.io.IOException e) {
			rd = null;
		}

		return ch;
	}

	private boolean unreadChar(char ch) {
		boolean ok = (rd != null);
		if (ok) {
			try {
				rd.unread(ch);
			} catch (java.io.IOException e) {
				rd = null;
				ok = false;
			}
		}
		return ok;
	}

	/**
	 * finds the matching tokentype for the given token-value
	 * @param token
	 * @return token-type of the given token-value
	 */
	private TokenType getTokenType(String token) {
		TokenType type = TokenType.invalid;
		
		if(Arrays.asList(KEYWORDS).contains(token.toLowerCase())) {
			type = TokenType.keyword;
		}
		else if(Arrays.asList(DATATYPES).contains(token)) {
			type = TokenType.datatype;
		}
		else if(Arrays.asList(ARITHMETIC_OPS).contains(token)) {
			type = TokenType.arithmeticOp;
		}
		else if(Arrays.asList(LOGICAL_OPS).contains(token)) {
			type = TokenType.logicalOp;
		}
		else if(Arrays.asList(COMPARING_OPS).contains(token)) {
			type = TokenType.comparingOp;
		}
		else if(Arrays.asList(BRACKETS).contains(token)) {
			type = TokenType.bracket;
		}
		else if(token.equals(".")) {
			type = TokenType.other;
		}
		else if(isNumberLiteral(token) || isBooleanLiteral(token) || isStringLiteral(token) || isTimeLiteral(token)) {
			type = TokenType.literal;
		}
		else if(isIdentifier(token)) {
			type = TokenType.identifier;
		}
		else if(token.equals("")) {
			type = TokenType.EOF;
		}
		
		return type;
	}
	
	private boolean isIdentifier(String token) {
		return token.matches("[a-zA-Z_][a-zA-Z0-9_]*");
	}
	
	/**
	 * checks if the given token-value is a numerical literal
	 * @param token
	 */
	public boolean isNumberLiteral(String token) {
		return token.matches("([-]|[+])?[0-9][0-9]*([.][0-9][0-9]*)?");
	}
	
	/**
	 * checks if the given token-value is a boolean literal
	 * @param token
	 */
	public boolean isBooleanLiteral(String token) {
		return (token.equalsIgnoreCase("true") || token.equalsIgnoreCase("false"));
	}
	
	/**
	 * checks if the given token-value is a string literal
	 * @param token
	 */
	public boolean isStringLiteral(String token) {
		return token.matches("[\"].*[\"]");
	}
	
	/**
	 * checks if the given token-value is a time literal
	 * @param token
	 */
	public boolean isTimeLiteral(String token) {
		return token.matches("([0-1][0-9]|2[0-4]):[0-5][0-9]");
	}
	
	/**
	 * ungets the given token
	 * @param tokenToUnget
	 */
	public boolean ungetToken(Token tokenToUnget) {
		boolean isOk = (ungottenToken == null);

		if (isOk) {
			ungottenToken = tokenToUnget;
		}

		return isOk;
	}

	public int getLineNr() {
		return lineNr;
	}

	public Token getCurrToken() {
		return (currToken == null) ? null : new Token(currToken);
	}
}
