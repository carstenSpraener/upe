package upe.profile.rest.common;

import de.spraener.nxtgen.model.ModelElement;

import java.util.ArrayList;
import java.util.List;

public abstract class CodeSnippet {
    private List<CodeSnippet> beforeMe = null;
    private List<CodeSnippet> afterMe = null;

    private Object aspect;
    private ModelElement me;

    public CodeSnippet(Object aspect) {
        this(aspect, null);
    }

    public CodeSnippet(Object aspect, ModelElement me) {
        this.aspect = aspect;
        this.me = me;
    }

    public List<CodeSnippet> getBeforeMe() {
        if( beforeMe == null ) {
            beforeMe = new ArrayList<>();
        }
        return beforeMe;
    }

    public List<CodeSnippet> getAfterMe() {
        if( afterMe == null ) {
            afterMe = new ArrayList<>();
        }
        return afterMe;
    }

    public CodeSnippet insertBefore(Object aspect, String codeLine) {
        return insertBefore(aspect, null, codeLine);
    }

    public CodeSnippet insertBefore(Object aspect, ModelElement attr, String code) {
        getBeforeMe().add(new CodeBlockSnippet(aspect, attr, code));
        return this;
    }

    public CodeSnippet insertAfter(Object aspect, String codeLine) {
        getAfterMe().add(new SingleLineSnippet(aspect,codeLine));
        return this;
    }

    public void evaluate(StringBuilder sb) {
        if( beforeMe != null) {
            for( CodeSnippet before : this.beforeMe ) {
                before.evaluate(sb);
            }
        }

        this.evaluateIntern(sb);

        if( afterMe != null ) {
            for( CodeSnippet after : this.afterMe ) {
                after.evaluate(sb);
            }
        }
    }

    protected abstract void evaluateIntern(StringBuilder sb);

    public boolean matches(Object key, ModelElement me) {
        if( this.aspect!=null && this.aspect.equals(key) ) {
            if( this.me==null && me==null ) {
                return true;
            } else {
                return me.equals(this.me);
            }
        }
        return false;
    }
}
