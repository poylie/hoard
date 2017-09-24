import { Route } from '@angular/router';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';
import { Injectable } from '@angular/core';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../shared';
import { HomeComponent } from './';

@Injectable()
export class HomeGroupResolvePagingParams implements Resolve<any> {

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

export const HOME_ROUTE: Route = {
    path: '',
    component: HomeComponent,
    resolve: {
        'pagingParams': HomeGroupResolvePagingParams
    },
    data: {
        authorities: [],
        pageTitle: 'Hoard'
    }
};
