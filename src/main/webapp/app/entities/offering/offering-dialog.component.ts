import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { Offering } from './offering.model';
import { OfferingPopupService } from './offering-popup.service';
import { OfferingService } from './offering.service';
import { Student, StudentService } from '../student';

@Component({
    selector: 'jhi-offering-dialog',
    templateUrl: './offering-dialog.component.html'
})
export class OfferingDialogComponent implements OnInit {

    offering: Offering;
    authorities: any[];
    isSaving: boolean;

    students: Student[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private offeringService: OfferingService,
        private studentService: StudentService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['offering']);
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.studentService.query().subscribe(
            (res: Response) => { this.students = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.offering.id !== undefined) {
            this.offeringService.update(this.offering)
                .subscribe((res: Offering) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.offeringService.create(this.offering)
                .subscribe((res: Offering) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: Offering) {
        this.eventManager.broadcast({ name: 'offeringListModification', content: 'OK'});
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

    trackStudentById(index: number, item: Student) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-offering-popup',
    template: ''
})
export class OfferingPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private offeringPopupService: OfferingPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.offeringPopupService
                    .open(OfferingDialogComponent, params['id']);
            } else {
                this.modalRef = this.offeringPopupService
                    .open(OfferingDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
