import {BehaviorSubject, Observable} from "rxjs";

export interface ProcessElementListener {
  updateFromModel(element: ProcessElement): void;
}

export interface DialogState {
  processName: string | null;
  dialogID: string | null;
  stepCount: number | null;
  activeView: string | null;
}

export interface ProcessMessage {
  messageID: string
  messageText: string
  messageLevel: number
}

export interface ProcessElementDelta {
  elementPath: string;
  isVisible: boolean;
  enabled: boolean;
  valueForFrontend: string;
  newMessages: ProcessMessage[]
  removedMessages: ProcessMessage[];
  severity: number
}

export interface ProcessDelta {
  state: DialogState
  processName: string
  elementDeltaList: ProcessElementDelta[]
}

export interface UpeValueUpdate {
  dialogID: string | null;
  step: number | null;
  fieldPath: string;
  newValue: string;
}

export const MAX_SEVERITY = 3;

export const BG_COLORS: string[] = ["#FFFFFF", "#a3dba3", "#efef8d", "#ea9292"]

export interface ProcessElement {
  isVisibel: boolean
  isEnabled: boolean
  fieldPath: string
  messages: ProcessMessage[]
  update$: BehaviorSubject<boolean>;
}

export interface ProcessField extends ProcessElement {
  value: string;
}

export interface Process {
  state: DialogState
  elementListnerMap: Map<string, ProcessElementListener[]>;
  name: string
  processFields: Map<string,ProcessField>;
}

