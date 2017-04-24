import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { Company } from './company.model';
import { CompanyService } from './company.service';

@Component({
    selector: 'jhi-company-detail',
    templateUrl: './company-detail.component.html'
})
export class CompanyDetailComponent implements OnInit, OnDestroy {

    company: Company;
    private subscription: any;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private companyService: CompanyService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['company']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.companyService.find(id).subscribe(company => {
            this.company = company;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
