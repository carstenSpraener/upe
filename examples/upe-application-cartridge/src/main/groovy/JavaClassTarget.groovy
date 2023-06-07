class JavaClassTarget {
    CodeAspect header = new CodeAspect("header");
    CodeAspect imports = new CodeAspect("imports");
    CodeAspect declaration = new CodeAspect("declaration");
    CodeAspect attributes = new CodeAspect("attributes");
    CodeAspect methods = new CodeAspect("methods");

    String evaluate() {
        StringBuilder sb = new StringBuilder()
        sb.append(header.evaluate())
        sb.append("\n");
        sb.append(imports.evaluate())
        sb.append("\n");
        sb.append(declaration.evaluate())
        sb.append("\n");
        sb.append(attributes.evaluate())
        sb.append("\n");
        sb.append(methods.evaluate())
        sb.append("}\n");
        return sb.toString()
    }
}
