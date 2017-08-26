import { BaseEntity } from './../../shared';

export class SalesOrderItem implements BaseEntity {
    constructor(
        public id?: number,
        public quantity?: number,
        public totalPrice?: number,
        public items?: BaseEntity[],
        public salesOrder?: BaseEntity,
    ) {
    }
}
