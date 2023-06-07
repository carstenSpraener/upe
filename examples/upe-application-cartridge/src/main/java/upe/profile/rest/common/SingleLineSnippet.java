package upe.profile.rest.common;

import de.spraener.nxtgen.model.ModelElement;

public class SingleLineSnippet extends CodeSnippet {
    private String lineOfCode;

    public SingleLineSnippet(Object aspect, String lineOfCode) {
        this(aspect, null, lineOfCode);
    }

    public SingleLineSnippet(Object aspect, ModelElement me, String lineOfCode) {
        super(aspect, me);
        this.lineOfCode = lineOfCode;
    }

    @Override
    protected void evaluateIntern(StringBuilder sb) {
        sb.append(lineOfCode).append("\n");
    }
}
