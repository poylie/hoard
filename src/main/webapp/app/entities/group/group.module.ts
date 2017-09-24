import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HoardSharedModule } from '../../shared';
import {
    GroupService,
    GroupPopupService,
    GroupComponent,
    GroupDetailComponent,
    GroupDialogComponent,
    GroupPopupComponent,
    GroupDeletePopupComponent,
    GroupDeleteDialogComponent,
    groupRoute,
    groupPopupRoute,
    GroupResolvePagingParams,
    GroupDashboardComponent,
} from './';
import { MdCardModule } from '@angular/material';

const ENTITY_STATES = [
    ...groupRoute,
    ...groupPopupRoute,
];

@NgModule({
    imports: [
        HoardSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true }),
        MdCardModule
    ],
    declarations: [
        GroupDetailComponent,
        GroupDialogComponent,
        GroupDeleteDialogComponent,
        GroupPopupComponent,
        GroupDeletePopupComponent,
        GroupDashboardComponent,
    ],
    entryComponents: [
        GroupDialogComponent,
        GroupPopupComponent,
        GroupDeleteDialogComponent,
        GroupDeletePopupComponent,
    ],
    providers: [
        GroupService,
        GroupPopupService,
        GroupResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HoardGroupModule { }
