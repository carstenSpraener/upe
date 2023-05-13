import {NgModule} from "@angular/core";
import {HomeComponent} from "./components/home-component/home-component";
import {CommonModule} from "@angular/common";
import {UpeClientModule} from "../upe-client/upe-client-module";
import {RouterModule} from "@angular/router";

@NgModule({
  imports: [
    CommonModule,
    UpeClientModule,
    RouterModule
      .forRoot(UpeClientModule.buildUPERoutes(HomeComponent))
  ],
  declarations: [
    HomeComponent
  ],
  exports: [
    HomeComponent
  ]
})
export class HomeModule {}
