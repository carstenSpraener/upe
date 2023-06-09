package upe.process;


import java.util.HashMap;
import java.util.Map;

public class UProcessSession {
	private Map<String, Object> sessionMap = new HashMap<>();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setValue( String name, Object value ) {
		sessionMap.put(name, value);
	}
	
	public Object getValue( String name ) {
		return sessionMap.get(name);
	}
}

