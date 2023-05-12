package upe.process;

import upe.exception.UPERuntimeException;

public class UProcessElementSystem {

	private UProcessElementSystem() {}
	/**
	 * This property defines a class that implements the interface UProcessElementFactory.
	 * The implementation is used to create new process elements. The default implementation
	 * is included in the UPE-Package.
	 * 
	 * @see UProcessElementFactory
	 */
	private static final String UPE_ELEMENT_FACTORY_CLASS = "upe.elementfactory.class";

	/**
	 * Gives a reference to a process element factory implementation. On error, it creates a UPE-RuntimeException.
	 * This is for example if the implementation is not found, or it could not be created for some reason. The reason
	 * is included in the UPERuntimeException.
	 * 
	 * @return instance of {@link UProcessElementFactory}
	 */
	public static UProcessElementFactory getProcessElementFactory() {
		String className = System.getProperty(UPE_ELEMENT_FACTORY_CLASS, UProcessElementFactoryDefaultImpl.class.getName() );
		try {
			return (UProcessElementFactory)Class.forName(className).getConstructor().newInstance();
		} catch( Exception e ) {
			throw new UPERuntimeException( "FATAL: process element factory is not correctly defined. See system property "+UPE_ELEMENT_FACTORY_CLASS, e );
		}
	}
}
