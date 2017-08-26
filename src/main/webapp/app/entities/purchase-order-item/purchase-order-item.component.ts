import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiAlertService } from 'ng-jhipster';

import { PurchaseOrderItem } from './purchase-order-item.model';
import { PurchaseOrderItemService } from './purchase-order-item.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-purchase-order-item',
    templateUrl: './purchase-order-item.component.html'
})
export class PurchaseOrderItemComponent implements OnInit, OnDestroy {
purchaseOrderItems: PurchaseOrderItem[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private purchaseOrderItemService: PurchaseOrderItemService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.purchaseOrderItemService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.purchaseOrderItems = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.purchaseOrderItemService.query().subscribe(
            (res: ResponseWrapper) => {
                this.purchaseOrderItems = res.json;
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
        this.registerChangeInPurchaseOrderItems();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: PurchaseOrderItem) {
        return item.id;
    }
    registerChangeInPurchaseOrderItems() {
        this.eventSubscriber = this.eventManager.subscribe('purchaseOrderItemListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
