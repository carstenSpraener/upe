import de.spraener.nxtgen.ProtectionStrategieDefaultImpl
import de.spraener.nxtgen.oom.model.MClass
import de.spraener.nxtgen.oom.model.OOModel
import upe.profile.rest.generator.JavaHelper

class UProcessTemplate {
    MClass mClass;
    OOModel model

    public UProcessTemplate(MClass me) {
        this.mClass = me;
        this.model = this.mClass.getModel();
    }

    String generate() {
        return """//${ProtectionStrategieDefaultImpl.GENERATED_LINE}
package ${mClass.getPackage().getFQName()};

import upe.annotations.*;
import upe.process.*;

import java.io.Serializable;
import java.util.Map;

@UpeProcess("${JavaHelper.firstToLowerCase(mClass.getName())}")
public class ${mClass.getName()} extends ${mClass.getName()}Base {
    public static final String NAME = "${JavaHelper.firstToLowerCase(mClass.getName())}";
 
    public ${mClass.getName()}(UProcessEngine pe, String name) {
       super(pe, name);
    }
     
    @Override
    public void initialize(Map<String, Serializable> args) {
        super.initialize(args);
        try(UProcessModification mod = new UProcessModification(this)) {
        }
    }
}
"""
    }
}

return new UProcessTemplate((MClass)this.getProperty("modelElement")).generate()
