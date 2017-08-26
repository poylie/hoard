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
} from './';

const ENTITY_STATES = [
    ...groupRoute,
    ...groupPopupRoute,
];

@NgModule({
    imports: [
        HoardSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        GroupComponent,
        GroupDetailComponent,
        GroupDialogComponent,
        GroupDeleteDialogComponent,
        GroupPopupComponent,
        GroupDeletePopupComponent,
    ],
    entryComponents: [
        GroupComponent,
        GroupDialogComponent,
        GroupPopupComponent,
        GroupDeleteDialogComponent,
        GroupDeletePopupComponent,
    ],
    providers: [
        GroupService,
        GroupPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HoardGroupModule {}
