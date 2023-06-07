package upe.profile.rest.common;

import de.spraener.nxtgen.model.ModelElement;

public class CodeBlockSnippet extends CodeSnippet {

    private final String codeBlock;

    public CodeBlockSnippet(Object key, ModelElement me, String codeBlock ) {
        super(key, me);
        this.codeBlock = codeBlock;
    }

    @Override
    protected void evaluateIntern(StringBuilder sb) {
        sb.append(this.codeBlock);
    }
}
