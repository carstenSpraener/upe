import de.spraener.nxtgen.ProtectionStrategieDefaultImpl
import de.spraener.nxtgen.oom.model.MClass
import de.spraener.nxtgen.oom.model.MOperation
import de.spraener.nxtgen.oom.model.OOModel
import upe.profile.rest.generator.GeneratorGapTransformation
import upe.profile.rest.generator.ValidatorOperationToClass

MClass mClass = this.getProperty("modelElement");
MOperation orgOp = ValidatorOperationToClass.getOriginalOperation(mClass);
OOModel model = mClass.getModel();

def argList(MOperation op) {
    StringBuilder sb = new StringBuilder();
    op.parameters.forEach {
        if( sb.length() > 0 ) {
            sb.append( ", " )
        }
        sb.append( "String ${it.name}")
    }
    return sb.toString()
}

"""//${ProtectionStrategieDefaultImpl.GENERATED_LINE}
package ${mClass.getPackage().getFQName()};

import upe.process.UProcessComponent;

public class ${mClass.getName()} extends ${mClass.getName()}Base {

    @Override
    protected boolean hasError(${argList(orgOp)}) {
        // TODO: Remove Generated-MarkerLine and implement this method.
        return true;
    }
}
"""
