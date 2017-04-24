import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { Company } from './company.model';
import { CompanyPopupService } from './company-popup.service';
import { CompanyService } from './company.service';
import { CompanyAdmin, CompanyAdminService } from '../company-admin';
import { Resume, ResumeService } from '../resume';
import { Jobadvertisment, JobadvertismentService } from '../jobadvertisment';

@Component({
    selector: 'jhi-company-dialog',
    templateUrl: './company-dialog.component.html'
})
export class CompanyDialogComponent implements OnInit {

    company: Company;
    authorities: any[];
    isSaving: boolean;

    companyadmins: CompanyAdmin[];

    resumes: Resume[];

    jobadvertisments: Jobadvertisment[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private companyService: CompanyService,
        private companyAdminService: CompanyAdminService,
        private resumeService: ResumeService,
        private jobadvertismentService: JobadvertismentService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['company']);
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.companyAdminService.query().subscribe(
            (res: Response) => { this.companyadmins = res.json(); }, (res: Response) => this.onError(res.json()));
        this.resumeService.query().subscribe(
            (res: Response) => { this.resumes = res.json(); }, (res: Response) => this.onError(res.json()));
        this.jobadvertismentService.query().subscribe(
            (res: Response) => { this.jobadvertisments = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.company.id !== undefined) {
            this.companyService.update(this.company)
                .subscribe((res: Company) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.companyService.create(this.company)
                .subscribe((res: Company) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: Company) {
        this.eventManager.broadcast({ name: 'companyListModification', content: 'OK'});
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

    trackCompanyAdminById(index: number, item: CompanyAdmin) {
        return item.id;
    }

    trackResumeById(index: number, item: Resume) {
        return item.id;
    }

    trackJobadvertismentById(index: number, item: Jobadvertisment) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-company-popup',
    template: ''
})
export class CompanyPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private companyPopupService: CompanyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.companyPopupService
                    .open(CompanyDialogComponent, params['id']);
            } else {
                this.modalRef = this.companyPopupService
                    .open(CompanyDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
