package upe.sample;

import upe.annotations.UpeProcess;
import upe.annotations.UpeProcessField;
import upe.process.UProcessEngine;
import upe.process.UProcessTextField;
import upe.process.impl.AbstractUProcessImpl;

import java.util.Map;

@UpeProcess(HelloWorldProcess.NAME)
public class HelloWorldProcess extends AbstractUProcessImpl {
    public static final String NAME="helloWorld";

    @UpeProcessField("content")
    private UProcessTextField content;

    public HelloWorldProcess(UProcessEngine pe, String name) {
        super(pe, name);
    }

    /**
     * The callArgs-Map from the login action will be passed to this
     * initialize method and can be used here.
     * @param args
     */
    @Override
    public void initialize(Map<String, Object> args) {
        // Is there a "user" argument in the args map?
        if( args.get("user") != null ) {
            // if yes set the content to say hello to the user
            content.setStringValue("Hello to '"+args.get("user")+"'");
        } else {
            // if not set the content to say hello to an anonymous user.
            content.setStringValue("Hello to anonymous.");
        }
    }

    @Override
    public Map<String, Object> finish() {
        return null;
    }

    @Override
    public Map<String, Object> cancel() {
        return null;
    }
}
