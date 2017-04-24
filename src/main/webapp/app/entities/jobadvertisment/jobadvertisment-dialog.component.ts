import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { Jobadvertisment } from './jobadvertisment.model';
import { JobadvertismentPopupService } from './jobadvertisment-popup.service';
import { JobadvertismentService } from './jobadvertisment.service';
import { Company, CompanyService } from '../company';

@Component({
    selector: 'jhi-jobadvertisment-dialog',
    templateUrl: './jobadvertisment-dialog.component.html'
})
export class JobadvertismentDialogComponent implements OnInit {

    jobadvertisment: Jobadvertisment;
    authorities: any[];
    isSaving: boolean;

    companies: Company[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private jobadvertismentService: JobadvertismentService,
        private companyService: CompanyService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['jobadvertisment']);
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
        if (this.jobadvertisment.id !== undefined) {
            this.jobadvertismentService.update(this.jobadvertisment)
                .subscribe((res: Jobadvertisment) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.jobadvertismentService.create(this.jobadvertisment)
                .subscribe((res: Jobadvertisment) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: Jobadvertisment) {
        this.eventManager.broadcast({ name: 'jobadvertismentListModification', content: 'OK'});
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
    selector: 'jhi-jobadvertisment-popup',
    template: ''
})
export class JobadvertismentPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private jobadvertismentPopupService: JobadvertismentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.jobadvertismentPopupService
                    .open(JobadvertismentDialogComponent, params['id']);
            } else {
                this.modalRef = this.jobadvertismentPopupService
                    .open(JobadvertismentDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
