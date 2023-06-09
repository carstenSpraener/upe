package upe.process;


import java.util.Map;

public interface UProcess extends UProcessComponent {
	
	// ******************** Lifecycle methods *******************************
	/**
	 * Called from the process engine after the process is instantiated or
	 * reassigned to a new session. This method has to set the process into
	 * a defined initial state. Remember: The constructor is only called
	 * once and the process can be used for many sessions. That means, the
	 * settings of default values has to be done HERE. 
	 */
	public void initialize( Map<String,Object> args );
	
	/**
	 * Someone wants to finish this process. You can not veto this finish. The 
	 * Method should return result values in the map. This map is given to
	 * the calling process.
	 * 
	 * @return A map with result values, that is given to the calling process.
	 */
	public Map<String,Object> finish();
	/**
	 * Someone wants to cancel this process. You can not veto this cancel. The 
	 * Method should return result values in the map. This map is given to
	 * the calling process.
	 * 
	 * @return A map with result values, that is given to the calling process.
	 */
	public Map<String,Object> cancel();
	
	// ************ User Transaction Methods ********************************
	/**
	 * Called from the process engine before any input is set to the process.
	 * The input can modify one or many fields.
	 */
	public void inputStarts();
	
	/**
	 * Called from the process engine after input is set to the process.
	 * The input can modify one or many fields. 
	 */
	public void inputStops();
	
	// ************ Information Methods ********************************
	/**
	 * Return the instance of the process engine this process in running 
	 * within.
	 */
	public UProcessEngine getProcessEngine();
	
	/**
	 * returns a simple name of the process. This is used for example to 
	 * map UIs to this process.
	 * @return
	 */
	public String getName();

	/**
	 * check if any field of this process has a message with level error
	 * @return
	 */
	public boolean hasErrorMessage();

}

