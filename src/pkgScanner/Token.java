package pkgScanner;

/**
 * Scanner token
 * value and type
 * @author pupil
 */
public class Token {
	private String value;
	private TokenType tokenType;
	
	
	public Token(String value, TokenType tokenType) {
		super();
		setValue(value);
		setTokenType(tokenType);
	}
	
	public Token(Token tokenToClone) {
		super();
		setValue(tokenToClone.getValue());
		setTokenType(tokenToClone.getTokenType());
	}
	
	
	public boolean isValue(String value) {
		return (this.value.equalsIgnoreCase(value));
	}
	
	public boolean isOfType(TokenType tokenType) {
		return (this.tokenType == tokenType);
	}
	
	public String getValue() {
		return this.value;
	}
	
	private void setValue(String newValue) {
		this.value = newValue;
	}
	
	public TokenType getTokenType() {
		return this.tokenType;
	}
	
	private void setTokenType(TokenType newTokenType) {
		this.tokenType = newTokenType;
	}
}
