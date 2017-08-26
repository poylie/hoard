import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SalesOrderItem } from './sales-order-item.model';
import { SalesOrderItemPopupService } from './sales-order-item-popup.service';
import { SalesOrderItemService } from './sales-order-item.service';
import { SalesOrder, SalesOrderService } from '../sales-order';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-sales-order-item-dialog',
    templateUrl: './sales-order-item-dialog.component.html'
})
export class SalesOrderItemDialogComponent implements OnInit {

    salesOrderItem: SalesOrderItem;
    authorities: any[];
    isSaving: boolean;

    salesorders: SalesOrder[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private salesOrderItemService: SalesOrderItemService,
        private salesOrderService: SalesOrderService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.salesOrderService.query()
            .subscribe((res: ResponseWrapper) => { this.salesorders = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.salesOrderItem.id !== undefined) {
            this.subscribeToSaveResponse(
                this.salesOrderItemService.update(this.salesOrderItem));
        } else {
            this.subscribeToSaveResponse(
                this.salesOrderItemService.create(this.salesOrderItem));
        }
    }

    private subscribeToSaveResponse(result: Observable<SalesOrderItem>) {
        result.subscribe((res: SalesOrderItem) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: SalesOrderItem) {
        this.eventManager.broadcast({ name: 'salesOrderItemListModification', content: 'OK'});
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

    trackSalesOrderById(index: number, item: SalesOrder) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-sales-order-item-popup',
    template: ''
})
export class SalesOrderItemPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private salesOrderItemPopupService: SalesOrderItemPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.salesOrderItemPopupService
                    .open(SalesOrderItemDialogComponent, params['id']);
            } else {
                this.modalRef = this.salesOrderItemPopupService
                    .open(SalesOrderItemDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
