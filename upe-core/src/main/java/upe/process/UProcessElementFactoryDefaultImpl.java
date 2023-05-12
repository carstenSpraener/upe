package upe.process;

import upe.process.impl.*;

public class UProcessElementFactoryDefaultImpl implements UProcessElementFactory {

	@Override
	public UProcessDecimalField newDecimalField(UProcessComponent parent,
												String name) {
		return new UProcessDecimalFieldImpl(parent,name);
	}

	@Override
	public UProcessDateField newDateField(UProcessComponent parent, String name) {
		return new UProcessDateFieldImpl(parent, name);
	}

	@Override
	public UProcessImageField newImageField(UProcessComponent parent, String name) {
		return new UProcessImageFieldImpl(parent, name);
	}

	@Override
	public UProcessChoiceField newChoiceField(UProcessComponent parent, String name) {
		return new UProcessChoiceFieldImpl(parent, name);
	}

	@Override
	public UProcessTextField newTextField(UProcessComponent parent, String name) {
		return new UProcessTextFieldImpl(parent, name);
	}

}
