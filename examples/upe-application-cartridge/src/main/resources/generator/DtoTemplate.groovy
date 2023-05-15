package entity


import de.spraener.nxtgen.ProtectionStrategie
import de.spraener.nxtgen.model.ModelElement
import de.spraener.nxtgen.oom.model.MAssociation
import de.spraener.nxtgen.oom.model.MAttribute
import de.spraener.nxtgen.oom.model.MClass
import de.spraener.nxtgen.oom.model.MOperation
import de.spraener.nxtgen.oom.model.MParameter
import upe.profile.rest.generator.JavaHelper

def pkgName = ((MClass)modelElement).getPackage().getFQName()
def cName = modelElement.name;

def extendsExpr(MClass me) {
    if (me.getProperty("extends") == null) {
        return "";
    }
    return " extends " + me.getProperty("extends");
}

def constructorCode(MClass me) {
    if (me.getProperty("constructorCode") == null) {
        return "";
    }
    return me.getProperty("constructorCode");

}

def constructor(MClass c) {
    def args = c.getProperty("constructorArgs");
    def superCallArgs = c.getProperty("superCallArgs");
    if (args != null) {
        return """    public ${c.name}(${args}) {
        super(${superCallArgs});${constructorCode(c)}
    }""";
    } else {
        return """    public ${c.name}(${constructorCode(c)}) {
    }""";
    }
}

def importList(MClass c) {
    def imports = c.getProperty("importList");
    if (imports != null) {
        return "\n" + imports;
    } else {
        return "";
    }
}

def elementAnnotations(ModelElement c) {
    def annotations = c.getProperty("annotations");
    if (annotations != null) {
        return annotations;
    } else {
        return "";
    }
}

def toMethodStub(MOperation op ) {
    def type = op.getType();
    def name = op.getName();
    def paramList = "";
    def annotations=elementAnnotations(op);

    for(MParameter p : op.getParameters() ) {
        paramList += "${p.getType()} ${p.getName()}, ";
    }
    if( paramList.length()>2 ) {
        paramList = paramList.substring(0, paramList.length()-2);
    }
    def methodBody = op.getProperty("methodBody");
    if( methodBody==null ) {
        def returnStatement = op.getProperty("returnStatement");
        if( null == returnStatement ) {
            returnStatement = "return${'void'.equals(type) ? '' : ' null'};";
        }
        methodBody = "// TODO: Implement this method\n        ${returnStatement}";
    }
    return """
    ${annotations}public ${type} ${name}(${paramList}) {
        ${methodBody}
    } 
""";
}
def methodStubs(MClass c) {
    StringBuilder sb = new StringBuilder();
    for (MOperation op : c.getOperations()) {
        sb.append(toMethodStub(op));
    }
    return sb.toString();
}

def attributeDeclaration(MClass modelElement) {
    StringBuilder sb = new StringBuilder();
    modelElement.attributes.each({
        sb.append("    private ${it.type} ${it.name};\n");
    })
    return sb.toString();
}

def referenceDeclaration(MClass modelElement) {
    StringBuilder sb = new StringBuilder();
    modelElement.associations.each({
        m = it.multiplicity
        if( m.equals("Unspecified") ||m.startsWith("1")) {
            sb.append("    private ${it.type} ${it.name};\n");
        } else {
            sb.append("    private java.util.List<${it.type}> ${it.name} = new ArrayList<>();\n");
        }
    })
    return sb.toString();
}

def buildAttributeAccessTemplate(String type, String name) {
    String accessName = JavaHelper.firstToUpperCase(name)
    return """
    public ${type} get${accessName}() {
        return this.${name};
    }
    
    public void set${accessName}( ${type} value ) {
        this.${name} = value;
    }
"""
}
def attributeAccessMethods(MClass modelElement, MAttribute attr ) {
    return buildAttributeAccessTemplate(attr.type, attr.name);
}

def isToN(MAssociation assoc) {
    if( assoc.multiplicity==null || assoc.multiplicity.equals("Undefined") ) {
        return false;
    }
    if( assoc.multiplicity.endsWith("*") ||  assoc.multiplicity.endsWith("n")) {
        return true;
    }
    return false;
}

def associationAccessMethods(MClass modelElement, MAssociation attr ) {
    System.out.println("Generating access for attribute "+attr.name+" and Type "+attr.type)
    String accessName = JavaHelper.firstToUpperCase(attr.name);
    String result;
    if( isToN(attr) ) {
        result = """
    public void set${accessName}(List<${attr.type}> value) {
        this.${attr.name} = value;
    }
        
    public List<${attr.type}> get${accessName}() {
        return this.${attr.name};
    }
        
    public void add${accessName}(${attr.type} value) {
        if( this.${attr.name} == null ) {
            this.${attr.name} = new java.util.ArrayList<>();
        }
        this.${attr.name}.add(value);
    }
        
    public void add${accessName}(${attr.type} value) {
        if( this.${attr.name} == null ) {
            return;
        }
        this.${attr.name}.remove(value);
    }
        
"""
    } else {
        result = buildAttributeAccessTemplate(attr.type, attr.name);
    }
    return result;
}

def accessMethods(MClass modelElement) {
    StringBuffer sb = new StringBuffer();
    modelElement.attributes.forEach {
        sb.append(attributeAccessMethods(modelElement, it))
    }
    modelElement.associations.forEach {
        sb.append(associationAccessMethods(modelElement, it))
    }
    return sb.toString();
}

return """// ${ProtectionStrategie.GENERATED_LINE}
package ${pkgName};
${importList(modelElement)}
${elementAnnotations(modelElement)}public class ${cName}${extendsExpr(modelElement)} {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(${cName}.class.getName());
${attributeDeclaration(modelElement)}
${referenceDeclaration(modelElement)}
${constructor(modelElement)}
${methodStubs(modelElement)}
    // Access Methods
${accessMethods(modelElement)}
}
"""
