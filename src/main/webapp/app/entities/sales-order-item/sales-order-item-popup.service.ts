import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { SalesOrderItem } from './sales-order-item.model';
import { SalesOrderItemService } from './sales-order-item.service';

@Injectable()
export class SalesOrderItemPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private salesOrderItemService: SalesOrderItemService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.salesOrderItemService.find(id).subscribe((salesOrderItem) => {
                this.salesOrderItemModalRef(component, salesOrderItem);
            });
        } else {
            return this.salesOrderItemModalRef(component, new SalesOrderItem());
        }
    }

    salesOrderItemModalRef(component: Component, salesOrderItem: SalesOrderItem): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.salesOrderItem = salesOrderItem;
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
