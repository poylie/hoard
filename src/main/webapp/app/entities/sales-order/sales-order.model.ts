import { BaseEntity, User } from './../../shared';

const enum Status {
    'APPROVED',
    'PENDING',
    'REVIEWED',
    'ABANDONED'
}

export class SalesOrder implements BaseEntity {
    constructor(
        public id?: number,
        public referenceNumber?: string,
        public transactionDate?: any,
        public lastUpdated?: any,
        public customer?: string,
        public status?: Status,
        public author?: User,
        public lastEdit?: User,
        public salesOrderItems?: BaseEntity[],
        public group?: BaseEntity,
    ) {
    }
}
