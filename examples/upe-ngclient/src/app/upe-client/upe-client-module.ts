import {NgModule} from "@angular/core";
import {UpeClientService} from "./service/upe-client-service";
import {HttpClientModule} from "@angular/common/http";
import {CommonModule} from "@angular/common";
import {RouterModule, Routes} from "@angular/router";
import {UpeViewDirective} from "./components/upe-view/upe-view-directive";
import {UpeFieldDirective} from "./components/upe-field/upe-field-directive";
import {UpeViewNavDirective} from "./components/upe-viewnav-directive/upe-viewnav-directive";
import {UpeClientDirective} from "./components/upe-client-directive/upe-client-directive";

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    RouterModule,
  ],
  declarations: [
    UpeViewNavDirective,
    UpeViewDirective,
    UpeClientDirective,
    UpeFieldDirective,
  ],
  exports: [
    UpeViewNavDirective,
    UpeViewDirective,
    UpeClientDirective,
    UpeFieldDirective,
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
