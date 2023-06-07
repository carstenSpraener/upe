import java.util.ArrayList;
import java.util.List;

class CodeSnippet {
    String id;
    List<String> codeLines = new ArrayList<>();

    CodeSnippet(String id, String code) {
        this.id = id;
        for( String line : code.split("\n") ) {
            codeLines.add(line);
        }
    }

    CodeSnippet insertBefore(String code) {
        codeLines.add(0, code);
        return this;
    }

    CodeSnippet insertAfter(String code) {
        codeLines.add(code);
        return this;
    }

    CodeSnippet println(String codeLine) {
        this.codeLines.add(codeLine);
        return this;
    }

    String evaluate() {
        StringBuilder sb = new StringBuilder();
        this.codeLines.forEach( it-> {
            if( it!=null && !"".equals(it) ) {
                sb.append(it).append("\n");
            }
        });
        return sb.toString();
    }

    boolean contains(String line) {
        if( line==null ) {
            return false;
        }
        for( String l : codeLines ) {
            if( l.equals(line)) {
                return true;
            }
        }
        return false;
    }
}
