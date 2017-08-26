import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SalesOrder } from './sales-order.model';
import { SalesOrderPopupService } from './sales-order-popup.service';
import { SalesOrderService } from './sales-order.service';
import { User, UserService } from '../../shared';
import { Group, GroupService } from '../group';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-sales-order-dialog',
    templateUrl: './sales-order-dialog.component.html'
})
export class SalesOrderDialogComponent implements OnInit {

    salesOrder: SalesOrder;
    authorities: any[];
    isSaving: boolean;

    users: User[];

    groups: Group[];
    transactionDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private salesOrderService: SalesOrderService,
        private userService: UserService,
        private groupService: GroupService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.groupService.query()
            .subscribe((res: ResponseWrapper) => { this.groups = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.salesOrder.id !== undefined) {
            this.subscribeToSaveResponse(
                this.salesOrderService.update(this.salesOrder));
        } else {
            this.subscribeToSaveResponse(
                this.salesOrderService.create(this.salesOrder));
        }
    }

    private subscribeToSaveResponse(result: Observable<SalesOrder>) {
        result.subscribe((res: SalesOrder) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: SalesOrder) {
        this.eventManager.broadcast({ name: 'salesOrderListModification', content: 'OK'});
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

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackGroupById(index: number, item: Group) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-sales-order-popup',
    template: ''
})
export class SalesOrderPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private salesOrderPopupService: SalesOrderPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.salesOrderPopupService
                    .open(SalesOrderDialogComponent, params['id']);
            } else {
                this.modalRef = this.salesOrderPopupService
                    .open(SalesOrderDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
