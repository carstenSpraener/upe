package upe.process;

import upe.annotations.UpeApplication;
import upe.annotations.UpeProcess;
import upe.exception.UPERuntimeException;

import java.util.HashMap;
import java.util.Map;

public class ApplicationConfiguration {
	private Map<String, Class<? extends UProcess>> processClass = new HashMap<>();
	private static final ApplicationConfiguration myInstance = new ApplicationConfiguration();
	
	private ApplicationConfiguration() {
	}
	
	public static ApplicationConfiguration getInstance() {
		return myInstance;
	}
	
	@SuppressWarnings("unchecked")
	public void addProcessClass( String name, String processClassName ) {
		try {
			processClass.put( name, (Class<UProcess>)Class.forName(processClassName));
		} catch( ClassNotFoundException cnfExc ) {
			throw new UPERuntimeException( "class "+processClassName+" for process "+name+" not found.", cnfExc );
		}
	}

	public Class<? extends UProcess> getProcessClass(String processName) {
		return processClass.get(processName);
	}

	public void readApplication(Class<?> applilcationClass) {
		UpeApplication applConfig = applilcationClass.getAnnotation(UpeApplication.class);
		for( Class<? extends UProcess> procClazz : applConfig.value() ) {
			UpeProcess procConfig = procClazz.getAnnotation(UpeProcess.class);
			if( procConfig!=null ) {
				processClass.put( procConfig.value(), procClazz);
			}
		}
	}
}
