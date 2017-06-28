import { Component, OnInit, OnDestroy } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService } from 'ng-jhipster';

import { Jobadvertisment } from './jobadvertisment.model';
import { JobadvertismentService } from './jobadvertisment.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-jobadvertisment',
    templateUrl: './jobadvertisment.component.html'
})
export class JobadvertismentComponent implements OnInit, OnDestroy {
    jobadvertisments: Jobadvertisment[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private jobadvertismentService: JobadvertismentService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
        this.jhiLanguageService.setLocations(['jobadvertisment']);
    }

    loadAll() {
        if (this.currentSearch) {
            this.jobadvertismentService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: Response) => this.jobadvertisments = res.json(),
                    (res: Response) => this.onError(res.json())
                );
            return;
       }
        this.jobadvertismentService.query().subscribe(
            (res: Response) => {
                this.jobadvertisments = res.json();
                this.currentSearch = '';
            },
            (res: Response) => this.onError(res.json())
        );
    }

    search (query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInJobadvertisments();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId (index: number, item: Jobadvertisment) {
        return item.id;
    }



    registerChangeInJobadvertisments() {
        this.eventSubscriber = this.eventManager.subscribe('jobadvertismentListModification', (response) => this.loadAll());
    }


    private onError (error) {
        this.alertService.error(error.message, null, null);
    }
}
