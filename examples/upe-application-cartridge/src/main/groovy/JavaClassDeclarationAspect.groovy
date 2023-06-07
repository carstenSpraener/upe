import de.spraener.nxtgen.oom.StereotypeHelper
import de.spraener.nxtgen.oom.model.MClass
import de.spraener.nxtgen.oom.model.MClassRef

class JavaClassDeclarationAspect {
    MClass mClass;

    JavaClassDeclarationAspect(MClass c) {
        this.mClass = c;
    }

    JavaClassDeclarationAspect apply(JavaClassTarget targetClazz) {
        CodeAspect importAspect = targetClazz.imports;

        StringBuilder implementsList = new StringBuilder();
        String extendsCode = "";
        for( MClassRef impRef: mClass.getInheritsFrom() ) {
            if( StereotypeHelper.hasStereotye(impRef, "Implements" ) ) {
                MClass target = impRef.getMClass(mClass.getModel());
                if( implementsList.length() > 0 ) {
                    implementsList.append(',');
                } else {
                    implementsList.add(" implements ");
                }
                implementsList.append(target.getName());
                String importLine = "import ${target.getFQName()};"
                importAspect.addIfNotPresent(importLine);
            }
            if( StereotypeHelper.hasStereotye(impRef, "Extends") ) {
                MClass target = impRef.getMClass(mClass.getModel());
                String importLine = "import ${target.getFQName()};"
                importAspect.addIfNotPresent(importLine);
                extendsCode = " extends ${target.getName()}"
            }
        }
        targetClazz.declaration.add("public class ${mClass.getName()}${extendsCode}${implementsList.toString()} {")
    }

}
