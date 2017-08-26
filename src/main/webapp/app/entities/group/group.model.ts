import { BaseEntity } from './../../shared';

export class Group implements BaseEntity {
    constructor(
        public id?: number,
        public groupName?: string,
        public description?: string,
        public avatar?: string,
        public users?: BaseEntity[],
        public products?: BaseEntity[],
        public purchaseOrders?: BaseEntity[],
        public salesOrders?: BaseEntity[],
    ) {
    }
}
