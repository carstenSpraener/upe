import de.spraener.nxtgen.ProtectionStrategieDefaultImpl
import de.spraener.nxtgen.oom.StereotypeHelper
import de.spraener.nxtgen.oom.model.MClass
import de.spraener.nxtgen.oom.model.MOperation
import de.spraener.nxtgen.oom.model.OOModel

MClass mClass = this.getProperty("modelElement");
OOModel model = mClass.getModel();

String printOperation(MOperation op) {
    StringBuilder paramList = new StringBuilder();
    op.parameters.forEach {
        if( !paramList.size()==0 ) {
            paramList.append(", ");
        }
        paramList.append("${it.type} ${it.name}");
    }
    return "    ${op.type} ${op.name}(${paramList.toString()});\n"
}

String listOperations(MClass mc) {
    StringBuilder sb = new StringBuilder();
    mc.operations.forEach {
        sb.append(printOperation(it))
    }
    return sb.toString();
}

"""//${ProtectionStrategieDefaultImpl.GENERATED_LINE}
package ${mClass.getPackage().getFQName()};

import upe.annotations.UpeBackendFacade;

@UpeBackendFacade("${StereotypeHelper.getStereotype(mClass, "UBackendFacade").getTaggedValue("facadeName")}")
public interface ${mClass.getName()} {
${listOperations(mClass)}
}
"""

