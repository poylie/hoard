import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { HoardGroupModule } from './group/group.module';
import { HoardUserGroupModule } from './user-group/user-group.module';
import { HoardProductModule } from './product/product.module';
import { HoardPurchaseOrderModule } from './purchase-order/purchase-order.module';
import { HoardPurchaseOrderItemModule } from './purchase-order-item/purchase-order-item.module';
import { HoardSalesOrderModule } from './sales-order/sales-order.module';
import { HoardSalesOrderItemModule } from './sales-order-item/sales-order-item.module';
import { HoardInvitationModule } from './invitation/invitation.module';
import { HoardRequestModule } from './request/request.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        HoardGroupModule,
        HoardUserGroupModule,
        HoardProductModule,
        HoardPurchaseOrderModule,
        HoardPurchaseOrderItemModule,
        HoardSalesOrderModule,
        HoardSalesOrderItemModule,
        HoardInvitationModule,
        HoardRequestModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HoardEntityModule {}
