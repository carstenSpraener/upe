import de.spraener.nxtgen.ProtectionStrategieDefaultImpl
import de.spraener.nxtgen.oom.StereotypeHelper
import de.spraener.nxtgen.oom.model.MAssociation
import de.spraener.nxtgen.oom.model.MAttribute
import de.spraener.nxtgen.oom.model.MClass
import de.spraener.nxtgen.oom.model.OOModel
import upe.profile.rest.generator.GeneratorGapTransformation
import upe.profile.rest.generator.JavaHelper
import upe.profile.rest.generator.MyModelHelper
import upe.profile.rest.generator.UPEStereotypes

MClass mClass = this.getProperty("modelElement");

class UProcessComponentBaseGenerator {
    MClass mClass;
    MClass orgClass;
    OOModel model;
    UProcessBaseTemplate pbTemplate;

    UProcessComponentBaseGenerator(MClass mClass) {
        this.mClass = mClass;
        this.orgClass  = GeneratorGapTransformation.getOriginalClass(mClass);
        this.model = mClass.getModel();
        this.pbTemplate = new UProcessBaseTemplate(mClass)
    }

    String generateSubComponent(MAssociation assoc, MClass target) {
        return """    @UpeProcessComponent("${assoc.name}")
    private ${target.getFQName()} ${assoc.name};
"""
    }

    String generate() {
        """//${ProtectionStrategieDefaultImpl.GENERATED_LINE}
package ${mClass.getPackage().getFQName()};

import upe.annotations.*;
import upe.common.MasterProcessComponent;
import upe.process.*;
import upe.process.impl.UProcessComponentImpl;
import upe.process.validation.impl.MandantoryValidator;

${this.pbTemplate.scaffoldsDefinition()}public class ${mClass.getName()} extends UProcessComponentImpl {
${this.pbTemplate.peFields()}${this.pbTemplate.peReferences()}
    public ${mClass.getName()}(UProcessComponent parent, String name) {
        super(parent, name);
${this.pbTemplate.validations()}
    }
${this.pbTemplate.generateFieldAccessMethods()}
${this.pbTemplate.generateSubComponentsGetter()}
${this.pbTemplate.generateActionGetters()}
}
"""

    }
}

new UProcessComponentBaseGenerator(mClass).generate()
