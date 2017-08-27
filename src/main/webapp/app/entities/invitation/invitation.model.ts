import { BaseEntity, User } from './../../shared';

const enum InvitationStatus {
    'PENDING',
    'ACCEPTED',
    'DECLINED'
}

export class Invitation implements BaseEntity {
    constructor(
        public id?: number,
        public invitationStatus?: InvitationStatus,
        public inviter?: User,
        public invitee?: User,
        public group?: BaseEntity,
    ) {
    }
}
