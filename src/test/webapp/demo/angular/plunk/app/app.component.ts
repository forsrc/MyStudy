import { Component } from '@angular/core';
import { HeroListComponent } from './hero-list.component';
import { SalesTaxComponent } from './sales-tax.component';

@Component({
  selector: 'my-app',
  template: `
  <hero-list></hero-list>
  <sales-tax></sales-tax>
  `,
  directives: [HeroListComponent, SalesTaxComponent]
})
export class AppComponent { }


/*
Copyright 2016 Google Inc. All Rights Reserved.
Use of this source code is governed by an MIT-style license that
can be found in the LICENSE file at http://angular.io/license
*/