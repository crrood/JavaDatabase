import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

///////////////////////////////////////////////////////////////////////
// HashMap/Stack based DataBase design for ThumbTack jobs challenge
//
// By Colin Rood
// crrood@gmail.com
// 510-851-1930
///////////////////////////////////////////////////////////////////////

// Each session's changes are stored in a HashMap
// Simultaneous sessions are stored in a LIFO stack represented by a Vector

public class DataBase {
	// CLASS CONSTANTS
	// Maximum number of open variables
	static int MAX_VARS = 10;
	// Maximum number of open sessions
	static int MAX_SESSIONS = 5;
	
	// CLASS VARIABLES
	// Initialize the base session
	private HashMap<String, String> root = new HashMap<String, String>(MAX_VARS);
	
	// Initialize the session stack
	private Vector<HashMap<String, String>> sessionStack = new Vector<HashMap<String, String>>(MAX_SESSIONS, 1);
	
	// Initialize the reverse HashMap to facilitate numEqualTo operations
	private HashMap<String, Integer> valueMap = new HashMap<String, Integer>(MAX_VARS);
	
	// List of keys
	private HashSet<String> keys = new HashSet<String>(MAX_VARS);
	
	// CLASS METHODS
	// Constructor
	public DataBase(){
		// Add root session to stack
		sessionStack.add(root);
	}
	
	// Method to set variable value
	// (in most recently opened session)
	
	// Runs in constant time
	
	public void set(String key, String value){
		// Update keys
		if(!keys.contains(key)){
			keys.add(key);
		}
		
		// Update valueMap
		// Decrement numEqualTo count for old value, if necessary
		String oldValue = get(key);
		if(oldValue.compareTo("NULL") != 0){
			valueMap.put(oldValue, valueMap.get(oldValue) - 1);
		}
		// Increment numEqualTo count for new value
		if(valueMap.containsKey(value)){
			valueMap.put(value,  valueMap.get(value) + 1);
		}
		else{
			valueMap.put(value, 1);
		}
		
		// Set variable value in topmost session
		sessionStack.lastElement().put(key, value);
	}
	
	// Method to get variable value
	// (from session where it was last modified)
	
	// Runs in constant time
	
	public String get(String key){
		// Iterate through session stack and return value of first non-null key
		int i = sessionStack.size() - 1;
		while(i >= 0){
			if(sessionStack.get(i).containsKey(key)){
				return (String) sessionStack.get(i).get(key);
			}
			i--;
		}
		
		// If not found, return NULL
		return "NULL";
	}
	
	// Method to unset variable value
	// (in most recently opened session)
	
	// Runs in constant time
	
	public void unset(String key){
		// Update numEqualTo count
		String oldValue = get(key);
		if(oldValue.compareTo("NULL") != 0){
			valueMap.put(oldValue, valueMap.get(oldValue) - 1);
		}
		
		// If session is bottom-most on stack (no rollback possible) remove key outright
		if(sessionStack.size() == 1){
			sessionStack.get(0).remove(key);
			keys.remove(key);
		}
		// Otherwise insert a NULL value as a placeholder
		// to keep subsequent GET operations from returning values from previous sessions
		else{
			sessionStack.get(sessionStack.size() - 1).put(key, "NULL");
		}
	}
	
	// Finds number of variables equal to some value
	// (using most up to date values)
	
	// Runs in constant time
	
	public int numEqualTo(String value){
		// Check valueMap (a HashMap containing value -> # of instances key-value pairs)
		if(valueMap.containsKey(value)){
			return valueMap.get(value);
		}
		return 0;
	}
	
	// Begin new session
	
	// Runs in constant time
	
	public void begin(){
		// If max number of sessions already open, report error
		if(sessionStack.size() >= MAX_SESSIONS){
			System.out.println(MAX_SESSIONS + " sessions are already open");
			return;
		}
		
		// Otherwise add new HashMap to top of sessionStack
		sessionStack.add(new HashMap<String, String>(MAX_VARS));
	}
	
	// Rollback to previous session
	
	// Runs in O(n) time, where n is the number of keys in the database
	
	public void rollback(){
		// If session is root, output error message
		if(sessionStack.size() <= 1){
			System.out.println("INVALID ROLLBACK");
			return;
		}
		
		// Otherwise remove topmost session from stack
		sessionStack.remove(sessionStack.size() - 1);
		
		// Update numEqualTo count
		valueMap.clear();
		String value;
		// Iterate over all keys
		for(String key : keys){
			value = get(key);
			// Drop unset keys
			if(value.compareTo("NULL") != 0){
				// Increment count
				if(valueMap.containsKey(value)){
					valueMap.put(value, valueMap.get(value) + 1);
				}
				// Or initialize to 1
				else{
					valueMap.put(value, 1);
				}
			}
		}
	}
	
	// Commit current session
	
	// Runs in O(n) time, where n is the number of keys in the database
	
	public void commit(){
		// If session is root, do nothing
		if(sessionStack.size() == 1){
			return;
		}
		
		// Otherwise flatten the stack into a new HashMap and assign that as root
		HashMap<String, String> newRoot = new HashMap<String, String>(MAX_VARS);
		for(String key : keys){
			// Don't copy over unset keys
			if(get(key).compareTo("NULL") != 0){
				newRoot.put(key, get(key));
			}
		}
		root = newRoot;
		sessionStack.clear();
		sessionStack.add(root);
	}

}
