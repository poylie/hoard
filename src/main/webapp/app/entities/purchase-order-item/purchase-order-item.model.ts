import { BaseEntity } from './../../shared';

export class PurchaseOrderItem implements BaseEntity {
    constructor(
        public id?: number,
        public quantity?: number,
        public totalCost?: number,
        public items?: BaseEntity[],
        public purchaseOrder?: BaseEntity,
    ) {
    }
}
