package upe.process;

public interface UProcessElementFactory {
	UProcessTextField newTextField(UProcessComponent parent, String name );
	UProcessDecimalField newDecimalField(UProcessComponent parent, String name );
	UProcessDateField newDateField(UProcessComponent parent, String name );
	UProcessImageField newImageField(UProcessComponent parent, String name );
	UProcessChoiceField newChoiceField(UProcessComponent parent, String name );
}
