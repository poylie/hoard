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
        public author?: User,
        public lastEdit?: User,
        public purchaseOrderItems?: BaseEntity[],
        public group?: BaseEntity,
    ) {
    }
}
