import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PurchaseOrderComponent } from './purchase-order.component';
import { PurchaseOrderDetailComponent } from './purchase-order-detail.component';
import { PurchaseOrderPopupComponent } from './purchase-order-dialog.component';
import { PurchaseOrderDeletePopupComponent } from './purchase-order-delete-dialog.component';

import { Principal } from '../../shared';

export const purchaseOrderRoute: Routes = [
    {
        path: 'purchase-order',
        component: PurchaseOrderComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'PurchaseOrders'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'purchase-order/:id',
        component: PurchaseOrderDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'PurchaseOrders'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const purchaseOrderPopupRoute: Routes = [
    {
        path: 'purchase-order-new',
        component: PurchaseOrderPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'PurchaseOrders'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'purchase-order/:id/edit',
        component: PurchaseOrderPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'PurchaseOrders'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'purchase-order/:id/delete',
        component: PurchaseOrderDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'PurchaseOrders'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
