import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SalesOrderItem } from './sales-order-item.model';
import { SalesOrderItemPopupService } from './sales-order-item-popup.service';
import { SalesOrderItemService } from './sales-order-item.service';

@Component({
    selector: 'jhi-sales-order-item-delete-dialog',
    templateUrl: './sales-order-item-delete-dialog.component.html'
})
export class SalesOrderItemDeleteDialogComponent {

    salesOrderItem: SalesOrderItem;

    constructor(
        private salesOrderItemService: SalesOrderItemService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.salesOrderItemService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'salesOrderItemListModification',
                content: 'Deleted an salesOrderItem'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sales-order-item-delete-popup',
    template: ''
})
export class SalesOrderItemDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private salesOrderItemPopupService: SalesOrderItemPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.salesOrderItemPopupService
                .open(SalesOrderItemDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
