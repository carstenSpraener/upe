import de.spraener.nxtgen.ProtectionStrategieDefaultImpl
import de.spraener.nxtgen.oom.StereotypeHelper
import de.spraener.nxtgen.oom.model.MAssociation
import de.spraener.nxtgen.oom.model.MAttribute
import de.spraener.nxtgen.oom.model.MClass
import de.spraener.nxtgen.oom.model.OOModel
import upe.profile.rest.generator.GeneratorGapTransformation
import upe.profile.rest.generator.JavaHelper
import upe.profile.rest.generator.UPEStereotypes

MClass mClass = this.getProperty("modelElement");

class UProcessComponentBaseGenerator {
    MClass mClass;
    MClass orgClass;
    OOModel model;

    UProcessComponentBaseGenerator(MClass mClass) {
        this.mClass = mClass;
        this.orgClass  = GeneratorGapTransformation.getOriginalClass(mClass);
        this.model = mClass.getModel();
    }

    String generateSubComponent(MAssociation assoc, MClass target) {
        return """    @UpeProcessComponent("${assoc.name}")
    private ${target.getFQName()} ${assoc.name};
"""
    }
    String subComponents() {
        StringBuilder sb = new StringBuilder();

        this.orgClass.associations.forEach {
            MClass target = (MClass)this.model.findClassByName(it.type);
            if( StereotypeHelper.hasStereotye(target, UPEStereotypes.UPROCESSCOMPONENT.name) ) {
                sb.append(generateSubComponent(it,target))
            }
        }
        return sb.toString()
    }

    protected String scaffoldsDefinition() {
        StringBuilder sb = new StringBuilder();
        orgClass.dependencies.forEach {
            if( StereotypeHelper.hasStereotye(it, "Scaffolds") ) {
                String target = it.getTarget();
                sb.append(
"""
@UpeScaffolds(${target}.class)
"""
                )
            }
        }
        return sb.toString();
    }

    String validations() {
        List<MAttribute> attrList = new ArrayList();
        attrList.addAll(this.mClass.attributes);
        StringBuilder sb = new StringBuilder();
        orgClass.dependencies.forEach {
            if( StereotypeHelper.hasStereotye(it, "Scaffolds") ) {
                MClass target = this.model.findClassByName(it.target);
                attrList.addAll(target.attributes);
            }
        }
        attrList.forEach {
            if( it.getProperties().get("required")!=null ) {
                sb.append(
"""        this.addValidator(new MandantoryValidator("${it.name}"));
"""
                )
            }
        }
        orgClass.operations.forEach {
            if( StereotypeHelper.hasStereotye(it, UPEStereotypes.UVALIDATOR.name) ) {
                String cName = JavaHelper.firstToUpperCase(it.name)
                sb.append("        super.addValidator(new ${orgClass.getPackage().getFQName()}.validations.${cName}());\n");
            }
        }
        return sb.toString();
    }


    String generate() {
        """//${ProtectionStrategieDefaultImpl.GENERATED_LINE}
package ${mClass.getPackage().getFQName()};

import upe.annotations.*;
import upe.process.UProcessComponent;
import upe.process.impl.UProcessComponentImpl;
import upe.process.validation.impl.MandantoryValidator;

${scaffoldsDefinition()}public class ${mClass.getName()} extends UProcessComponentImpl {
${subComponents()}
    public ${mClass.getName()}(UProcessComponent parent, String name) {
        super(parent, name);
${validations()}
    }
}
"""

    }
}

new UProcessComponentBaseGenerator(mClass).generate()
