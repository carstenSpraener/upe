package upe.profile.rest.common;

import de.spraener.nxtgen.model.ModelElement;

import java.util.*;

public abstract class AbstractClassSection implements ClassSection {
    Map<Object, List<CodeSnippet>> mySnippets = new TreeMap<>();

    private List<CodeSnippet> getCodeSnippetList(Object key) {
        List<CodeSnippet> list = mySnippets.get(key);
        if( list == null ) {
            list = new ArrayList<>();
            mySnippets.put(key, list);
        }
        return list;
    }

    public AbstractClassSection withSnippet(Object key, CodeSnippet snippet) {
        getCodeSnippetList(key).add(snippet);
        return this;
    }

    public AbstractClassSection withSnippet(Object key, String lineOfCode) {
        getCodeSnippetList(key).add(new SingleLineSnippet(key, lineOfCode));
        return this;
    }

    @Override
    public CodeSnippet getSnippet(Object key) {
        return getCodeSnippetList(key).get(0);
    }

    @Override
    public CodeSnippet getSnippet(Object key, ModelElement me) {
        for( CodeSnippet snippet : getCodeSnippetList(key) ) {
            if( snippet.matches(key, me)) {
                return snippet;
            }
        }
        return null;
    }

    @Override
    public void evaluate(StringBuilder sb) {
        for( List<CodeSnippet> snippetList : this.mySnippets.values() ) {
            for( CodeSnippet snippet : snippetList ) {
                snippet.evaluate(sb);
            }
        }
    }

}
