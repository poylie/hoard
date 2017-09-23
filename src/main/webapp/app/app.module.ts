import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ng2-webstorage';

import { HoardSharedModule, UserRouteAccessService } from './shared';
import { HoardHomeModule } from './home/home.module';
import { HoardAdminModule } from './admin/admin.module';
import { HoardAccountModule } from './account/account.module';
import { HoardEntityModule } from './entities/entity.module';
import { SidebarModule } from 'ng-sidebar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MdTabsModule } from '@angular/material';

import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';

// jhipster-needle-angular-add-module-import JHipster will add new module here

import {
    JhiMainComponent,
    LayoutRoutingModule,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ErrorComponent
} from './layouts';

import { JhipsterAboutUsModule } from './about-us/about-us.module';
import { JhipsterDocumentModule } from './document/document.module';
@NgModule({
    imports: [
        JhipsterDocumentModule,
        JhipsterAboutUsModule,
        BrowserModule,
        LayoutRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-' }),
        HoardSharedModule,
        HoardHomeModule,
        HoardAdminModule,
        HoardAccountModule,
        HoardEntityModule,
        SidebarModule.forRoot(),
        MdTabsModule,
        BrowserAnimationsModule
        // jhipster-needle-angular-add-module JHipster will add new module here
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        FooterComponent,
    ],
    providers: [
        ProfileService,
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService
    ],
    bootstrap: [JhiMainComponent]
})
export class HoardAppModule { }
