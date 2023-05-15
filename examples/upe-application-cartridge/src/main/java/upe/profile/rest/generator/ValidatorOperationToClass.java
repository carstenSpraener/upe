package upe.profile.rest.generator;

import de.spraener.nxtgen.model.Stereotype;
import de.spraener.nxtgen.oom.model.MClass;
import de.spraener.nxtgen.oom.model.MOperation;
import de.spraener.nxtgen.oom.model.MPackage;
import de.spraener.nxtgen.oom.model.MParameter;

public class ValidatorOperationToClass extends ValidatorOperationToClassBase {

    public static final String ORG_OPERATION = "orgOperation";

    @Override
    public void doTransformationIntern(MOperation me) {
        MClass parent = (MClass)me.getParent();
        MPackage parentPkg = parent.getPackage();
        MPackage valPkg = parentPkg.findOrCreatePackage("validations");
        valPkg.setModel(parent.getModel());
        MClass valClass = valPkg.createMClass(toValidationClassName(me));
        valClass.setModel(parent.getModel());
        for(Stereotype sType : me.getStereotypes() ) {
            valClass.addStereotypes(sType);
        }
        valClass.putObject(ORG_OPERATION, me);
        for( MParameter p : me.getParameters() ) {
            valClass.createAttribute(p.getName(), p.getType())
            .setModel(parent.getModel());
        }
    }

    private String toValidationClassName(MOperation me) {
        return JavaHelper.firstToUpperCase(me.getName());
    }

    public static MOperation getOriginalOperation(MClass mc) {
        return (MOperation)mc.getObject(ORG_OPERATION);
    }
}
