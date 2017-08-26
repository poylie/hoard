import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { PurchaseOrder } from './purchase-order.model';
import { PurchaseOrderService } from './purchase-order.service';

@Injectable()
export class PurchaseOrderPopupService {
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private purchaseOrderService: PurchaseOrderService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.purchaseOrderService.find(id).subscribe((purchaseOrder) => {
                if (purchaseOrder.transactionDate) {
                    purchaseOrder.transactionDate = {
                        year: purchaseOrder.transactionDate.getFullYear(),
                        month: purchaseOrder.transactionDate.getMonth() + 1,
                        day: purchaseOrder.transactionDate.getDate()
                    };
                }
                purchaseOrder.lastUpdated = this.datePipe
                    .transform(purchaseOrder.lastUpdated, 'yyyy-MM-ddThh:mm');
                this.purchaseOrderModalRef(component, purchaseOrder);
            });
        } else {
            return this.purchaseOrderModalRef(component, new PurchaseOrder());
        }
    }

    purchaseOrderModalRef(component: Component, purchaseOrder: PurchaseOrder): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.purchaseOrder = purchaseOrder;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
