package upe.profile.rest.generator;

import de.spraener.nxtgen.model.ModelElement;
import de.spraener.nxtgen.model.impl.StereotypeImpl;
import de.spraener.nxtgen.oom.model.*;
import de.spraener.nxtgen.oom.StereotypeHelper;

public class GeneratorGapTransformation extends GeneratorGapTransformationBase {

    public static final String ORIGINAL_CLASS = "originalClass";

    @Override
    public void doTransformationIntern(MClass mc) {
        MClass genGap = mc.getPackage().createMClass(mc.getName()+"Base");
        mc.getStereotypes().forEach(
                st -> {
                    genGap.addStereotypes(new StereotypeImpl(st.getName()+"Base"));
                }
        );
        genGap.putObject(ORIGINAL_CLASS, mc);
        genGap.setModel(mc.getModel());
    }

    public static MClass getOriginalClass(MClass mc) {
        return (MClass)mc.getObject(ORIGINAL_CLASS);
    }
}
