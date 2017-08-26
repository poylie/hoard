import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiAlertService } from 'ng-jhipster';

import { SalesOrderItem } from './sales-order-item.model';
import { SalesOrderItemService } from './sales-order-item.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-sales-order-item',
    templateUrl: './sales-order-item.component.html'
})
export class SalesOrderItemComponent implements OnInit, OnDestroy {
salesOrderItems: SalesOrderItem[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private salesOrderItemService: SalesOrderItemService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.salesOrderItemService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.salesOrderItems = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.salesOrderItemService.query().subscribe(
            (res: ResponseWrapper) => {
                this.salesOrderItems = res.json;
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
        this.registerChangeInSalesOrderItems();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: SalesOrderItem) {
        return item.id;
    }
    registerChangeInSalesOrderItems() {
        this.eventSubscriber = this.eventManager.subscribe('salesOrderItemListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
