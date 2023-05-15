import de.spraener.nxtgen.ProtectionStrategieDefaultImpl
import de.spraener.nxtgen.oom.model.MClass
import de.spraener.nxtgen.oom.model.OOModel

MClass mClass = this.getProperty("modelElement");
OOModel model = mClass.getModel();


"""//${ProtectionStrategieDefaultImpl.GENERATED_LINE}
package ${mClass.getPackage().getFQName()};

import upe.annotations.*;
import upe.process.UProcessComponent;
import upe.process.impl.UProcessComponentImpl;

public class ${mClass.getName()} extends ${mClass.getName()}Base {

    public ${mClass.getName()}(UProcessComponent parent, String name) {
        super(parent, name);
    }
}
"""
