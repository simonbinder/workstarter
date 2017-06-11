import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { Profession } from './profession.model';
import { ProfessionPopupService } from './profession-popup.service';
import { ProfessionService } from './profession.service';
import { Student, StudentService } from '../student';

@Component({
    selector: 'jhi-profession-dialog',
    templateUrl: './profession-dialog.component.html'
})
export class ProfessionDialogComponent implements OnInit {

    profession: Profession;
    authorities: any[];
    isSaving: boolean;

    students: Student[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private professionService: ProfessionService,
        private studentService: StudentService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['profession']);
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
        if (this.profession.id !== undefined) {
            this.professionService.update(this.profession)
                .subscribe((res: Profession) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.professionService.create(this.profession)
                .subscribe((res: Profession) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: Profession) {
        this.eventManager.broadcast({ name: 'professionListModification', content: 'OK'});
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
    selector: 'jhi-profession-popup',
    template: ''
})
export class ProfessionPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private professionPopupService: ProfessionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.professionPopupService
                    .open(ProfessionDialogComponent, params['id']);
            } else {
                this.modalRef = this.professionPopupService
                    .open(ProfessionDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
