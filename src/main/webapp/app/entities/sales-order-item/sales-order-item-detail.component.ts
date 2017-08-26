import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { SalesOrderItem } from './sales-order-item.model';
import { SalesOrderItemService } from './sales-order-item.service';

@Component({
    selector: 'jhi-sales-order-item-detail',
    templateUrl: './sales-order-item-detail.component.html'
})
export class SalesOrderItemDetailComponent implements OnInit, OnDestroy {

    salesOrderItem: SalesOrderItem;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private salesOrderItemService: SalesOrderItemService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSalesOrderItems();
    }

    load(id) {
        this.salesOrderItemService.find(id).subscribe((salesOrderItem) => {
            this.salesOrderItem = salesOrderItem;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSalesOrderItems() {
        this.eventSubscriber = this.eventManager.subscribe(
            'salesOrderItemListModification',
            (response) => this.load(this.salesOrderItem.id)
        );
    }
}
