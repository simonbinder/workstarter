import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { CompanyAdmin } from './company-admin.model';
import { CompanyAdminPopupService } from './company-admin-popup.service';
import { CompanyAdminService } from './company-admin.service';
import { Company, CompanyService } from '../company';

@Component({
    selector: 'jhi-company-admin-dialog',
    templateUrl: './company-admin-dialog.component.html'
})
export class CompanyAdminDialogComponent implements OnInit {

    companyAdmin: CompanyAdmin;
    authorities: any[];
    isSaving: boolean;

    companies: Company[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private companyAdminService: CompanyAdminService,
        private companyService: CompanyService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['companyAdmin']);
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.companyService.query().subscribe(
            (res: Response) => { this.companies = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.companyAdmin.id !== undefined) {
            this.companyAdminService.update(this.companyAdmin)
                .subscribe((res: CompanyAdmin) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.companyAdminService.create(this.companyAdmin)
                .subscribe((res: CompanyAdmin) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: CompanyAdmin) {
        this.eventManager.broadcast({ name: 'companyAdminListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError (error) {
        this.isSaving = false;
        this.onError(error);
    }

    private onError (error) {
        this.alertService.error(error.message, null, null);
    }

    trackCompanyById(index: number, item: Company) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-company-admin-popup',
    template: ''
})
export class CompanyAdminPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private companyAdminPopupService: CompanyAdminPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.companyAdminPopupService
                    .open(CompanyAdminDialogComponent, params['id']);
            } else {
                this.modalRef = this.companyAdminPopupService
                    .open(CompanyAdminDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
