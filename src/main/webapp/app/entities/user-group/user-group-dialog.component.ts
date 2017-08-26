import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { UserGroup } from './user-group.model';
import { UserGroupPopupService } from './user-group-popup.service';
import { UserGroupService } from './user-group.service';
import { Group, GroupService } from '../group';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-user-group-dialog',
    templateUrl: './user-group-dialog.component.html'
})
export class UserGroupDialogComponent implements OnInit {

    userGroup: UserGroup;
    authorities: any[];
    isSaving: boolean;

    groups: Group[];

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private userGroupService: UserGroupService,
        private groupService: GroupService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.groupService.query()
            .subscribe((res: ResponseWrapper) => { this.groups = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.userGroup.id !== undefined) {
            this.subscribeToSaveResponse(
                this.userGroupService.update(this.userGroup));
        } else {
            this.subscribeToSaveResponse(
                this.userGroupService.create(this.userGroup));
        }
    }

    private subscribeToSaveResponse(result: Observable<UserGroup>) {
        result.subscribe((res: UserGroup) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: UserGroup) {
        this.eventManager.broadcast({ name: 'userGroupListModification', content: 'OK'});
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

    trackGroupById(index: number, item: Group) {
        return item.id;
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-user-group-popup',
    template: ''
})
export class UserGroupPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private userGroupPopupService: UserGroupPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.userGroupPopupService
                    .open(UserGroupDialogComponent, params['id']);
            } else {
                this.modalRef = this.userGroupPopupService
                    .open(UserGroupDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
