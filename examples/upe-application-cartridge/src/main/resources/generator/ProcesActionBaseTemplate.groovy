import de.spraener.nxtgen.ProtectionStrategieDefaultImpl
import de.spraener.nxtgen.oom.model.MClass
import de.spraener.nxtgen.oom.model.OOModel

MClass mClass = this.getProperty("modelElement");
OOModel model = mClass.getModel();


"""//${ProtectionStrategieDefaultImpl.GENERATED_LINE}
package ${mClass.getPackage().getFQName()};

import upe.process.UProcessComponent;
import upe.process.impl.AbstractUActionImpl;

public abstract class ${mClass.getName()} extends AbstractUActionImpl {


    public ${mClass.getName()}(UProcessComponent parent, String name) {
        super(parent, name);
    }

    public UProcessComponent getParent() {
       return (UProcessComponent)getProcess().getProcessElement(getElementPath()+"/..");
    }
}
"""
