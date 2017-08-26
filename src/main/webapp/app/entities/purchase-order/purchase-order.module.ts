import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HoardSharedModule } from '../../shared';
import { HoardAdminModule } from '../../admin/admin.module';
import {
    PurchaseOrderService,
    PurchaseOrderPopupService,
    PurchaseOrderComponent,
    PurchaseOrderDetailComponent,
    PurchaseOrderDialogComponent,
    PurchaseOrderPopupComponent,
    PurchaseOrderDeletePopupComponent,
    PurchaseOrderDeleteDialogComponent,
    purchaseOrderRoute,
    purchaseOrderPopupRoute,
} from './';

const ENTITY_STATES = [
    ...purchaseOrderRoute,
    ...purchaseOrderPopupRoute,
];

@NgModule({
    imports: [
        HoardSharedModule,
        HoardAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PurchaseOrderComponent,
        PurchaseOrderDetailComponent,
        PurchaseOrderDialogComponent,
        PurchaseOrderDeleteDialogComponent,
        PurchaseOrderPopupComponent,
        PurchaseOrderDeletePopupComponent,
    ],
    entryComponents: [
        PurchaseOrderComponent,
        PurchaseOrderDialogComponent,
        PurchaseOrderPopupComponent,
        PurchaseOrderDeleteDialogComponent,
        PurchaseOrderDeletePopupComponent,
    ],
    providers: [
        PurchaseOrderService,
        PurchaseOrderPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HoardPurchaseOrderModule {}
