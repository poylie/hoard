import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { InvitationComponent } from './invitation.component';
import { InvitationDetailComponent } from './invitation-detail.component';
import { InvitationPopupComponent } from './invitation-dialog.component';
import { InvitationDeletePopupComponent } from './invitation-delete-dialog.component';

import { Principal } from '../../shared';

export const invitationRoute: Routes = [
    {
        path: 'invitation',
        component: InvitationComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Invitations'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'invitation/:id',
        component: InvitationDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Invitations'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const invitationPopupRoute: Routes = [
    {
        path: 'invitation-new',
        component: InvitationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Invitations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'invitation/:id/edit',
        component: InvitationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Invitations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'invitation/:id/delete',
        component: InvitationDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Invitations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
