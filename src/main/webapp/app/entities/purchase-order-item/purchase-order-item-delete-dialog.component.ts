import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { PurchaseOrderItem } from './purchase-order-item.model';
import { PurchaseOrderItemPopupService } from './purchase-order-item-popup.service';
import { PurchaseOrderItemService } from './purchase-order-item.service';

@Component({
    selector: 'jhi-purchase-order-item-delete-dialog',
    templateUrl: './purchase-order-item-delete-dialog.component.html'
})
export class PurchaseOrderItemDeleteDialogComponent {

    purchaseOrderItem: PurchaseOrderItem;

    constructor(
        private purchaseOrderItemService: PurchaseOrderItemService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.purchaseOrderItemService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'purchaseOrderItemListModification',
                content: 'Deleted an purchaseOrderItem'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-purchase-order-item-delete-popup',
    template: ''
})
export class PurchaseOrderItemDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private purchaseOrderItemPopupService: PurchaseOrderItemPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.purchaseOrderItemPopupService
                .open(PurchaseOrderItemDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
