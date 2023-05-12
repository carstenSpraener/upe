package upe.process;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UProcessSession implements Serializable {
	private Map<String, Serializable> sessionMap = new HashMap<>();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setValue( String name, Serializable value ) {
		sessionMap.put(name, value);
	}
	
	public Serializable getValue( String name ) {
		return sessionMap.get(name);
	}
}

