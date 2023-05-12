import {AfterViewInit, Component, Directive, Input, OnDestroy, OnInit} from "@angular/core";
import {UpeClientService} from "../../service/upe-client-service";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {Process, ProcessDelta} from "../../service/upe-client-dto";
// @ts-ignore
import {Subscription} from "rxjs";

@Directive({
  selector: "[upeProcess]",
})
export class UpeClientDirective implements OnInit, OnDestroy, AfterViewInit {
  @Input('upeProcess')
  processName: string = '';
  activeProcess : Process | undefined
  subscriptions: Subscription[] = [];

  constructor( private activatedRoute: ActivatedRoute, private router: Router,private upeClientService: UpeClientService) {
  }

  ngAfterViewInit() {
    // console.log("UPEClientUPEClientComponent: processName is '"+this.processName+"'");
    this.subscriptions.push(this.activatedRoute.paramMap.subscribe(
      (params: ParamMap) => this.routeParamsChanged(params)
    ));
    /*
    this.subscriptions.push(this.upeClientService.process$.subscribe( (p:Process|undefined)=> {
      if( p ) {
        console.log("UPEClientComponent: new process '" + p.name + " 'loaded!")
      }
    }));
    this.subscriptions.push(this.upeClientService.processDelta$.subscribe( (p:ProcessDelta|undefined)=> {
      if( p ) {
        console.log("UPEClientComponent: new processDelta fired on process '"+p.processName+"'")
      }
    }));
    this.subscriptions.push(this.upeClientService.activeView$.subscribe( (viewName:string|undefined)=> {
      if( viewName ) {
        console.log("UPEClientComponent: new view name fired '"+viewName+"'")
      }
    }));

     */
  }

  ngOnInit() {
  }

  ngOnDestroy() {
    for( let s of this.subscriptions ) {
      s.unsubscribe();
    }
  }

  private routeParamsChanged(params: ParamMap) {
    /*
    for( let key of params.keys) {
      console.log( "params['"+key+"'] := '"+params.get(key)+"'")
    }
    */
    var processName = this.processName;
    if(params.get('process') && params.get('process') != "") {
      processName = params.get('process') as string;
    }
    this.upeClientService.loadProcess({
      processName: processName,
      dialogID: params.get('dialogID'),
      stepCount: params.get('stepCount') as number|null,
      activeView: params.get('activeView')
    } )
  }

  setName() {
    this.upeClientService.handleValueChange(
      "/name",
      "Carsten"
    );
  }

  setStrasse() {
    this.upeClientService.handleValueChange(
      "/adress/strasse",
      "Kirchesch 6"
    );
  }
}
