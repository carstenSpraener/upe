package upe.profile.rest.generator;

import de.spraener.nxtgen.model.impl.StereotypeImpl;
import de.spraener.nxtgen.oom.model.*;
import de.spraener.nxtgen.oom.StereotypeHelper;

public class MDLoadDataOperationTransformation extends MDLoadDataOperationTransformationBase {

    @Override
    public void doTransformationIntern(MClass me) {
        for( MAssociation assoc : me.getAssociations() ) {
            if( StereotypeHelper.hasStereotye(assoc, UPEStereotypes.MASTERLIST.getName())) {
                addLoadDataOperation(me, assoc);
            }
        }
    }

    private void addLoadDataOperation(MClass me, MAssociation assoc) {
        MOperation op = me.createOperation("load"+JavaHelper.firstToUpperCase(assoc.getName())+"Data");
        StereotypeImpl sType = new StereotypeImpl(UPEStereotypes.MDDATALOADER.getName());
        op.addStereotypes(sType);
        MClass target = ((OOModel)assoc.getModel()).findClassByName(assoc.getType());
        String opType = "java.util.List<?>";
        if( target!=null) {
            String scaffoldedClazz = findScaffolded(target);
            if( scaffoldedClazz!=null) {
                sType.setTaggedValue("", scaffoldedClazz);
                opType = "java.util.List<"+scaffoldedClazz+">";
            }
        }
        op.setType(opType);
        op.setModel(me.getModel());
    }

    private String findScaffolded(MClass aClazz) {
        for(MDependency d : aClazz.getDependencies() ) {
            if( StereotypeHelper.hasStereotye(d, UPEStereotypes.SCAFFOLDS.getName()) ) {
                return d.getTarget();
            }
        }
        return null;
    }
}
