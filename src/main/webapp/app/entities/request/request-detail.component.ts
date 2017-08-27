import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Request } from './request.model';
import { RequestService } from './request.service';

@Component({
    selector: 'jhi-request-detail',
    templateUrl: './request-detail.component.html'
})
export class RequestDetailComponent implements OnInit, OnDestroy {

    request: Request;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private requestService: RequestService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInRequests();
    }

    load(id) {
        this.requestService.find(id).subscribe((request) => {
            this.request = request;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRequests() {
        this.eventSubscriber = this.eventManager.subscribe(
            'requestListModification',
            (response) => this.load(this.request.id)
        );
    }
}
