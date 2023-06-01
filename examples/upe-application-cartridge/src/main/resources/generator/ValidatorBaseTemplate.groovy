import de.spraener.nxtgen.ProtectionStrategieDefaultImpl
import de.spraener.nxtgen.model.Stereotype
import de.spraener.nxtgen.oom.StereotypeHelper
import de.spraener.nxtgen.oom.model.MClass
import de.spraener.nxtgen.oom.model.MOperation
import de.spraener.nxtgen.oom.model.OOModel
import upe.profile.rest.generator.GeneratorGapTransformation
import upe.profile.rest.generator.UPEStereotypes
import upe.profile.rest.generator.ValidatorOperationToClass
import upe.profile.rest.generator.ValidatorOperationToClassBase

MClass mClass = this.getProperty("modelElement");
MClass orgClass = GeneratorGapTransformation.getOriginalClass(mClass);
MOperation orgOp = ValidatorOperationToClass.getOriginalOperation(orgClass)
OOModel model = mClass.getModel();
Stereotype sType = StereotypeHelper.getStereotype(orgOp, UPEStereotypes.UVALIDATOR.name);
String msgID = sType.getTaggedValue("msgID")
String msgLevel = sType.getTaggedValue("msgLevel")
String msgTxt = sType.getTaggedValue("msgText")


def argList(MClass op) {
    StringBuilder sb = new StringBuilder();
    op.attributes.forEach {
        if( sb.length() > 0 ) {
            sb.append( ", " )
        }
        sb.append( "${it.type} ${it.name}")
    }
    return sb.toString()
}

def readValues(MClass mc) {
    StringBuilder sb = new StringBuilder();
    mc.attributes.forEach {
        String name = it.name
        sb.append("        String ${name} = ((UProcessField)proc.getProcessElement(\"${name}\")).getValueForFrontend();\n")
    }
    return sb.toString()
}

def paramList(MClass mc) {
    StringBuilder sb = new StringBuilder();
    mc.attributes.forEach {
        if( sb.length()>0 ) {
            sb.append(", ");
        }
        String name = it.name
        sb.append(name);
    }
    return sb.toString()
}

def listFields(MClass mc) {
    StringBuilder sb = new StringBuilder();
    mc.attributes.forEach {
        if( sb.length()>0 ) {
            sb.append(",\n");
        }
        String name = it.name
        sb.append("                    \"${name}\"");
    }
    return sb.toString()
}

"""//${ProtectionStrategieDefaultImpl.GENERATED_LINE}
package ${mClass.getPackage().getFQName()};

import upe.process.UProcessComponent;
import upe.process.UProcessField;
import upe.process.messages.UProcessMessage;
import upe.process.messages.UProcessMessageImpl;
import upe.process.messages.UProcessMessageStorage;
import upe.process.validation.impl.UProcessValidatorSupport;


public abstract class ${mClass.getName()} extends UProcessValidatorSupport {
    private static final UProcessMessage MY_MSG = new UProcessMessageImpl(
            "${msgID}",
            "${msgTxt}",
            UProcessMessage.MESSAGE_LEVEL_${msgLevel.toUpperCase()}
    );

    static {
        UProcessMessageStorage.getInstance().storeMessage(MY_MSG);
    }

    protected String getMessageID() {
        return "${msgID}";
    }

    @Override
    public void validate(UProcessComponent proc) {
${readValues(orgClass)}
        if( hasError(${paramList(orgClass)}) ) {
            addMessage(
                    getMessageID(),
                    proc,
${listFields(orgClass)}
                    );
        } else {
            removeMessage(
                    getMessageID(),
                    proc,
${listFields(orgClass)}
            );
        }
    }

    protected abstract boolean hasError(${argList(orgClass)});
}
"""
