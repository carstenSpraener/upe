import de.spraener.nxtgen.oom.model.MAssociation
import de.spraener.nxtgen.oom.model.MAttribute
import de.spraener.nxtgen.oom.model.MClass
import upe.profile.rest.generator.MyModelHelper

import java.util.function.BiConsumer

class JavaAssociationAspect {
    MClass mClass;
    BiConsumer<MAssociation, JavaClassTarget> preAttrDefinition = null;

    JavaAssociationAspect(MClass mClass) {
        this.mClass = mClass;
    }

    JavaAssociationAspect withPreAttributeDefinition( BiConsumer<MAssociation, JavaClassTarget> preAttrDefinition ) {
        this.preAttrDefinition = preAttrDefinition;
        return this;
    }

    void apply(JavaClassTarget cTarget) {
        for (MAssociation assoc : mClass.associations) {
            if (MyModelHelper.Assoc.isToN(assoc)) {
                applyToNAssociation(assoc, cTarget);
            } else {
                applyToOneAssociation(assoc, cTarget);
            }
        }
    }

    void applyToNAssociation(MAssociation assoc, JavaClassTarget cTarget) {
        MClass targetClass = MyModelHelper.Assoc.getTargetClass(assoc);
        if( !targetClass.getPackage().getFQName().equalsIgnoreCase(mClass.getPackage().FQName)) {
            cTarget.imports.addIfNotPresent("import ${assoc.type};");
        }
        cTarget.imports.addIfNotPresent("import java.util.Set;");
        cTarget.imports.addIfNotPresent("import java.util.HashSet;");
        if( this.preAttrDefinition != null ) {
            this.preAttrDefinition.accept(assoc, cTarget);
        }
        cTarget.attributes.add("    private java.util.Set<${assoc.type}> ${assoc.name} = null;");
        String accessName = MyModelHelper.toJavaAccessorName(assoc.name);
        String name = assoc.name;
        cTarget.methods.add("""
    public Set<${assoc.type}> get${accessName}() {
        if( this.${name} == null ) {
            set${accessName}(new HashSet<>());
        }
        return this.${name};
    }
        
    public void set${accessName}( Set<${assoc.type}> value ) {
        this.${name} = value;
    }

    public void addTo${accessName}( ${assoc.type} value ) {
        this.${name}.add(value);
    }

    public void removeFrom${accessName}( ${assoc.type} value ) {
        this.${name}.remove(value);
    }

    public boolean ${name}Contains( ${assoc.type} value ) {
        return this.${name}.contains(value);
    }
""")
    }

    void applyToOneAssociation(MAssociation assoc, JavaClassTarget cTarget) {
        cTarget.imports.addIfNotPresent("import ${assoc.type};");
        if( this.preAttrDefinition != null ) {
            this.preAttrDefinition.accept(assoc, cTarget);
        }
        cTarget.attributes.add("    private ${assoc.type} ${assoc.name} = null;");
        String accessName = MyModelHelper.toJavaAccessorName(assoc.name);
        cTarget.methods.add("""
    public void set${accessName}( ${assoc.type} value ) {
        this.${assoc.name} = value;
    }

    public  ${assoc.type} get${accessName}() {
        return this.${assoc.name};
    }

""");
    }
}
