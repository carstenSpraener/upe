import {AfterViewInit, Directive, ElementRef, Input, OnDestroy} from "@angular/core";
import {UpeClientService} from "../../service/upe-client-service";
import {Subscription} from "rxjs";
import {MAX_SEVERITY, ProcessDelta} from "../../service/upe-client-dto";
import {BG_COLORS} from "../../upe-client-module";

@Directive({
  selector: "[upeView]"
})
export class UpeViewDirective implements OnDestroy, AfterViewInit {
  @Input('upeView')
  viewName: string = 'init';
  @Input('display')
  visibleDisplay: string = 'block'
  subFields: string[] = [];
  // this is the combined maximal severity of all included fields.
  severtity: number = 0;
  subscriptions: Subscription[] = [];

  constructor(private upeSrv: UpeClientService, private elRef: ElementRef) {
    (elRef.nativeElement as HTMLElement).style.display = 'none';
    // Update the visibility of the view when global viewName changes.
    this.subscriptions.push(upeSrv.activeView$.subscribe((viewName: string | undefined) => {
      if (viewName && viewName == this.viewName) {
        (this.elRef.nativeElement as HTMLElement).style.display = this.visibleDisplay
      } else {
        (this.elRef.nativeElement as HTMLElement).style.display = 'none'
      }
    }))
    this.subscriptions.push(this.upeSrv.processDelta$.subscribe(
      (pd: ProcessDelta |undefined) => {
        if( pd) {
          this.updateSeverity();
        }
      })
    );
  }

  // Update the views severity to the maximum severity of all
  // contained fields.
  private updateSeverity() {
    for (let elementPath of this.subFields) {
      var pe = this.upeSrv.findProcessElementByPath(elementPath);
      if (pe) {
        let peSeverity = this.upeSrv.getElementsSeverity(pe);
        if (peSeverity > this.severtity) {
          this.severtity = peSeverity;
        }
        if( this.severtity >= MAX_SEVERITY ) {
          return;
        }
      }
    }
    let color = BG_COLORS[this.severtity];
    this.elRef.nativeElement.style.background=color;
  }

  ngAfterViewInit(): void {
    // Collect the contained fields to provide a view severity level
    for (let e of this.elRef.nativeElement.children) {
      if (e.getAttribute("ng-reflect-upe-field")) {
        var fieldName = e.getAttribute("ng-reflect-upe-field");
        this.subFields.push(fieldName)
      }
    }
  }

  ngOnDestroy(): void {
    for (let s of this.subscriptions) {
      s.unsubscribe();
    }
  }

}
