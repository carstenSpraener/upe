package upe.resource.model;

import java.util.ArrayList;
import java.util.List;

public class RsrcLink {
    private String name;
    private String url;
    private List<String> urlArgs;

    public RsrcLink(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public List<String> getUrlArgs() {
        return urlArgs;
    }

    public RsrcLink addUrlArg(String argName) {
        if( this.urlArgs==null) {
            this.urlArgs = new ArrayList<>();
        }
        this.urlArgs.add(argName);
        return this;
    }

    @Override
    public String toString() {
        return "RsrcLink{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", urlArgs=" + urlArgs +
                '}';
    }
}
