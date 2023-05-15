import de.spraener.nxtgen.ProtectionStrategieDefaultImpl
import de.spraener.nxtgen.oom.model.MClass
import de.spraener.nxtgen.oom.model.OOModel

class ProcessActionTemplate {
    MClass mClass;

    ProcessActionTemplate(Object me) {
        this.mClass = (MClass)me;
    }

    String generate() {
        return """//${ProtectionStrategieDefaultImpl.GENERATED_LINE}
package ${mClass.getPackage().getFQName()};

import upe.process.UProcessComponent;

import java.io.Serializable;
import java.util.Map;


public class ${mClass.getName()} extends ${mClass.getName()}Base {

    public ${mClass.getName()}(UProcessComponent parent, String name) {
        super(parent, name);
    }

    public Serializable internalExecute( Map<String, Serializable> args ) {
        // TODO: Remove generator protection line and implement this method
        return null;
    }
}
"""
    }
}
new ProcessActionTemplate(this.getProperty("modelElement")).generate();
