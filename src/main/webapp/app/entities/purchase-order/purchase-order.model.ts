import { BaseEntity, User } from './../../shared';

const enum Status {
    'APPROVED',
    'PENDING',
    'REVIEWED',
    'ABANDONED'
}

export class PurchaseOrder implements BaseEntity {
    constructor(
        public id?: number,
        public referenceNumber?: string,
        public transactionDate?: any,
        public lastUpdated?: any,
        public supplier?: string,
        public status?: Status,
        public purchaseOrderItems?: BaseEntity[],
        public author?: User,
        public lastEdit?: User,
        public group?: BaseEntity,
    ) {
    }
}
