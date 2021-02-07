package pkgNameLookup;

import java.util.HashMap;
import java.util.Stack;

/**
 * lookup-table for identifiers
 * stack with scopes
 * each scope is a hashmap id -> id-data
 * @author pupil
 *
 */
public class NameLookupTable {
	private Stack<HashMap<String, NameLookupEntry>> table;
	
	public NameLookupTable() {
		super();
		table = new Stack<>();
	}
	
	public void createScope() {
		table.push(new HashMap<>());
	}
	
	public void removeScope() {
		table.pop();
	}
	
	public boolean containsIdentifier(String identifier) {
		return (getByIdentifier(identifier) != null);
	}
	
	public HashMap<String, NameLookupEntry> getCurrScope() {
		return table.lastElement();
	}

	public void addIdentifier(String identifier, String identifierType, AccessType accessType) {
		getCurrScope().put(identifier, new NameLookupEntry(identifierType, accessType));
	}

	public NameLookupEntry getByIdentifier(String identifier) {
		NameLookupEntry foundEntry = null;
		int idx = 0;
		
		while(foundEntry == null && idx < table.size()) {
			if(table.get(idx).containsKey(identifier)) {
				foundEntry = table.get(idx).get(identifier);
			}
			
			idx++;
		}
		
		return foundEntry;
	}
}
