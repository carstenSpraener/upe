package upe.process.impl;

import upe.process.UProcessComponent;
import upe.process.UProcessImageField;

public class UProcessImageFieldImpl extends AbstractUProcessFieldImpl implements
		UProcessImageField {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UProcessImageFieldImpl(UProcessComponent parent, String name) {
		super(parent, name);
	}

	@Override
	public byte[] getImage() {
		return (byte[]) super.getValue();
	}

	@Override
	public void setImage( byte[] imgData) {
		super.setValue( imgData );
	}

}
