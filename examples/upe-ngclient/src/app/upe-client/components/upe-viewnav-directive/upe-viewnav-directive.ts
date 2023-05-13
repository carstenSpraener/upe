import {Directive, ElementRef, Input} from "@angular/core";
import {UpeClientService} from "../../service/upe-client-service";
import {Subscription} from "rxjs";
import {BG_COLORS} from "../../service/upe-client-dto";

@Directive({
  selector: "[upeViewNav]"
})
export class UpeViewNavDirective {
  @Input('upeViewNav')
  targetView: string = "init";
  priviousView : string;
  subscriptions : Subscription[] = []

  constructor(private upeClientSrv: UpeClientService,
              private elRef: ElementRef
  ) {
    this.priviousView = this.upeClientSrv.activeView;
    elRef.nativeElement.onclick = (evt: any)=> {
      if( this.targetView=="_back") {
        this.upeClientSrv.changeView(this.priviousView);
      } else {
        this.upeClientSrv.changeView(this.targetView);
      }
    }
    this.subscriptions.push(
        this.upeClientSrv.viewSeverity$.subscribe( change=> {
          if( change.view == this.targetView ) {
            this.updateSeverity(change.severity)
          }
        })
    )
  }

  private updateSeverity(severity: number) {
    this.elRef.nativeElement.style.background = BG_COLORS[severity];
  }
}
