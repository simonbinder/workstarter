import { Component, OnInit, OnDestroy } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService } from 'ng-jhipster';

import { CompanyAdmin } from './company-admin.model';
import { CompanyAdminService } from './company-admin.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-company-admin',
    templateUrl: './company-admin.component.html'
})
export class CompanyAdminComponent implements OnInit, OnDestroy {
companyAdmins: CompanyAdmin[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private companyAdminService: CompanyAdminService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
        this.jhiLanguageService.setLocations(['companyAdmin']);
    }

    loadAll() {
        if (this.currentSearch) {
            this.companyAdminService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: Response) => this.companyAdmins = res.json(),
                    (res: Response) => this.onError(res.json())
                );
            return;
       }
        this.companyAdminService.query().subscribe(
            (res: Response) => {
                this.companyAdmins = res.json();
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
        this.registerChangeInCompanyAdmins();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId (index: number, item: CompanyAdmin) {
        return item.id;
    }



    registerChangeInCompanyAdmins() {
        this.eventSubscriber = this.eventManager.subscribe('companyAdminListModification', (response) => this.loadAll());
    }


    private onError (error) {
        this.alertService.error(error.message, null, null);
    }
}
