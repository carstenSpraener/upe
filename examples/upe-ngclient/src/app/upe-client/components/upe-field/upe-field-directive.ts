import {Directive, ElementRef, Input, OnDestroy, OnInit} from "@angular/core";
import {Subscription} from "rxjs";
import {
    BG_COLORS,
    Process,
    ProcessDelta,
    ProcessElementDelta,
    ProcessField,
    ProcessMessage
} from "../../service/upe-client-dto";
import {UpeClientService} from "../../service/upe-client-service";

@Directive({
    selector: "[upeField]"
})
export class UpeFieldDirective implements OnInit, OnDestroy {
    @Input('upeField')
    upeField: string = "";
    @Input('display')
    visibleDisplay: string = 'block'

    subscriptions: Subscription[] = [];
    messages: ProcessMessage[] = [];
    isVisible = true
    isEnabled = true;
    value: string = "";
    severity = 0;
    myProcess: Process | null = null;

    constructor(private elRef: ElementRef,
                private upeSrv: UpeClientService,
    ) {
    }

    ngOnInit() {
        this.upeSrv.process$.subscribe(p => {
            if (p && p != this.myProcess) {
                for (let s of this.subscriptions) {
                    s.unsubscribe();
                }
                this.myProcess = p;
                this.bindToProcess(p);
            }
        });
    }

    bindToProcess(p: Process): void {
        if (this.elRef.nativeElement instanceof HTMLInputElement) {
            var htmlInput = this.elRef.nativeElement as HTMLInputElement;
            htmlInput.onchange = (evt) => {
                this.upeSrv.handleValueChange(this.upeField, htmlInput.value);
            }
        }

        var pField = p.processFields.get(this.upeField);
        if (!pField) {
            return
        }
        this.setFieldState(pField);
        this.subscriptions.push(pField.update$.subscribe(
            b => {
                if (b) {
                    this.setFieldState(this.upeSrv.findProcessElementByPath(this.upeField) as ProcessField);
                }
            }
        ));
    }

    ngOnDestroy() {
        for (let s of this.subscriptions) {
            s.unsubscribe();
        }
    }

    handleDelta(pDelta: ProcessDelta) {
        var elementDeltas = pDelta.elementDeltaList;
        if (elementDeltas) {
            for (let eD of elementDeltas) {
                if (eD.elementPath == this.upeField) {
                    this.setFieldStateFromDelta(eD);
                }
            }
        }
    }

    private setFieldStateFromDelta(pD: ProcessElementDelta) {
        this.severity = 0;
        this.messages = []
        var newMessages: ProcessMessage[] = []
        for (let myMsg of this.messages) {
            var isRemoved = false;
            for (let removedMsg of pD.removedMessages) {
                if (myMsg.messageID == removedMsg.messageID) {
                    isRemoved = true;
                    break;
                }
            }
            if (!isRemoved) {
                newMessages.push(myMsg)
            }
        }
        if (pD.newMessages) {
            newMessages.push(...(pD.newMessages));
        }
        this.messages = newMessages;
        for (let msg of this.messages) {
            if (msg.messageLevel > this.severity) {
                this.severity = msg.messageLevel;
            }
        }

        if (pD.enabled) {
            this.isEnabled = pD.enabled;
        }
        if (pD.isVisible !== undefined) {
            this.isVisible = pD.isVisible
        }
        ;
        this.value = pD.valueForFrontend;
        this.updateHtmlElement();
    }

    private setFieldState(pf: ProcessField) {
        this.severity = 0;
        this.messages = []
        for (let msg of pf.messages) {
            this.messages.push(msg)
            if (msg.messageLevel > this.severity) {
                this.severity = msg.messageLevel;
            }
        }
        this.isVisible = pf.isVisibel;
        this.isEnabled = pf.isEnabled;
        this.value = pf.value;
        this.updateHtmlElement();
    }

    private updateHtmlElement() {
        var html = this.elRef.nativeElement as HTMLElement;
        html.style.display = this.isVisible ? this.visibleDisplay : 'none'
        html.setAttribute('enabled', this.isEnabled ? 'true' : 'false')
        html.style.background = BG_COLORS[this.severity];
        if (html instanceof HTMLInputElement) {
            (html as HTMLInputElement).value = this.value ? this.value : '';
        }
    }
}
