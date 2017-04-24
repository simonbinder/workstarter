import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { CompanyAdmin } from './company-admin.model';
import { CompanyAdminService } from './company-admin.service';

@Component({
    selector: 'jhi-company-admin-detail',
    templateUrl: './company-admin-detail.component.html'
})
export class CompanyAdminDetailComponent implements OnInit, OnDestroy {

    companyAdmin: CompanyAdmin;
    private subscription: any;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private companyAdminService: CompanyAdminService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['companyAdmin']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.companyAdminService.find(id).subscribe(companyAdmin => {
            this.companyAdmin = companyAdmin;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
