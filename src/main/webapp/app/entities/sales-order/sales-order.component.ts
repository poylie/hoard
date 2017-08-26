import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiAlertService } from 'ng-jhipster';

import { SalesOrder } from './sales-order.model';
import { SalesOrderService } from './sales-order.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-sales-order',
    templateUrl: './sales-order.component.html'
})
export class SalesOrderComponent implements OnInit, OnDestroy {
salesOrders: SalesOrder[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private salesOrderService: SalesOrderService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.salesOrderService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.salesOrders = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.salesOrderService.query().subscribe(
            (res: ResponseWrapper) => {
                this.salesOrders = res.json;
                this.currentSearch = '';
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInSalesOrders();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: SalesOrder) {
        return item.id;
    }
    registerChangeInSalesOrders() {
        this.eventSubscriber = this.eventManager.subscribe('salesOrderListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
