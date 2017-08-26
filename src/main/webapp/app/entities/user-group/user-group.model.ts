import { BaseEntity, User } from './../../shared';

const enum Permission {
    'CREATE',
    'EDIT',
    'VIEW',
    'APPROVE',
    'ABANDON'
}

const enum Feature {
    'SALES_ORDER',
    'PURCHASE_ODER',
    'INVENTORY',
    'GROUP',
    'PRODUCTS'
}

export class UserGroup implements BaseEntity {
    constructor(
        public id?: number,
        public permission?: Permission,
        public feature?: Feature,
        public group?: BaseEntity,
        public user?: User,
    ) {
    }
}
