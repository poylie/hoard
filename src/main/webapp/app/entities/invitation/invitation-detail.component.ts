import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Invitation } from './invitation.model';
import { InvitationService } from './invitation.service';

@Component({
    selector: 'jhi-invitation-detail',
    templateUrl: './invitation-detail.component.html'
})
export class InvitationDetailComponent implements OnInit, OnDestroy {

    invitation: Invitation;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private invitationService: InvitationService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInInvitations();
    }

    load(id) {
        this.invitationService.find(id).subscribe((invitation) => {
            this.invitation = invitation;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInInvitations() {
        this.eventSubscriber = this.eventManager.subscribe(
            'invitationListModification',
            (response) => this.load(this.invitation.id)
        );
    }
}
