import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager , JhiDataUtils } from 'ng-jhipster';

import { Group } from './group.model';
import { GroupService } from './group.service';

@Component({
    selector: 'jhi-group-detail',
    templateUrl: './group-detail.component.html'
})
export class GroupDetailComponent implements OnInit, OnDestroy {

    group: Group;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private groupService: GroupService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInGroups();
    }

    load(id) {
        this.groupService.find(id).subscribe((group) => {
            this.group = group;
        });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInGroups() {
        this.eventSubscriber = this.eventManager.subscribe(
            'groupListModification',
            (response) => this.load(this.group.id)
        );
    }
}
