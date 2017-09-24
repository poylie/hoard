import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HoardSharedModule } from '../../shared';
import { HoardAdminModule } from '../../admin/admin.module';
import {
    UserGroupService,
    UserGroupPopupService,
    UserGroupComponent,
    UserGroupDetailComponent,
    UserGroupDialogComponent,
    UserGroupPopupComponent,
    UserGroupDeletePopupComponent,
    UserGroupDeleteDialogComponent,
    userGroupRoute,
    userGroupPopupRoute,
    UserGroupResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...userGroupRoute,
    ...userGroupPopupRoute,
];

@NgModule({
    imports: [
        HoardSharedModule,
        HoardAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        UserGroupComponent,
        UserGroupDetailComponent,
        UserGroupDialogComponent,
        UserGroupDeleteDialogComponent,
        UserGroupPopupComponent,
        UserGroupDeletePopupComponent,
    ],
    entryComponents: [
        UserGroupComponent,
        UserGroupDialogComponent,
        UserGroupPopupComponent,
        UserGroupDeleteDialogComponent,
        UserGroupDeletePopupComponent,
    ],
    providers: [
        UserGroupService,
        UserGroupPopupService,
        UserGroupResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HoardUserGroupModule {}
