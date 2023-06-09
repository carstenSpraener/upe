package upe.process;

import upe.process.messages.UProcessMessage;

import java.util.List;

public interface UProcessElement {
	
	UProcess getProcess();

	String getName();

	/** 
	 * Each element is located in a unique path. This method 
	 * returns the path of the process element.
	 * @return
	 */
	String getElementPath();
	 
	/**
	 * set this process element to visible or not visible.
	 * @param value
	 */
	void setVisible( Boolean value );
	
	/**
	 * Returns true, if this process element should be visible
	 * on the front end.
	 * @return
	 */
	Boolean isVisible();
	
	/**
	 * returns the highest message level. 
	 * @return
	 */
	int getMaximumMessageLevel();
	
	/**
	 * returns a list of all process messages on this element.
	 * @return
	 */
	List<UProcessMessage> getMessages();
	
	/**
	 * Add the process message to the process element. 
	 * @param msg
	 */
	void addProcessMessage( UProcessMessage msg );
	
	/**
	 * remove a message from the element.
	 * @param msg
	 */
	void removeProcessMessage( UProcessMessage msg );

	/**
	 * Returns true if the value changed within the actual input transaction.
	 * After the call to inputStops on the elements process this methods
	 * determines if the value of the field was changed. The method is used
	 * by the GuiUpdateStrategie do determine if a re rendering is necessary.
	 * 
	 * @return
	 */
	boolean needsRendering();

	/**
	 * The UI has renderd the state of this process element for this moment. So 
	 * no more UI updates are necessary until the process element is re changed.
	 * @param b
	 */
	void setNeedsRendering(Boolean b);

	boolean isEnabled();

	void setEnabled( Boolean value );
	
	/**
	 * Adds a listener to this process element. The element will call the listener
	 * on every change.
	 * 
	 * @param pel an instance of UProcessElementListener
	 */
	void addProcessElementListener( UProcessElementListener pel );
	
	/**
	 * Remove the UProcessElementListener from this process element.
	 *  
	 * @param pel an instance of UProcessElementListener
	 */
	void removeProcessElementListener( UProcessElementListener pel );

	/**
	 * Returns if the process element wos changed since the given timestamp.
	 *
	 * @param timeStamp
	 * @return
	 */
	public boolean modifiedSince(long timeStamp);

	/**
	 * Resets the last modification time stamp so the element looks untouched.
	 */
	public void resetModificationTracking();
}
