import {Injectable} from "@angular/core";
import {BehaviorSubject, filter} from "rxjs";
import {
  DialogState,
  MAX_SEVERITY,
  Process,
  ProcessDelta,
  ProcessElement,
  ProcessElementDelta,
  ProcessElementListener,
  ProcessField,
  UpeValueUpdate
} from "./upe-client-dto";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";


@Injectable()
export class UpeClientService {
  process$ = new BehaviorSubject<Process | undefined>(undefined);
  processDelta$ = new BehaviorSubject<ProcessDelta | undefined>(undefined);
  activeView$ = new BehaviorSubject<string | undefined>(undefined);
  activeProcess: Process | undefined = undefined;
  activeView: string = "init";
  viewSeverity$ = new BehaviorSubject<{view: string, severity: number }>({view: 'init', severity: 0});

  static serverURL = "http://localhost:8080";

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
  }

  loadProcess(dialogState: DialogState) {
    if (dialogState.activeView && dialogState.activeView !== this.activeView) {
      this.activeView = dialogState.activeView;
      this.activeView$.next(this.activeView);
    }
    if (
      this.activeProcess && this.activeProcess.state &&
      dialogState.dialogID == this.activeProcess.state.dialogID &&
      dialogState.stepCount == this.activeProcess.state.stepCount
    ) {
      //console.log("State already loaded. Nothing has to be done.");
      return;
    }
    //console.log("my State is: "+JSON.stringify(this.activeProcess?.state));
    //console.log("requested State is: "+JSON.stringify(dialogState));
    var url = this.buildBackendCallURL(dialogState);
    //console.log("Requesting Process-State with url '"+url+"'");
    this.http.get<ProcessDelta>(url).subscribe((delta: ProcessDelta) => {
      //console.log("Received process Delta: "+JSON.stringify(delta))
      this.pushNewProcess(delta);
    });
  }

  private buildBackendCallURL(dialogState: DialogState) {
    //console.log("Building url on state: "+JSON.stringify(dialogState))
    var url = UpeClientService.serverURL;
    if (dialogState.processName) {
      url += "/" + dialogState.processName
    } else if (this.activeProcess) {
      url += "/" + this.activeProcess.name
    }
    if (dialogState.dialogID) {
      url += "/" + dialogState.dialogID
      if (dialogState.stepCount) {
        url += "/" + dialogState.stepCount
      } else {
        url += "/0";
      }
    } else {
      url += "/init"
    }
    return url;
  }

  private buildProcessFields(delta: ProcessDelta): Map<string, ProcessField> {
    var fields: Map<string, ProcessField> = new Map();
    for (let peDelta of delta.elementDeltaList) {
      var field: ProcessField = {
        fieldPath: peDelta.elementPath,
        value: peDelta.valueForFrontend,
        isEnabled: peDelta.enabled,
        isVisibel: peDelta.isVisible,
        messages: peDelta.newMessages,
        update$ : new BehaviorSubject<boolean>(false)
      }
      fields.set(field.fieldPath, field);
    }
    return fields;
  }


  private pushNewProcess(delta: ProcessDelta) {
    var p: Process = {
      name: delta.processName,
      state: delta.state,
      processFields: this.buildProcessFields(delta),
      elementListnerMap: new Map<string, ProcessElementListener[]>()
    };

    this.activeProcess = p;
    if (!this.activeView) {
      this.activeView = "init";
    }
    this.process$.next(this.activeProcess);
    this.processDelta$.next(delta);
    this.activeView$.next(this.activeView);
    this.activeProcess.processFields.forEach( pf => pf.update$.next(true));
    var url = this.buildPathFromDialogState(delta.state);
    this.router.navigateByUrl(url);
  }

  private buildPathFromDialogState(dialogState: DialogState) {
    var url = "/upe"
    if (dialogState.dialogID) {
      url += "/d/" + dialogState.dialogID
    }
    if (dialogState.stepCount) {
      url += "/s/" + dialogState.stepCount
    } else {
      url += "/s/0";
    }
    if (this.activeView) {
      url += "/v/" + this.activeView
    }
    return url;
  }

  handleValueChange(fieldPath: string, value: string) {
    if (this.activeProcess && this.activeProcess.state) {
      var dialogState = this.activeProcess.state;
      var updUrl = UpeClientService.serverURL + "/" + this.activeProcess.name + "/" + dialogState.dialogID + "/value";
      var valueChange: UpeValueUpdate = {
        dialogID: dialogState.dialogID,
        fieldPath: fieldPath,
        newValue: value,
        step: dialogState.stepCount
      }
      this.http.put<ProcessDelta>(updUrl, valueChange).subscribe(
        (delta: ProcessDelta) => this.handleDelta(delta)
      );
    }
  }

  private handleDelta(delta: ProcessDelta) {
    if (!this.activeProcess) {
      this.pushNewProcess(delta);
    } else {
      var nextURL = this.buildPathFromDialogState(delta.state);
      this.activeProcess.state = delta.state;
      this.updateActiveProcessModel(delta);
      this.router.navigateByUrl(nextURL);
    }
  }

  changeView(targetView: string) {
    this.activeView = targetView;
    this.activeView$.next(this.activeView);
    if (this.activeProcess && this.activeProcess.state) {
      this.router.navigateByUrl(this.buildPathFromDialogState(this.activeProcess.state));
    }
  }

  updateActiveProcessModel(delta: ProcessDelta) {
    if (!this.activeProcess) {
      return;
    }
    for (let fieldUpd of delta.elementDeltaList) {
      this.updateProcessField(fieldUpd)
    }
    this.process$.next(this.activeProcess);
  }

  private updateProcessField(fieldUpd: ProcessElementDelta): void {
    if( !this.activeProcess ) {
      return;
    }
    var pField = this.activeProcess.processFields.get(fieldUpd.elementPath);
    if( !pField ) {
      pField = {
        fieldPath: fieldUpd.elementPath,
        messages: fieldUpd.newMessages,
        isEnabled: fieldUpd.enabled,
        isVisibel: fieldUpd.isVisible,
        value: fieldUpd.valueForFrontend,
        update$ : new BehaviorSubject<boolean>(false)
      }
    } else {
      pField.messages = fieldUpd.newMessages? fieldUpd.newMessages : []
      pField.value = fieldUpd.valueForFrontend
      pField.isEnabled = fieldUpd.enabled !== undefined? fieldUpd.enabled : true;
      pField.isVisibel = fieldUpd.isVisible !== undefined ? fieldUpd.isVisible : true;
    }
    this.activeProcess.processFields.set(fieldUpd.elementPath, pField);
    pField.update$.next(true);
    this.process$.next(this.activeProcess);
  }

  findProcessElementByPath(elementPath: string): ProcessElement | null {
    if (!this.activeProcess || !this.activeProcess.processFields) {
      return null;
    }
    let field = this.activeProcess.processFields.get(elementPath);
    return field ? field : null;
  }

  getElementsSeverity(pe: ProcessElement): number {
    var severity = 0;
    for( var msg of pe.messages ) {
      if( msg.messageLevel > severity ) {
        severity = msg.messageLevel;
      }
      if( severity >= MAX_SEVERITY ) {
        break
      }
    }
    return severity;
  }

  publishSeverityUpdate(viewName: string, severtity: number) {
    console.log("Update severity on view '"+viewName+" to "+severtity)
    this.viewSeverity$.next( {view: viewName, severity: severtity})
  }
}
