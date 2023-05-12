import {Component, NgModule} from "@angular/core";
import {UpeClientService} from "./service/upe-client-service";
import {HttpClientModule} from "@angular/common/http";
import {CommonModule} from "@angular/common";
import { RouterModule, Routes} from "@angular/router";
import {UpeClientDirective} from "./components/upe-client-directive/upe-client-directive";
import {UpeFieldDirective} from "./components/upe-field/upe-field-directive";
import {UpeViewDirective} from "./components/upe-view/upe-view-directive";
import {UpeViewNavDirective} from "./components/upe-viewnav-directive/upe-viewnav-directive";

export const BG_COLORS: string[] = ["#FFFFFF", "#a3dba3", "#efef8d", "#ea9292"]


@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    //RouterModule.forChild(upeRoutes),
    RouterModule,
  ],
  declarations: [
    UpeClientDirective,
    UpeFieldDirective,
    UpeViewDirective,
    UpeViewNavDirective
  ],
  exports: [
    UpeClientDirective,
    UpeFieldDirective,
    UpeViewDirective,
    UpeViewNavDirective
  ],
  providers: [
    UpeClientService
  ]
})
export class UpeClientModule {
  constructor() {
  }

  static buildUPERoutes( app: any) : Routes {
    let routes : Routes = [
      {path: "upe", component: app},
      {path: "upe/d/:dialogID", component: app},
      {path: "upe/d/:dialogID/s/:stepCount", component: app},
      {path: "upe/d/:dialogID/s/:stepCount/v/:activeView", component: app},
    ]
    return routes
  }
}
