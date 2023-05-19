package upe.process.messages;

import java.util.HashMap;

public class UProcessMessageStorage {
	private static UProcessMessageStorage myImpl = new UProcessMessageStorage();
	private HashMap<String, UProcessMessage> messageMap = new HashMap<>();
	
	public static UProcessMessageStorage getInstance() {
		return myImpl;
	}
	
	private UProcessMessageStorage() {
	}
	
	public void storeMessage( UProcessMessage pm ) {
		messageMap.put(pm.getMessageID(), pm);
	}
	
	public UProcessMessage getMessage(String messageID ) {
		UProcessMessage msg = messageMap.get(messageID);
		if( msg==null ) {
			msg = new UProcessMessageImpl( messageID, "No message width id '"+messageID+"' defined.", UProcessMessage.MESSAGE_LEVEL_ERROR );
		}
		return msg;
	}
	
	public boolean hasMessage( String messageID ) {
		return messageMap.containsKey(messageID);
	}
}
