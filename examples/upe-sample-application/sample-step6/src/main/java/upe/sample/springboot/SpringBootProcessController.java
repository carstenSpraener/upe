package upe.sample.springboot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import upe.resource.UpeDialog;
import upe.resource.model.ProcessActionTrigger;
import upe.resource.model.ProcessDelta;
import upe.resource.model.ProcessPutValue;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class SpringBootProcessController {
    @Value("${upe.start-process:login}")
    private String startProcessName;

    @GetMapping(value = "/ping", produces = MimeTypeUtils.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong!");
    }

    @GetMapping("/init")
    public ResponseEntity<ProcessDelta> startNewProcess(HttpServletRequest request) {
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
        return ResponseEntity.ok().body(dialog.initiateProcess(startProcessName, args));
    }

    @GetMapping("/{dialogID}/{stepNr}")
    public ResponseEntity<ProcessDelta> getDialogStateToStep(@PathVariable String dialogID, @PathVariable int stepNr ) {
        UpeDialog dialog = new UpeDialog();
        return ResponseEntity.ok(dialog.getDialogState(dialogID, stepNr));
    }

    @GetMapping("/{dialogID}")
    @ResponseBody
    public ResponseEntity<ProcessDelta> getDialogState( @PathVariable String dialogID) {
        UpeDialog dialog = new UpeDialog();
        return ResponseEntity.ok(dialog.getDialogState(dialogID, Integer.MAX_VALUE));
    }

    @PutMapping("/{dialogID}/value")
    @ResponseBody
    public ResponseEntity<ProcessDelta> handleUpdateValue(@RequestBody ProcessPutValue putValue) {
        UpeDialog dialog = new UpeDialog();
        ProcessDelta delta = dialog.putValueChange(putValue.getDialogID(), Integer.parseInt(putValue.getStep()), putValue.getFieldPath(), putValue.getNewValue());
        return ResponseEntity.ok(delta);
    }


    @PutMapping("/{dialogID}/action")
    @ResponseBody
    public ResponseEntity<ProcessDelta> triggerAction(@RequestBody ProcessActionTrigger actionTrigger) throws JsonProcessingException {
        UpeDialog dialog = new UpeDialog();
        ProcessDelta delta = dialog.triggerAction(actionTrigger.getDialogID(), Integer.parseInt(actionTrigger.getStep()), actionTrigger.getFieldPath());
        System.out.println(new ObjectMapper().writeValueAsString(delta));
        return ResponseEntity.ok(delta);
    }

}
