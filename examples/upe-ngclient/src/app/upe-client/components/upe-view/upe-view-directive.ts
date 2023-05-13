import {AfterViewInit, Directive, ElementRef, Input, OnDestroy} from "@angular/core";
import {Subscription} from "rxjs";
import {UpeClientService} from "../../service/upe-client-service";
import {BG_COLORS, MAX_SEVERITY, ProcessDelta} from "../../service/upe-client-dto";

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
  severtity: number = -1;
  subscriptions: Subscription[] = [];

  constructor( private elRef: ElementRef,
               private upeSrv: UpeClientService
  ) {
    (elRef.nativeElement as HTMLElement).style.display = 'none';
    // Update the visibility of the view when global viewName changes.
    this.subscriptions.push(upeSrv.activeView$.subscribe((viewName: string | undefined) => {
      if (viewName && viewName == this.viewName) {
        (this.elRef.nativeElement as HTMLElement).style.display = this.visibleDisplay
      } else {
        (this.elRef.nativeElement as HTMLElement).style.display = 'none'
      }
    }))
    this.subscriptions.push(this.upeSrv.process$.subscribe(
      p => {
        if(p) {
          this.updateSeverity();
          console.log("View-Severity for view '"+this.viewName+"' is "+this.severtity);
        }
      })
    );

  }

  // Update the views severity to the maximum severity of all
  // contained fields.
  private updateSeverity() {
    let oldSeverity = this.severtity;
    this.severtity = 0
    for (let elementPath of this.subFields) {
      var pe = this.upeSrv.findProcessElementByPath(elementPath);
      if (pe) {
        let peSeverity = this.upeSrv.getElementsSeverity(pe);
        if (peSeverity > this.severtity) {
          this.severtity = peSeverity;
        }
        if( this.severtity >= MAX_SEVERITY ) {
          break;
        }
      }
    }
    let color = BG_COLORS[this.severtity];
    this.elRef.nativeElement.style.borderColor=color;
    if( this.severtity > 1 ) {
      this.elRef.nativeElement.style.borderStyle='solid'
      this.elRef.nativeElement.style.borderWidth=3
    } else {
      this.elRef.nativeElement.style.borderStyle='none'
      this.elRef.nativeElement.style.borderWidth=0
    }
    if( this.severtity != oldSeverity ) {
      this.upeSrv.publishSeverityUpdate( this.viewName, this.severtity );
    }
  }

  ngAfterViewInit(): void {
    // Collect the contained fields to provide a view severity level
    for (let e of this.elRef.nativeElement.children) {
      if (e.getAttribute("ng-reflect-upe-field")) {
        var fieldName = e.getAttribute("ng-reflect-upe-field");
        this.subFields.push(fieldName)
      }
    }
    this.updateSeverity()
  }

  ngOnDestroy(): void {
    for (let s of this.subscriptions) {
      s.unsubscribe();
    }
  }

}
