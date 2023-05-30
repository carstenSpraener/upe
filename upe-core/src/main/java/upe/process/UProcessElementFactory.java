package upe.process;

public interface UProcessElementFactory {
	UProcessTextField newTextField(UProcessComponent parent, String name );
	UProcessDecimalField newDecimalField(UProcessComponent parent, String name );
	UProcessDateField newDateField(UProcessComponent parent, String name );
	UProcessImageField newImageField(UProcessComponent parent, String name );
	UProcessChoiceField newChoiceField(UProcessComponent parent, String name );
    UProcessBooleanField newBooleanField(UProcessComponent parent, String peName);
	UProcessComponentList newProcessComponentList(UProcessComponent parent, String peName, Class<? extends UProcessComponent> listElementClazz);
}
