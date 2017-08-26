import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { SalesOrder } from './sales-order.model';
import { SalesOrderService } from './sales-order.service';

@Injectable()
export class SalesOrderPopupService {
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private salesOrderService: SalesOrderService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.salesOrderService.find(id).subscribe((salesOrder) => {
                if (salesOrder.transactionDate) {
                    salesOrder.transactionDate = {
                        year: salesOrder.transactionDate.getFullYear(),
                        month: salesOrder.transactionDate.getMonth() + 1,
                        day: salesOrder.transactionDate.getDate()
                    };
                }
                salesOrder.lastUpdated = this.datePipe
                    .transform(salesOrder.lastUpdated, 'yyyy-MM-ddThh:mm');
                this.salesOrderModalRef(component, salesOrder);
            });
        } else {
            return this.salesOrderModalRef(component, new SalesOrder());
        }
    }

    salesOrderModalRef(component: Component, salesOrder: SalesOrder): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.salesOrder = salesOrder;
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
