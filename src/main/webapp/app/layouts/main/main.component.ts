import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd, RoutesRecognized } from '@angular/router';

import { Title } from '@angular/platform-browser';
import { StateStorageService, Principal } from '../../shared';

@Component({
    selector: 'jhi-main',
    templateUrl: './main.component.html',
    styles: [`
        .home-page {
            height: 100vh;
        }
    `]
})
export class JhiMainComponent implements OnInit {
    _opened = false;
    mode = 'push';

    constructor(
        private titleService: Title,
        private router: Router,
        private principal: Principal,
        private $storageService: StateStorageService,
    ) { }

    private getPageTitle(routeSnapshot: ActivatedRouteSnapshot) {
        let title: string = (routeSnapshot.data && routeSnapshot.data['pageTitle']) ? routeSnapshot.data['pageTitle'] : 'hoardApp';
        if (routeSnapshot.firstChild) {
            title = this.getPageTitle(routeSnapshot.firstChild) || title;
        }
        return title;
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    ngOnInit() {
        this.router.events.subscribe((event) => {
            if (event instanceof NavigationEnd) {
                this.titleService.setTitle(this.getPageTitle(this.router.routerState.snapshot.root));
            }
        });
    }

    _toggleSidebar() {
        this._opened = !this._opened;
    }

    closeSidebar() {
        this._opened = false;
    }
}
