package upe.demo.rest;

import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import upe.resource.UpeDialog;
import upe.resource.model.ProcessDelta;
import upe.resource.model.ProcessPutValue;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public abstract class ProcessController {
    public abstract String getStartProcessName();

    @GetMapping(value = "/ping", produces = MimeTypeUtils.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong!");
    }

    @GetMapping("/init")
    @ResponseBody
    public String startNewProcess(HttpServletRequest request) {
        Map<String,Object> args = new HashMap<>();
        for(Map.Entry<String, String[]> e : request.getParameterMap().entrySet() ) {
            String key = e.getKey();
            String value = "";
            for( String v : e.getValue() ) {
                if( !"".equals(value) ) {
                    value += ";";
                }
                value += v;
            }
            args.put(key,value);
        }
        UpeDialog dialog = new UpeDialog();
        ProcessDelta delta = dialog.initiateProcess(getStartProcessName(), args);
        return new Gson().toJson(delta);
    }

    @GetMapping("/{dialogID}/{stepNr}")
    @ResponseBody
    public String getDialogStateToStep(@PathVariable String dialogID, @PathVariable int stepNr ) {
        UpeDialog dialog = new UpeDialog();
        ProcessDelta delta = dialog.rebuild(dialogID, stepNr);
        return new Gson().toJson(delta);
    }

    @GetMapping("/{dialogID}")
    @ResponseBody
    public String getDialogState( @PathVariable String dialogID) {
        UpeDialog dialog = new UpeDialog();
        ProcessDelta delta = dialog.rebuild(dialogID);
        return new Gson().toJson(delta);
    }

    @PutMapping("/{dialogID}/value")
    @ResponseBody
    public String handleUpdateValue(@RequestBody ProcessPutValue putValue) {
        UpeDialog dialog = new UpeDialog();
        ProcessDelta delta = dialog.putValueChange(putValue.getDialogID(), Integer.parseInt(putValue.getStep()), putValue.getFieldPath(), putValue.getNewValue());
        return new Gson().toJson(delta);
    }
}
