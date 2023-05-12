import {Directive, ElementRef, Input} from "@angular/core";
import {UpeClientService} from "../../service/upe-client-service";
import {Location} from "@angular/common";

@Directive({
  selector: "[upeViewNav]"
})
export class UpeViewNavDirective {
  @Input('upeViewNav')
  targetView: string = "init";
  priviousView : string;
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
  }
}
