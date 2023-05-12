	package upe.process.messages;

import java.io.Serializable;

public interface UProcessMessage extends Serializable {
	public static final int MESSAGE_LEVEL_ERROR   = 3;
	public static final int MESSAGE_LEVEL_WARNING = 2;
	public static final int MESSAGE_LEVEL_INFO    = 1;
	public static final int MESSAGE_LEVEL_NONE    = 0;
	
	/**
	 * 
	 */
	public int getMessageLevel();
	
	/**
	 * returns a unique ID for this message.
	 * @return
	 */
	public String getMessageID();
	
	/**
	 * returns the message text.
	 * @return
	 */
	public String getMessageText();
}
