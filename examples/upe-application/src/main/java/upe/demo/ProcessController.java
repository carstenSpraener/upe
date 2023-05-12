package upe.demo;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;
import upe.resource.UpeDialog;
import upe.resource.model.ProcessDelta;
import upe.resource.model.ProcessPutValue;

public abstract class ProcessController {
    @GetMapping("/init")
    @ResponseBody
    public String startNewProcess() {
        UpeDialog dialog = new UpeDialog();
        ProcessDelta delta = dialog.initiateProcess("person", null);
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
