package upe.demo.process;

import upe.annotations.*;
import upe.process.*;


import java.util.Map;

@UpeProcess("clientBrowserDispatcherProcess")
public class ClientBrowserDispatcherProcess extends ClientBrowserDispatcherProcessBase {

     public ClientBrowserDispatcherProcess(UProcessEngine pe, String name) {
        super(pe, name);
     }
     
    @Override
    public void initialize(Map<String, Object> args) {
        super.initialize(args);
        try(UProcessModification mod = new UProcessModification(this)) {
            String personID = (String)args.get("personID");
            if( personID != null ) {
                getProcess().getProcessEngine().jumpToProcess("personProcess", args);
            } else {
                getProcess().getProcessEngine().jumpToProcess("clientBrowsingProcess", args);
            }
        }
    }
}
