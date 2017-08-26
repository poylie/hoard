import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { GroupComponent } from './group.component';
import { GroupDetailComponent } from './group-detail.component';
import { GroupPopupComponent } from './group-dialog.component';
import { GroupDeletePopupComponent } from './group-delete-dialog.component';

import { Principal } from '../../shared';

export const groupRoute: Routes = [
    {
        path: 'group',
        component: GroupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Groups'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'group/:id',
        component: GroupDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Groups'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const groupPopupRoute: Routes = [
    {
        path: 'group-new',
        component: GroupPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Groups'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'group/:id/edit',
        component: GroupPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Groups'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'group/:id/delete',
        component: GroupDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Groups'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
