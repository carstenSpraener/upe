import de.spraener.nxtgen.ProtectionStrategieDefaultImpl
import de.spraener.nxtgen.model.ModelElement
import de.spraener.nxtgen.oom.StereotypeHelper
import de.spraener.nxtgen.oom.model.MAssociation
import de.spraener.nxtgen.oom.model.MAttribute
import de.spraener.nxtgen.oom.model.MClass
import de.spraener.nxtgen.oom.model.MDependency
import de.spraener.nxtgen.oom.model.MOperation
import de.spraener.nxtgen.oom.model.OOModel
import upe.profile.rest.generator.JavaHelper
import upe.profile.rest.generator.MyModelHelper
import upe.profile.rest.generator.UPEStereotypes

class UProcessBaseTemplate {
    MClass mClass
    MClass orgClass
    OOModel model

    UProcessBaseTemplate(ModelElement me) {
        this.mClass = (MClass)me;
        this.orgClass = upe.profile.rest.generator.GeneratorGapTransformation.getOriginalClass(this.mClass);
        this.model = orgClass.getModel()
    }

    String getScaffoldsTarget() {
        String target = null;
        orgClass.dependencies.forEach {
            if (StereotypeHelper.hasStereotye(it, "Scaffolds")) {
                target = it.target;
            }
        }
        return target;
    }

    String scaffoldsDefinition() {
        StringBuilder sb = new StringBuilder();
        orgClass.dependencies.forEach {
            if( StereotypeHelper.hasStereotye(it, "Scaffolds") ) {
                String target = it.getTarget();
                sb.append(
                        """
@UpeScaffolds(${target}.class)
"""
                )
            }
        }
        return sb.toString();
    }


    String masterDetailImports() {
        if( MyModelHelper.MDHelper.hasMasterDetailReference(orgClass)) {
            return """import upe.common.MasterDetailConfiguration;
import upe.common.MasterProcessComponent;
"""
        } else {
            return "";
        }
    }
    String generateActionField(MAssociation assoc, MClass target) {
        return """    @UpeProcessAction("${assoc.name}")
    private ${target.getFQName()} ${assoc.getName()};
"""
    }

    String generateProcessComponentField( MAssociation assoc, MClass target) {
        if( MyModelHelper.Assoc.isToN(assoc) ) {
            return """    @UpeProcessComponent(value="${assoc.name}", listType=${target.getFQName()}.class)
    private UProcessComponentList<${target.getFQName()}> ${assoc.getName()};
"""
        } else {
            return """    @UpeProcessComponent("${assoc.name}")
    private ${target.getFQName()} ${assoc.getName()};
"""
        }
    }

    String generateDecimalFieldExtras(String accessName, String upeFieldType,String upeJavaType) {
        if( !upeJavaType.equals("java.math.BigDecimal") ) {
            return "";
        }
        return """

    public void set${accessName}Value(Number value) {
        get${accessName}Field().set${upeFieldType}Value(new java.math.BigDecimal(value.doubleValue()));
    }"""
    }

    String generateFieldMethods(String fieldName, String fieldType) {
        String accessName = MyModelHelper.toJavaAccessorName(fieldName);
        String upeType = MyModelHelper.upeTypeFor(fieldType);
        String upeFieldType = MyModelHelper.upeFieldTypeFor(fieldType);
        String upeJavaType = MyModelHelper.upeJavaTypeFor(fieldType);
        return """
    public ${upeJavaType} get${accessName}Value() {
        return get${accessName}Field().get${upeFieldType}Value();
    }

    public void set${accessName}Value(${upeJavaType} value) {
        get${accessName}Field().set${upeFieldType}Value(value);
    }${generateDecimalFieldExtras(accessName, upeFieldType, upeJavaType)}

    public ${upeType} get${accessName}Field() {
        return getProcessElement("${fieldName}", ${upeType}.class);
    }
"""
    }

    String generateFieldAccessMethods() {
        StringBuilder sb = new StringBuilder();
        sb.append("    // Access-Methods for fields and their values")
        for( MAttribute attr : orgClass.attributes ) {
            String fieldName = attr.getName();
            String javaType = attr.getType();
            sb.append(generateFieldMethods(fieldName, javaType));
        }
        String scaffolded = getScaffoldsTarget();
        if( scaffolded != null ) {
            MClass scaffClass = this.model.findClassByName(scaffolded);
            scaffClass.attributes.forEach {
                String fieldName = it.getName();
                String javaType = it.getType();
                sb.append(generateFieldMethods(fieldName, javaType));
            }
        } else {
            sb.append( "    // No scaffold defined." );
        }
        return sb.toString();
    }

    String generateSubComponentsGetter() {
        StringBuilder sb = new StringBuilder();
        orgClass.associations.forEach {
            String accessor = MyModelHelper.toJavaAccessorName(it.getName());
            if( MyModelHelper.MDHelper.isMasterDetailAssociation(it) ) {
                sb.append("""
    public MasterProcessComponent get${accessor}Master() {
        return  getProcessElement("${it.getName()}Master", MasterProcessComponent.class);
    }
""")
            }
            sb.append("    // Association to "+it.type+"\n");
            MClass target = MyModelHelper.Assoc.getTargetClass(it);
            if( StereotypeHelper.hasStereotye(target, UPEStereotypes.UPROCESSCOMPONENT.name)) {
                if( MyModelHelper.Assoc.isToN(it) ) {
                    sb.append("""
    public UProcessComponentList<${it.type}> get${accessor}() {
        return getProcessElement("${it.name}", UProcessComponentList.class);
    }
""")
                } else {
                    sb.append("""
    public ${target.getFQName()} get${accessor}() {
        return getProcessElement("${it.name}",${target.getFQName()}.class);
    }
""")
                }
            }
        }
        return sb.toString();
    }

    String peReferences() {
        StringBuffer sb = new StringBuffer();
        sb.append("    // Components by References\n")
        orgClass.associations.forEach {
            if( MyModelHelper.MDHelper.isMasterDetailAssociation(it) ) {
                sb.append("""    @UpeProcessComponent()
    private MasterProcessComponent ${it.getName()}Master;
""")
            }
            sb.append("    // Association to "+it.type+"\n");
            MClass target = MyModelHelper.Assoc.getTargetClass(it);
            if( StereotypeHelper.hasStereotye(target, UPEStereotypes.UPROCESSACTION.name)) {
                sb.append(generateActionField(it, target))
            } else if( StereotypeHelper.hasStereotye(target, UPEStereotypes.UPROCESSCOMPONENT.name)) {
                sb.append(generateProcessComponentField(it, target))
            } else {
                de.spraener.nxtgen.NextGen.LOGGER.severe("Unknownd target for reference "+it.name+" from "+orgClass.name+"  to "+it.type);
            }
        }
        return sb.toString();
    }

    String generateActionGetters() {
        StringBuilder sb = new StringBuilder();
        for( MAssociation a : orgClass.getAssociations() ) {
            if( MyModelHelper.Assoc.isActionAssociation(a) ) {
                sb.append """
    public ${a.getType()} get${MyModelHelper.toJavaAccessorName(a.getName())}() {
        return (${a.getType()}) getProcessElement("${a.getName()}");
    }
"""
            }
        }
        return sb.toString();
    }

    String mapToProcessFieldType(String type) {
        if( type.equalsIgnoreCase("string")) {
            return "UProcessTextField"
        } else if( type.equalsIgnoreCase("int")
                || type.equalsIgnoreCase("integer")
                || type.equalsIgnoreCase("double")
                || type.equalsIgnoreCase("float")
        ) {
            return "UProcessDecimalField"
        } else if( type.equalsIgnoreCase("boolean")) {
            return "UProcessBooleanField"
        } else if( type.equalsIgnoreCase("date")) {
            return "UProcessDateField"
        } else if( type.equalsIgnoreCase("image")) {
            return "UProcessImageField"
        }
    }
    String peFields() {
        StringBuilder sb = new StringBuilder()
        this.orgClass.attributes.forEach {
            String type = mapToProcessFieldType(it.type)
            String name = it.name
            sb.append("    @UpeProcessField\n    upe.process.${type} ${name};\n")
        }
        return sb.toString();
    }

    String validations() {
        List<MAttribute> attrList = new ArrayList();
        attrList.addAll(this.mClass.attributes);
        StringBuilder sb = new StringBuilder();
        orgClass.dependencies?.forEach {
            if( StereotypeHelper.hasStereotye(it, "Scaffolds") ) {
                MClass target = this.model.findClassByName(it.target);
                attrList.addAll(target.attributes);
            }
        }
        attrList.forEach {
            if( it.getProperties().get("required")!=null ) {
                sb.append(
                        """        this.addValidator(new MandantoryValidator("${it.name}"));
"""
                )
            }
        }
        orgClass.operations.forEach {
            if( StereotypeHelper.hasStereotye(it, UPEStereotypes.UVALIDATOR.name) ) {
                String cName = JavaHelper.firstToUpperCase(it.name)
                sb.append("        super.addValidator(new ${orgClass.getPackage().getFQName()}.validations.${cName}());\n");
            }
        }
        return sb.toString();
    }

    String generateMDInitialization( MAssociation assoc) {
        MClass detail = MyModelHelper.MDHelper.getDetailProcess(assoc)
        String detailProcessName = JavaHelper.firstToUpperCase(detail.getName());
        String listName = assoc.getName();
        return """            ${listName}Master.init(
                 new MasterDetailConfiguration(
                    getElementPath()+"/${listName}",
                    "${detailProcessName}",
                    "id"
                ).withDataSupplier(this::load${JavaHelper.firstToUpperCase(listName)}Data)
            );
"""
    }
    String initializations() {
        StringBuilder sb = new StringBuilder();
        if( MyModelHelper.MDHelper.hasMasterDetailReference(orgClass) ) {
            MyModelHelper.MDHelper.getMasterDetailReferences(orgClass).forEach {
                sb.append(generateMDInitialization(it))
            }
        }
        return sb.toString();
    }

    String mdLoadDataMethods() {
        StringBuilder sb = new StringBuilder();
        for(MOperation op : orgClass.operations) {
            if( StereotypeHelper.hasStereotye(op, UPEStereotypes.MDDATALOADER.getName()) ) {
                sb.append("""
    public ${op.getType()} ${op.getName()} () {
        return new java.util.ArrayList<>();
    }
""")
            }
        }
        return sb.toString();
    }
    String generate() {
        return """//${ProtectionStrategieDefaultImpl.GENERATED_LINE}
package ${mClass.getPackage().getFQName()};

import upe.annotations.*;
import upe.process.*;
import upe.process.impl.AbstractUProcessImpl;
${masterDetailImports()}

import java.io.Serializable;
import java.util.Map;

public abstract class ${mClass.getName()} extends AbstractUProcessImpl {
${peFields()}${peReferences()}
     public ${mClass.getName()}(UProcessEngine pe, String name) {
        super(pe, name);
        ${validations()}
     }

    @Override
    public void initialize(Map<String, Serializable> args) {
        try(UProcessModification mod = new UProcessModification(this)) {
${initializations()}
        }
    }

    @Override
    public Map<String, Serializable> finish() {
        return null;
    }

    @Override
    public Map<String, Serializable> cancel() {
        return null;
    }
${mdLoadDataMethods()}
}
"""
    }

}
