import { BaseEntity, User } from './../../shared';

const enum RequestStatus {
    'PENDING',
    'ACCEPTED',
    'DECLINED'
}

export class Request implements BaseEntity {
    constructor(
        public id?: number,
        public requestStatus?: RequestStatus,
        public requestor?: User,
        public group?: BaseEntity,
    ) {
    }
}
