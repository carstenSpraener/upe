package upe.profile.rest.generator;

import de.spraener.nxtgen.model.impl.StereotypeImpl;
import de.spraener.nxtgen.oom.model.*;
import de.spraener.nxtgen.oom.StereotypeHelper;

import java.util.Scanner;

public class FacadeScaffoldOperations extends FacadeScaffoldOperationsBase {

    @Override
    public void doTransformationIntern(MClass me) {
        StereotypeImpl sType = (StereotypeImpl)StereotypeHelper.getStereotype(me, UPEStereotypes.UBACKENDFACADE.getName());
        String facadeName = sType.getTaggedValue("facadeName");
        if( facadeName==null || "".equals(facadeName) ) {
            facadeName = JavaHelper.firstToLowerCase(me.getName());
            sType.setTaggedValue("facadeName", facadeName);
        }
        MClass scaffolded = findScaffolded(me);
        if( scaffolded==null ) {
            return;
        }
        createCreateMethod(me, scaffolded);
        createReadMethods(me, scaffolded);
        createSaveMethod(me, scaffolded);
        createDeleteMethods(me, scaffolded);
    }

    private MClass findScaffolded(MClass me) {
        return me.getDependencies().stream()
                .filter(d -> StereotypeHelper.hasStereotye(d, UPEStereotypes.SCAFFOLDS.getName()))
                .map(d -> ((OOModel)me.getModel()).findClassByName(d.getTarget()) )
                .findFirst()
                .orElse(null);
    }

    private void createDeleteMethods(MClass me, MClass scaffolded) {
        MOperation op = me.createOperation("delete"+scaffolded.getName());
        op.setModel(me.getModel());
        op.setType(scaffolded.getFQName());
        op.createParameter("value", scaffolded.getFQName());

        op = me.createOperation("deleteById");
        op.setModel(me.getModel());
        op.setType(scaffolded.getFQName());
        op.createParameter("id", "String");
    }

    private void createSaveMethod(MClass me, MClass scaffolded) {
        MOperation op = me.createOperation("save"+scaffolded.getName());
        op.setModel(me.getModel());
        op.setType(scaffolded.getFQName());
        op.createParameter("value", scaffolded.getFQName());
    }

    private void createReadMethods(MClass me, MClass scaffolded) {
        MOperation op = me.createOperation("findByID");
        op.setModel(me.getModel());
        op.setType(scaffolded.getFQName());
        op.createParameter("id", "String");

        op = me.createOperation("findAll");
        op.setModel(me.getModel());
        op.setType("java.util.List<"+scaffolded.getFQName()+">");

    }

    private void createCreateMethod(MClass me, MClass scaffolded) {
        MOperation op = me.createOperation("create"+ scaffolded.getName());
        op.setType(scaffolded.getFQName());
        op.setModel(me.getModel());
    }

}
