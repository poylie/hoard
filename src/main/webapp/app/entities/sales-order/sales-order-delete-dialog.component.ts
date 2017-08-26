import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SalesOrder } from './sales-order.model';
import { SalesOrderPopupService } from './sales-order-popup.service';
import { SalesOrderService } from './sales-order.service';

@Component({
    selector: 'jhi-sales-order-delete-dialog',
    templateUrl: './sales-order-delete-dialog.component.html'
})
export class SalesOrderDeleteDialogComponent {

    salesOrder: SalesOrder;

    constructor(
        private salesOrderService: SalesOrderService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.salesOrderService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'salesOrderListModification',
                content: 'Deleted an salesOrder'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sales-order-delete-popup',
    template: ''
})
export class SalesOrderDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private salesOrderPopupService: SalesOrderPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.salesOrderPopupService
                .open(SalesOrderDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
