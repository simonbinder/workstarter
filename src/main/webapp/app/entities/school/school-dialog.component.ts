import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { School } from './school.model';
import { SchoolPopupService } from './school-popup.service';
import { SchoolService } from './school.service';
import { Resume, ResumeService } from '../resume';

@Component({
    selector: 'jhi-school-dialog',
    templateUrl: './school-dialog.component.html'
})
export class SchoolDialogComponent implements OnInit {

    school: School;
    authorities: any[];
    isSaving: boolean;

    resumes: Resume[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private schoolService: SchoolService,
        private resumeService: ResumeService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['school']);
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.resumeService.query().subscribe(
            (res: Response) => { this.resumes = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.school.id !== undefined) {
            this.schoolService.update(this.school)
                .subscribe((res: School) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.schoolService.create(this.school)
                .subscribe((res: School) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: School) {
        this.eventManager.broadcast({ name: 'schoolListModification', content: 'OK'});
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

    trackResumeById(index: number, item: Resume) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-school-popup',
    template: ''
})
export class SchoolPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private schoolPopupService: SchoolPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.schoolPopupService
                    .open(SchoolDialogComponent, params['id']);
            } else {
                this.modalRef = this.schoolPopupService
                    .open(SchoolDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
