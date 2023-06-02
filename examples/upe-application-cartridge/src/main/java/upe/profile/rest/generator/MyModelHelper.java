package upe.profile.rest.generator;

import de.spraener.nxtgen.model.ModelElement;
import de.spraener.nxtgen.oom.StereotypeHelper;
import de.spraener.nxtgen.oom.model.MAssociation;
import de.spraener.nxtgen.oom.model.MClass;
import de.spraener.nxtgen.oom.model.MDependency;
import de.spraener.nxtgen.oom.model.OOModel;

import java.util.ArrayList;
import java.util.List;

public class MyModelHelper {

    public static String toJavaAccessorName(String aName) {
        if (aName == null || "".equals(aName)) {
            return "";
        }
        if (aName.length() == 1) {
            return aName.toUpperCase();
        }
        return aName.substring(0, 1).toUpperCase() + aName.substring(1);
    }

    public static String upeTypeFor(String javaType) {
        switch( javaType.toLowerCase() ) {
            case "string":
                return "UProcessTextField";
            case "int":
            case "integer":
            case "long":
            case "double":
            case "float":
            case "bigdecimal":
                return "UProcessDecimalField";
            case "boolean":
                return "UProcessBooleanField";
            case "date":
                return "UProcessDateField";
            default:
                return "UProcessField";
        }
    }

    public static String upeFieldTypeFor(String type) {
        switch(type.toLowerCase()) {
            case "string":
                return "String";
            case "int":
            case "integer":
            case "long":
            case "double":
            case "float":
            case "bigdecimal":
                return "Decimal";
            case "boolean":
                return "Boolean";
            case "date":
                return "Date";
            default:
                return type;
        }
    }

    public static String upeJavaTypeFor(String type) {
        switch(type.toLowerCase()) {
            case "string":
                return "String";
            case "int":
            case "integer":
            case "long":
            case "double":
            case "float":
            case "bigdecimal":
                return "java.math.BigDecimal";
            case "boolean":
                return "Boolean";
            case "date":
                return "java.util.Date";
            default:
                return type;
        }
    }

    public static class Assoc {
        public static boolean isToN(MAssociation association) {
            if (association == null || association.getMultiplicity() == null) {
                return false;
            }
            return association.getMultiplicity().endsWith("*") || association.getMultiplicity().endsWith("n");
        }

        public static MClass getTargetClass(MAssociation a) {
            OOModel model = (OOModel) a.getModel();
            ModelElement target = model.findClassByName(a.getType());
            if (target instanceof MClass) {
                return (MClass) target;
            }
            return null;
        }

        public static boolean isActionAssociation(MAssociation assoc) {
            return StereotypeHelper.hasStereotye(getTargetClass(assoc), UPEStereotypes.UPROCESSACTION.getName());
        }
    }

    public static class MDHelper {

        public static List<MAssociation> getMasterDetailReferences(de.spraener.nxtgen.oom.model.MClass mClass) {
            List<MAssociation> resultLiIst = new ArrayList<>();
            for (MAssociation assoc : mClass.getAssociations()) {
                if (StereotypeHelper.hasStereotye(assoc, UPEStereotypes.MASTERLIST.getName())) {
                    resultLiIst.add(assoc);
                }
            }
            return resultLiIst;
        }

        public static boolean hasMasterDetailReference(de.spraener.nxtgen.oom.model.MClass mClass) {
            return !getMasterDetailReferences(mClass).isEmpty();
        }

        public static MClass getDetailProcess(MAssociation dep) {
            return ((OOModel) dep.getModel()).findClassByName(dep.getType());
        }

        public static boolean isMasterDetailAssociation(MAssociation association) {
            return StereotypeHelper.hasStereotye(association, UPEStereotypes.MASTERLIST.getName());
        }
    }
}
