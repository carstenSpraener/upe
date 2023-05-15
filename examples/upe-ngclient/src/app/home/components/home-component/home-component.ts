import {Component, OnDestroy, OnInit} from "@angular/core";
import {UpeClientService} from "../../../upe-client/service/upe-client-service";
import {Subscription} from "rxjs";
import {ProcessField} from "../../../upe-client/service/upe-client-dto";

@Component({
    selector: "app-home",
    templateUrl: "home-component.html"
})
export class HomeComponent implements OnInit, OnDestroy {
    subscriptions: Subscription[] = [];
    processJson: string = "";
    fieldsJson: string = "";
    nameFieldJSON: string = "";
    streetFieldJSON: string = "";
    showProcesState: boolean = false;

    constructor(private upeSrv: UpeClientService) {
    }

    ngOnInit() {
        this.subscriptions.push(
            this.upeSrv.process$.subscribe(p => {
                if (!p) {
                    return;
                }
                this.processJson = JSON.stringify(p, null, 2);
                let fieldsJson = "";
                p?.processFields?.forEach(pf => {
                    fieldsJson += JSON.stringify(pf,this.noStreamsStringification, 2)
                })
                this.fieldsJson = fieldsJson;
                this.upeSrv.findProcessElementByPath("/person/name")?.update$.subscribe(b => {
                    if (b) {
                        this.nameFieldJSON =
                        JSON.stringify(this.upeSrv.findProcessElementByPath("/person/name"), this.noStreamsStringification);
                    }
                })
                this.upeSrv.findProcessElementByPath("/person/address/strasse")?.update$.subscribe(b => {
                    if (b) {
                        this.streetFieldJSON =
                            JSON.stringify(this.upeSrv.findProcessElementByPath("/person/address/strasse"), this.noStreamsStringification);
                    }
                })
            })
        )
    }

    noStreamsStringification(key: string, value: any): any {
            if( key.indexOf("$")>-1 ) {
                return "not stringified"
            }
            return value
    }
    ngOnDestroy() {
        for (let s of this.subscriptions) {
            s.unsubscribe()
        }
    }

    setName(value: string) {
        this.upeSrv.handleValueChange("/person/name", value);
    }

    toggleShowProcessState() {
        this.showProcesState = !this.showProcesState
    }
}
