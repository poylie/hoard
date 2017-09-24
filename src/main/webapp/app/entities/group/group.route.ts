import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { GroupComponent } from './group.component';
import { GroupDetailComponent } from './group-detail.component';
import { GroupPopupComponent } from './group-dialog.component';
import { GroupDashboardComponent } from './group-dashboard.component';
import { GroupDeletePopupComponent } from './group-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class GroupResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const groupRoute: Routes = [
    {
        path: 'group',
        component: GroupComponent,
        resolve: {
            'pagingParams': GroupResolvePagingParams
        },
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
    }, {
        path: 'groupDashboard/:id',
        component: GroupDashboardComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Group'
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
