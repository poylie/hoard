import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { SalesOrderItemComponent } from './sales-order-item.component';
import { SalesOrderItemDetailComponent } from './sales-order-item-detail.component';
import { SalesOrderItemPopupComponent } from './sales-order-item-dialog.component';
import { SalesOrderItemDeletePopupComponent } from './sales-order-item-delete-dialog.component';

import { Principal } from '../../shared';

export const salesOrderItemRoute: Routes = [
    {
        path: 'sales-order-item',
        component: SalesOrderItemComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SalesOrderItems'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'sales-order-item/:id',
        component: SalesOrderItemDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SalesOrderItems'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const salesOrderItemPopupRoute: Routes = [
    {
        path: 'sales-order-item-new',
        component: SalesOrderItemPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SalesOrderItems'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sales-order-item/:id/edit',
        component: SalesOrderItemPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SalesOrderItems'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sales-order-item/:id/delete',
        component: SalesOrderItemDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SalesOrderItems'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
