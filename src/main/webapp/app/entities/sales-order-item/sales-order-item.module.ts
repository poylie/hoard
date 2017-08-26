import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HoardSharedModule } from '../../shared';
import {
    SalesOrderItemService,
    SalesOrderItemPopupService,
    SalesOrderItemComponent,
    SalesOrderItemDetailComponent,
    SalesOrderItemDialogComponent,
    SalesOrderItemPopupComponent,
    SalesOrderItemDeletePopupComponent,
    SalesOrderItemDeleteDialogComponent,
    salesOrderItemRoute,
    salesOrderItemPopupRoute,
} from './';

const ENTITY_STATES = [
    ...salesOrderItemRoute,
    ...salesOrderItemPopupRoute,
];

@NgModule({
    imports: [
        HoardSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        SalesOrderItemComponent,
        SalesOrderItemDetailComponent,
        SalesOrderItemDialogComponent,
        SalesOrderItemDeleteDialogComponent,
        SalesOrderItemPopupComponent,
        SalesOrderItemDeletePopupComponent,
    ],
    entryComponents: [
        SalesOrderItemComponent,
        SalesOrderItemDialogComponent,
        SalesOrderItemPopupComponent,
        SalesOrderItemDeleteDialogComponent,
        SalesOrderItemDeletePopupComponent,
    ],
    providers: [
        SalesOrderItemService,
        SalesOrderItemPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HoardSalesOrderItemModule {}
