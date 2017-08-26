import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HoardSharedModule } from '../../shared';
import {
    PurchaseOrderItemService,
    PurchaseOrderItemPopupService,
    PurchaseOrderItemComponent,
    PurchaseOrderItemDetailComponent,
    PurchaseOrderItemDialogComponent,
    PurchaseOrderItemPopupComponent,
    PurchaseOrderItemDeletePopupComponent,
    PurchaseOrderItemDeleteDialogComponent,
    purchaseOrderItemRoute,
    purchaseOrderItemPopupRoute,
} from './';

const ENTITY_STATES = [
    ...purchaseOrderItemRoute,
    ...purchaseOrderItemPopupRoute,
];

@NgModule({
    imports: [
        HoardSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PurchaseOrderItemComponent,
        PurchaseOrderItemDetailComponent,
        PurchaseOrderItemDialogComponent,
        PurchaseOrderItemDeleteDialogComponent,
        PurchaseOrderItemPopupComponent,
        PurchaseOrderItemDeletePopupComponent,
    ],
    entryComponents: [
        PurchaseOrderItemComponent,
        PurchaseOrderItemDialogComponent,
        PurchaseOrderItemPopupComponent,
        PurchaseOrderItemDeleteDialogComponent,
        PurchaseOrderItemDeletePopupComponent,
    ],
    providers: [
        PurchaseOrderItemService,
        PurchaseOrderItemPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HoardPurchaseOrderItemModule {}
