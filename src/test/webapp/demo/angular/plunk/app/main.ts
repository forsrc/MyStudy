import { bootstrap }      from '@angular/platform-browser-dynamic';
import { AppComponent } from './app.component';
import { HeroService }    from './hero.service';
import { BackendService } from './backend.service';
import { Logger }         from './logger.service';

bootstrap(AppComponent, [BackendService, HeroService, Logger]);


/*
Copyright 2016 Google Inc. All Rights Reserved.
Use of this source code is governed by an MIT-style license that
can be found in the LICENSE file at http://angular.io/license
*/