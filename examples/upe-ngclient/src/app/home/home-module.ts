import {NgModule} from "@angular/core";
import {HomeComponent} from "./components/home-component/home-component";
import {CommonModule} from "@angular/common";
import {RouterModule} from "@angular/router";
import {UpeClientModule} from "../upe-client/upe-client-module";

@NgModule({
  imports: [
    CommonModule,
    UpeClientModule,
    RouterModule
      .forChild(UpeClientModule.buildUPERoutes(HomeComponent))
  ],
  declarations: [
    HomeComponent
  ],
  exports: [
    HomeComponent
  ]
})
export class HomeModule {}
