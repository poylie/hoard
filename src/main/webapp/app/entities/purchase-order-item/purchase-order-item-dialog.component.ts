import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { PurchaseOrderItem } from './purchase-order-item.model';
import { PurchaseOrderItemPopupService } from './purchase-order-item-popup.service';
import { PurchaseOrderItemService } from './purchase-order-item.service';
import { PurchaseOrder, PurchaseOrderService } from '../purchase-order';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-purchase-order-item-dialog',
    templateUrl: './purchase-order-item-dialog.component.html'
})
export class PurchaseOrderItemDialogComponent implements OnInit {

    purchaseOrderItem: PurchaseOrderItem;
    authorities: any[];
    isSaving: boolean;

    purchaseorders: PurchaseOrder[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private purchaseOrderItemService: PurchaseOrderItemService,
        private purchaseOrderService: PurchaseOrderService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.purchaseOrderService.query()
            .subscribe((res: ResponseWrapper) => { this.purchaseorders = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.purchaseOrderItem.id !== undefined) {
            this.subscribeToSaveResponse(
                this.purchaseOrderItemService.update(this.purchaseOrderItem));
        } else {
            this.subscribeToSaveResponse(
                this.purchaseOrderItemService.create(this.purchaseOrderItem));
        }
    }

    private subscribeToSaveResponse(result: Observable<PurchaseOrderItem>) {
        result.subscribe((res: PurchaseOrderItem) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: PurchaseOrderItem) {
        this.eventManager.broadcast({ name: 'purchaseOrderItemListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackPurchaseOrderById(index: number, item: PurchaseOrder) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-purchase-order-item-popup',
    template: ''
})
export class PurchaseOrderItemPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private purchaseOrderItemPopupService: PurchaseOrderItemPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.purchaseOrderItemPopupService
                    .open(PurchaseOrderItemDialogComponent, params['id']);
            } else {
                this.modalRef = this.purchaseOrderItemPopupService
                    .open(PurchaseOrderItemDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
