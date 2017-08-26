import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HoardSharedModule } from '../../shared';
import { HoardAdminModule } from '../../admin/admin.module';
import {
    SalesOrderService,
    SalesOrderPopupService,
    SalesOrderComponent,
    SalesOrderDetailComponent,
    SalesOrderDialogComponent,
    SalesOrderPopupComponent,
    SalesOrderDeletePopupComponent,
    SalesOrderDeleteDialogComponent,
    salesOrderRoute,
    salesOrderPopupRoute,
} from './';

const ENTITY_STATES = [
    ...salesOrderRoute,
    ...salesOrderPopupRoute,
];

@NgModule({
    imports: [
        HoardSharedModule,
        HoardAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        SalesOrderComponent,
        SalesOrderDetailComponent,
        SalesOrderDialogComponent,
        SalesOrderDeleteDialogComponent,
        SalesOrderPopupComponent,
        SalesOrderDeletePopupComponent,
    ],
    entryComponents: [
        SalesOrderComponent,
        SalesOrderDialogComponent,
        SalesOrderPopupComponent,
        SalesOrderDeleteDialogComponent,
        SalesOrderDeletePopupComponent,
    ],
    providers: [
        SalesOrderService,
        SalesOrderPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HoardSalesOrderModule {}
