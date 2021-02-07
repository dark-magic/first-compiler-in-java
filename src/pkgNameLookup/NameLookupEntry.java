package pkgNameLookup;

/**
 * datatype and accesstype of an identifier
 * @author pupil
 *
 */
public class NameLookupEntry {
	private String type;
	private AccessType access;
	
	public NameLookupEntry(String type, AccessType access) {
		super();
		setIdentifierType(type);
		setAccessType(access);
	}

	public String getType() {
		return type;
	}

	private void setIdentifierType(String type) {
		this.type = type;
	}

	public AccessType getAccess() {
		return access;
	}

	private void setAccessType(AccessType access) {
		this.access = access;
	}
	
	
}
