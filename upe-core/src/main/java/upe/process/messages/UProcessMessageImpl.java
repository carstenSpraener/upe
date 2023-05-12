package upe.process.messages;

public class UProcessMessageImpl implements UProcessMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String messageID = null;
	private String messageText = null;
	private int messageLevel = MESSAGE_LEVEL_INFO;
	
	public UProcessMessageImpl(String msgID, String msgText, int msgLevel ) {
		this.messageID = msgID;
		this.messageLevel = msgLevel;
		this.messageText = msgText;
	}
	
	@Override
	public String getMessageID() {
		return messageID;
	}

	@Override
	public int getMessageLevel() {
		return messageLevel;
	}

	@Override
	public String getMessageText() {
		return messageText;
	}

	@Override
	public boolean equals( Object obj ) {
		if( obj == null ) return false;
		if( !(obj instanceof UProcessMessage) ) {
			return false;
		}
		return getMessageID().equals( ((UProcessMessage)obj).getMessageID() );
	}
	
	@Override
	public int hashCode() {
		return getMessageID().hashCode();
	}
}
