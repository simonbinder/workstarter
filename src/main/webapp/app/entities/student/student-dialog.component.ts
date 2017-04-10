import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { Student } from './student.model';
import { StudentPopupService } from './student-popup.service';
import { StudentService } from './student.service';

@Component({
    selector: 'jhi-student-dialog',
    templateUrl: './student-dialog.component.html'
})
export class StudentDialogComponent implements OnInit {

    student: Student;
    authorities: any[];
    isSaving: boolean;
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private studentService: StudentService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['student']);
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.student.id !== undefined) {
            this.studentService.update(this.student)
                .subscribe((res: Student) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.studentService.create(this.student)
                .subscribe((res: Student) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: Student) {
        this.eventManager.broadcast({ name: 'studentListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-student-popup',
    template: ''
})
export class StudentPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private studentPopupService: StudentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.studentPopupService
                    .open(StudentDialogComponent, params['id']);
            } else {
                this.modalRef = this.studentPopupService
                    .open(StudentDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
