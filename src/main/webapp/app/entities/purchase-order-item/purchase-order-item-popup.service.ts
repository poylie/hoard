import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { PurchaseOrderItem } from './purchase-order-item.model';
import { PurchaseOrderItemService } from './purchase-order-item.service';

@Injectable()
export class PurchaseOrderItemPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private purchaseOrderItemService: PurchaseOrderItemService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.purchaseOrderItemService.find(id).subscribe((purchaseOrderItem) => {
                this.purchaseOrderItemModalRef(component, purchaseOrderItem);
            });
        } else {
            return this.purchaseOrderItemModalRef(component, new PurchaseOrderItem());
        }
    }

    purchaseOrderItemModalRef(component: Component, purchaseOrderItem: PurchaseOrderItem): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.purchaseOrderItem = purchaseOrderItem;
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
