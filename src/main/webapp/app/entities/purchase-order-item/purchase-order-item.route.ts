import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PurchaseOrderItemComponent } from './purchase-order-item.component';
import { PurchaseOrderItemDetailComponent } from './purchase-order-item-detail.component';
import { PurchaseOrderItemPopupComponent } from './purchase-order-item-dialog.component';
import { PurchaseOrderItemDeletePopupComponent } from './purchase-order-item-delete-dialog.component';

import { Principal } from '../../shared';

export const purchaseOrderItemRoute: Routes = [
    {
        path: 'purchase-order-item',
        component: PurchaseOrderItemComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'PurchaseOrderItems'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'purchase-order-item/:id',
        component: PurchaseOrderItemDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'PurchaseOrderItems'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const purchaseOrderItemPopupRoute: Routes = [
    {
        path: 'purchase-order-item-new',
        component: PurchaseOrderItemPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'PurchaseOrderItems'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'purchase-order-item/:id/edit',
        component: PurchaseOrderItemPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'PurchaseOrderItems'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'purchase-order-item/:id/delete',
        component: PurchaseOrderItemDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'PurchaseOrderItems'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
