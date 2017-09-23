import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HoardSharedModule } from '../shared';

import { HOME_ROUTE, HomeComponent } from './';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MdTabsModule } from '@angular/material';
import { GroupComponent } from '../entities/group';

@NgModule({
    imports: [
        HoardSharedModule,
        RouterModule.forRoot([HOME_ROUTE], { useHash: true }),
        BrowserAnimationsModule,
        MdTabsModule
    ],
    declarations: [
        HomeComponent,
        GroupComponent
    ],
    entryComponents: [
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HoardHomeModule { }
