import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Group } from './group.model';
import { GroupPopupService } from './group-popup.service';
import { GroupService } from './group.service';

@Component({
    selector: 'jhi-group-dialog',
    templateUrl: './group-dialog.component.html'
})
export class GroupDialogComponent implements OnInit {

    group: Group;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private groupService: GroupService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.group.id !== undefined) {
            this.subscribeToSaveResponse(
                this.groupService.update(this.group));
        } else {
            this.subscribeToSaveResponse(
                this.groupService.create(this.group));
        }
    }

    private subscribeToSaveResponse(result: Observable<Group>) {
        result.subscribe((res: Group) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Group) {
        this.eventManager.broadcast({ name: 'groupListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-group-popup',
    template: ''
})
export class GroupPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private groupPopupService: GroupPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.groupPopupService
                    .open(GroupDialogComponent, params['id']);
            } else {
                this.modalRef = this.groupPopupService
                    .open(GroupDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
