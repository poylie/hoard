import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { PurchaseOrderItem } from './purchase-order-item.model';
import { PurchaseOrderItemService } from './purchase-order-item.service';

@Component({
    selector: 'jhi-purchase-order-item-detail',
    templateUrl: './purchase-order-item-detail.component.html'
})
export class PurchaseOrderItemDetailComponent implements OnInit, OnDestroy {

    purchaseOrderItem: PurchaseOrderItem;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private purchaseOrderItemService: PurchaseOrderItemService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPurchaseOrderItems();
    }

    load(id) {
        this.purchaseOrderItemService.find(id).subscribe((purchaseOrderItem) => {
            this.purchaseOrderItem = purchaseOrderItem;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPurchaseOrderItems() {
        this.eventSubscriber = this.eventManager.subscribe(
            'purchaseOrderItemListModification',
            (response) => this.load(this.purchaseOrderItem.id)
        );
    }
}
