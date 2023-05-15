import de.spraener.nxtgen.ProtectionStrategieDefaultImpl
import de.spraener.nxtgen.model.ModelElement
import de.spraener.nxtgen.oom.StereotypeHelper
import de.spraener.nxtgen.oom.model.MAssociation
import de.spraener.nxtgen.oom.model.MClass
import de.spraener.nxtgen.oom.model.OOModel
import upe.profile.rest.generator.UPEStereotypes

class UProcessBaseTemplate {
    MClass mClass
    MClass orgClass
    OOModel model

    UProcessBaseTemplate(ModelElement me) {
        this.mClass = (MClass)me;
        this.orgClass = upe.profile.rest.generator.GeneratorGapTransformation.getOriginalClass(this.mClass);
        this.model = orgClass.getModel()
    }

    String generateActionField(MAssociation assoc, MClass target) {
        return """    @UpeProcessAction("${assoc.name}")
    private ${target.getFQName()} ${assoc.getName()};
"""
    }

    String generateProcessComponentField( MAssociation assoc, MClass target) {
        return """    @UpeProcessComponent("${assoc.name}")
    private ${target.getFQName()} ${assoc.getName()};
"""
    }

    String peReferences() {
        System.err.println("Generating process references")
        StringBuffer sb = new StringBuffer();
        orgClass.associations.forEach {
            System.err.println("Analyzing association "+it.getName());
            MClass target = this.model.findClassByName(it.getType());
            if( StereotypeHelper.hasStereotye(target, UPEStereotypes.UPROCESSACTION.name)) {
                sb.append(generateActionField(it, target))
            }else if( StereotypeHelper.hasStereotye(target, UPEStereotypes.UPROCESSCOMPONENT.name)) {
                sb.append(generateProcessComponentField(it, target))
            } else {
                System.err.println("Unknownd target "+target.name);
            }
        }
        return sb.toString();
    }

    String generate() {
        return """//${ProtectionStrategieDefaultImpl.GENERATED_LINE}
package ${mClass.getPackage().getFQName()};

import upe.annotations.*;
import upe.process.*;
import upe.process.impl.AbstractUProcessImpl;

import java.io.Serializable;
import java.util.Map;

public abstract class ${mClass.getName()} extends AbstractUProcessImpl {
${peReferences()}
     public ${mClass.getName()}(UProcessEngine pe, String name) {
        super(pe, name);
     }

    @Override
    public Map<String, Serializable> finish() {
        return null;
    }

    @Override
    public Map<String, Serializable> cancel() {
        return null;
    }
     
}
"""
    }
}

new UProcessBaseTemplate(this.getProperty("modelElement")).generate();
