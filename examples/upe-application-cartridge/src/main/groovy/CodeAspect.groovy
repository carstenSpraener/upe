class CodeAspect {
    String name;
    List<CodeSnippet> snippets = new ArrayList<>();

    CodeAspect(String name) {
        this.name = name;
    }

    CodeAspect addSnippet(CodeSnippet snippet) {
        this.snippets.add(snippet)
        return this;
    }

    CodeSnippet getById(String id) {
        for( CodeSnippet snippet : this.snippets ) {
            if( snippet.id.equals(id)) {
                return snippet;
            }
        }
        return new CodeSnippet(id, "");
    }

    boolean contains(String line) {
        for( CodeSnippet snippet : snippets ) {
            if( snippet.contains(line)) {
                return true;
            }
        }
        return false;
    }

    String evaluate() {
        StringBuilder sb = new StringBuilder();
        for( CodeSnippet snippet: snippets ) {
            sb.append(snippet.evaluate());
        }
        return sb.toString();
    }

    void add(String string) {
        this.snippets.add(new CodeSnippet("UNDEFINED",string));
    }

    CodeAspect addIfNotPresent( String line) {
        if( !contains(line)) {
            add(line);
        }
        return this;
    }
}
