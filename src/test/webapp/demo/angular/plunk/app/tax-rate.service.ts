import { Injectable } from '@angular/core';

@Injectable()
export class TaxRateService {
  getRate(rateName: string) { return 0.10; } // 10% everywhere
}


/*
Copyright 2016 Google Inc. All Rights Reserved.
Use of this source code is governed by an MIT-style license that
can be found in the LICENSE file at http://angular.io/license
*/