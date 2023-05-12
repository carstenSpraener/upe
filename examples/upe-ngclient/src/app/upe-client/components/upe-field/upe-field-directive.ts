import {AfterViewInit, Directive, ElementRef, Input, OnDestroy, OnInit} from "@angular/core";
import {UpeClientService} from "../../service/upe-client-service";
import {Subscription} from "rxjs";
import {ProcessDelta, ProcessElementDelta, ProcessField, ProcessMessage} from "../../service/upe-client-dto";
import {BG_COLORS} from "../../upe-client-module";

@Directive({
  selector: "[upeField]"
})
export class UpeFieldDirective implements OnInit, AfterViewInit, OnDestroy {
  @Input('upeField')
  upeField: string = "";
  @Input('display')
  visibleDisplay : string = 'block'

  subscriptions: Subscription[] = [];
  messages : ProcessMessage[] = [];
  isVisible = true
  isEnabled = true;
  value : string = "";
  severity =0;

  constructor(private elRef: ElementRef,
              private upeSrv : UpeClientService,
              ) {
    this.subscriptions.push(
      this.upeSrv.processDelta$.subscribe(
        (pDelta: ProcessDelta|undefined) => {
          if( pDelta ) {
            this.handleDelta(pDelta)
          }
        })
    )
  }

  ngOnInit() {
  }

  ngAfterViewInit(): void {
    console.log("UPEField "+this.upeField+".parent = "+this.elRef.nativeElement.parent);
    if( this.elRef.nativeElement instanceof HTMLInputElement ) {
      var htmlInput = this.elRef.nativeElement as HTMLInputElement;
      htmlInput.onchange = (evt) => {
        this.upeSrv.handleValueChange( this.upeField, htmlInput.value);
      }
    }
    var pFields = this.upeSrv.activeProcess?.processFields;
    if( !pFields ) {
      return
    }
    for( let pf of pFields ) {
      if( pf.fieldPath == this.upeField ) {
        this.setFieldState(pf);
      }
    }
  }

  ngOnDestroy() {
    for( let s of this.subscriptions ) {
      s.unsubscribe();
    }
  }

  handleDelta(pDelta: ProcessDelta) {
    var elementDeltas = pDelta.elementDeltaList;
    if( elementDeltas ) {
      for( let eD of elementDeltas ) {
        if( eD.elementPath == this.upeField ) {
          this.setFieldStateFromDelta(eD);
        }
      }
    }
  }

  private setFieldStateFromDelta(pD: ProcessElementDelta) {
    this.severity = 0;
    this.messages = []
    var newMessages : ProcessMessage[] = []
    for( let myMsg of this.messages ) {
      var isRemoved = false;
      for( let removedMsg of pD.removedMessages ) {
        if( myMsg.messageID == removedMsg.messageID ) {
          isRemoved = true;
          break;
        }
      }
      if( !isRemoved ) {
        newMessages.push(myMsg)
      }
    }
    if( pD.newMessages ) {
      newMessages.push(...(pD.newMessages));
    }
    this.messages = newMessages;
    for( let msg of this.messages ) {
      if( msg.messageLevel > this.severity ) {
        this.severity = msg.messageLevel;
      }
    }

    if( pD.enabled ) {this.isEnabled = pD.enabled;}
    if( pD.isVisible !== undefined ) {this.isVisible = pD.isVisible};
    this.value = pD.valueForFrontend;
    this.updateHtmlElement();
  }

  private setFieldState(pf: ProcessField) {
    this.severity = 0;
    this.messages = []
    for( let msg of pf.messages ) {
      this.messages.push(msg)
      if( msg.messageLevel > this.severity ) {
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
    if( html instanceof HTMLInputElement ) {
      (html as HTMLInputElement).value = this.value ? this.value : '';
    }
  }
}
