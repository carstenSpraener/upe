import de.spraener.nxtgen.oom.model.MAttribute
import de.spraener.nxtgen.oom.model.MClass
import upe.profile.rest.generator.MyModelHelper

import java.util.function.BiConsumer

class JavaAttributeAspect {
    MClass clazz;
    BiConsumer<MAttribute, JavaClassTarget> preAttrDefinition = null;

    List<String> javaBaseTypes = List.of("int", "Integer", "byte", "Byte", "char", "Character",
            "long", "Long", "boolean", "Boolean", "String", "double", "Double", "float", "Float"
    )

    JavaAttributeAspect(MClass clazz) {
        this.clazz = clazz
    }

    JavaAttributeAspect withPreAttributeDefinition( BiConsumer<MAttribute, JavaClassTarget> preAttrDefinition ) {
        this.preAttrDefinition = preAttrDefinition;
        return this;
    }

    void apply(JavaClassTarget target) {
        for (MAttribute attr : clazz.getAttributes()) {
            applyAttribute(attr, target);
        }
    }

    JavaAttributeAspect applyAttribute(MAttribute attr, JavaClassTarget classTarget) {
        if (!javaBaseTypes.contains(attr.type)) {
            String importLine = "import ${attr.type};"
            classTarget.imports.addIfNotPresent(importLine);
        }
        if( this.preAttrDefinition != null ) {
            this.preAttrDefinition.accept(attr, classTarget)
        }
        classTarget.attributes.add("    private ${attr.type} ${attr.name};")
        generateAccessMethods(attr, classTarget)
    }

    void generateAccessMethods(MAttribute attr, JavaClassTarget classTarget) {
        String type = attr.type;
        String name = attr.name;
        String accessName = MyModelHelper.toJavaAccessorName(name);

        String code = """
    public void set${accessName}( ${type} value ) {
        this.${name} = value;
    }
    
    public ${type} get${accessName}() {
        return ${name};
    }
"""
        classTarget.methods.add(code);
    }
}
