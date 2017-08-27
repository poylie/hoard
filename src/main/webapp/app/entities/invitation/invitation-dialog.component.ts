import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Invitation } from './invitation.model';
import { InvitationPopupService } from './invitation-popup.service';
import { InvitationService } from './invitation.service';
import { User, UserService } from '../../shared';
import { Group, GroupService } from '../group';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-invitation-dialog',
    templateUrl: './invitation-dialog.component.html'
})
export class InvitationDialogComponent implements OnInit {

    invitation: Invitation;
    authorities: any[];
    isSaving: boolean;

    users: User[];

    groups: Group[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private invitationService: InvitationService,
        private userService: UserService,
        private groupService: GroupService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.groupService.query()
            .subscribe((res: ResponseWrapper) => { this.groups = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.invitation.id !== undefined) {
            this.subscribeToSaveResponse(
                this.invitationService.update(this.invitation));
        } else {
            this.subscribeToSaveResponse(
                this.invitationService.create(this.invitation));
        }
    }

    private subscribeToSaveResponse(result: Observable<Invitation>) {
        result.subscribe((res: Invitation) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Invitation) {
        this.eventManager.broadcast({ name: 'invitationListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackGroupById(index: number, item: Group) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-invitation-popup',
    template: ''
})
export class InvitationPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private invitationPopupService: InvitationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.invitationPopupService
                    .open(InvitationDialogComponent, params['id']);
            } else {
                this.modalRef = this.invitationPopupService
                    .open(InvitationDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
