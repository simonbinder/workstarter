import { Component, OnInit, OnDestroy } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService } from 'ng-jhipster';

import { Portfolio } from './portfolio.model';
import { PortfolioService } from './portfolio.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-portfolio',
    templateUrl: './portfolio.component.html'
})
export class PortfolioComponent implements OnInit, OnDestroy {
portfolios: Portfolio[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private portfolioService: PortfolioService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
        this.jhiLanguageService.setLocations(['portfolio']);
    }

    loadAll() {
        if (this.currentSearch) {
            this.portfolioService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: Response) => this.portfolios = res.json(),
                    (res: Response) => this.onError(res.json())
                );
            return;
       }
        this.portfolioService.query().subscribe(
            (res: Response) => {
                this.portfolios = res.json();
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
        this.registerChangeInPortfolios();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId (index: number, item: Portfolio) {
        return item.id;
    }



    registerChangeInPortfolios() {
        this.eventSubscriber = this.eventManager.subscribe('portfolioListModification', (response) => this.loadAll());
    }


    private onError (error) {
        this.alertService.error(error.message, null, null);
    }
}
