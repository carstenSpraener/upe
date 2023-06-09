package upe.process;

import upe.process.messages.UProcessMessage;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

public interface UProcessEngine extends Serializable {
	public static final Logger LOGGER = Logger.getLogger(UProcessEngine.class.getName());
	public static String PROCESS_RESULT_STATE = "processResultState";
	public static String PROCESS_CANCEL       = "processCancel";
	public static String PROCESS_FINISH       = "processFinish";

    public void jumpToProcess( String processName, Map<String,Serializable> processArgs );
	
	public void callProcess( String processName, Map<String,Serializable> processArgs, UProcessAction returnAction );
	
	public Map<String,Serializable> finishProcess();
	
	public Map<String,Serializable> cancelProcess();

	/**
	 * Returns the session for this process.
	 * @return
	 */
	public UProcessSession getSession();
	
	/**
	 * present this message the next time when the engine renders the frontend 
	 * @param uProcessMessage
	 */
	public void queueProcessMessage(UProcessMessage uProcessMessage);
	
	/**
	 * gets the Locale as set in setLocale or the default locale of the system. process 
	 * elements can request the locale from here.
	 * 
	 * @return
	 */
	public Locale getLocale();
	
	/**
	 * Sets  the locale for this process engine instance. This can be done from the
	 * UProcessEngine Implementation. For example from the request header in a HTTP-Request.
	 * @param l
	 */
	public void setLocale( Locale l );

	/**
	 * Returns the currently running process. That can differ to the process a
	 * ProcessElement belongs to. So it is important in an (Return)-Action
	 * to request the actvice process and not to use this.getParent() or a cached
	 * reference to some process when the currently runing process is needed.
	 *
	 * @return the currently running process instance.
	 */
	public UProcess getActiveProcess();

    UProcess getProcessInstance(String processName);
}
