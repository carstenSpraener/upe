package upe.process.engine;

import upe.process.UProcess;
import upe.process.UProcessAction;
import upe.process.UProcessEngine;
import upe.process.UProcessSession;
import upe.process.messages.UProcessMessage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.logging.Logger;

@SuppressWarnings("java:S1149")
public class BaseUProcessEngine implements UProcessEngine, Serializable {
	private static final Logger LOGGER = Logger.getLogger(BaseUProcessEngine.class.getTypeName());
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Stack<ActiveUProcessInfo> processStack = new Stack<>();
	private final UProcessCmdQueue cmdQueue     = new UProcessCmdQueue();
	private UProcessSession session      = new UProcessSession();
	private Locale             processLocale = Locale.getDefault();
	private Consumer<UProcessMessage> queuedMessageConsumer = null;

	protected AbstractUUProcessCmd getCallProcessCmd() {
		return new CallUProcessCmd();
	}

	public void queueProcessMessage( UProcessMessage msg ) {
		if( this.queuedMessageConsumer != null ) {
			this.queuedMessageConsumer.accept(msg);
		}
		LOGGER.info( msg.getMessageText() );
	}

	public BaseUProcessEngine withQueuedMessageConsumer(Consumer<UProcessMessage> queuedMessageConsumer) {
		this.queuedMessageConsumer = queuedMessageConsumer;
		return this;
	}

	protected AbstractUUProcessCmd getJump2ProcessCmd() {
		return new Jump2UProcessCmd();
	}

	protected void setupProcessCmd(AbstractUUProcessCmd cmd,
								   String processName, Map<String, Serializable> processArgs,
								   UProcessAction returnAction) {
		cmd.setProcessName( processName );
		cmd.setProcessArgs(  processArgs );
		cmd.setReturnAction( returnAction );
		cmd.setProcessEngine(this);
		if( processStack.isEmpty() ) {
			cmd.setCallingProcess(null);
		} else {
			cmd.setCallingProcess(processStack.peek().getProcess());
		}
	}

	@Override
	public void callProcess(String processName,
			Map<String, Serializable> processArgs, UProcessAction returnAction) {
		AbstractUUProcessCmd cmd = getCallProcessCmd();
		setupProcessCmd(cmd, processName, processArgs, returnAction);
		cmdQueue.appendCmd(cmd);
		if( !cmdQueue.isProcessing() ) {
			cmdQueue.run();
		}
	}

	@Override
	public Map<String, Serializable> cancelProcess() {
		ActiveUProcessInfo pc = popProcess();
		Map<String,Serializable> result = pc.getProcess().cancel();
		result.put( PROCESS_RESULT_STATE, PROCESS_CANCEL);
		pc.getReturnAction().execute(result);
		return result;
	}

	@Override
	public Map<String, Serializable> finishProcess() {
		ActiveUProcessInfo pc = peekProcess();
		Map<String,Serializable> result = pc.getProcess().finish();
		if( result==null ) {
			result = new HashMap<>();
		}
		result.put( PROCESS_RESULT_STATE, PROCESS_FINISH );
		popProcess();
		if( pc.getReturnAction() != null ) {
			pc.getReturnAction().execute(result);
		}
		return result;
	}

	@Override
	public UProcessSession getSession() {
		return session;
	}

	@Override
	public void jumpToProcess(String processName,
			Map<String, Serializable> processArgs) {
		AbstractUUProcessCmd cmd = getJump2ProcessCmd();
		setupProcessCmd(cmd, processName, processArgs, null);
		cmdQueue.appendCmd(cmd);
		if( !cmdQueue.isProcessing() ) {
			cmdQueue.run();
		}
	}

	void pushProcess(UProcess p, UProcessAction returnAction, UProcess callingUProcess) {
		processStack.push( createActiveProcessInfo(p,returnAction, callingUProcess) );
	}

	protected ActiveUProcessInfo createActiveProcessInfo(UProcess p, UProcessAction returnAction,
														 UProcess callingUProcess) {
		return new ActiveUProcessInfo(p,returnAction, callingUProcess);
	}

	ActiveUProcessInfo popProcess() {
		return processStack.pop();
	}
	
	ActiveUProcessInfo peekProcess() {
		return processStack.peek();
	}
	
	public ActiveUProcessInfo getActiveProcessInfo() {
		return processStack.peek();
	}
	
	public boolean hasActiveProcess() {
		return !processStack.isEmpty();
	}

	@Override
	public Locale getLocale() {
		return processLocale;
	}

	@Override
	public void setLocale(Locale l) {
		processLocale = l;
	}

	@Override
	public UProcess getActiveProcess() {
		return this.getActiveProcessInfo().getProcess();
	}
}
