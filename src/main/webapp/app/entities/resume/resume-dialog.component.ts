import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { Resume } from './resume.model';
import { ResumePopupService } from './resume-popup.service';
import { ResumeService } from './resume.service';
import { Student, StudentService } from '../student';
import { School, SchoolService } from '../school';
import { Company, CompanyService } from '../company';
import { Qualification, QualificationService } from '../qualification';

@Component({
    selector: 'jhi-resume-dialog',
    templateUrl: './resume-dialog.component.html'
})
export class ResumeDialogComponent implements OnInit {

    resume: Resume;
    authorities: any[];
    isSaving: boolean;

    students: Student[];

    schools: School[];

    companies: Company[];

    qualifications: Qualification[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private resumeService: ResumeService,
        private studentService: StudentService,
        private schoolService: SchoolService,
        private companyService: CompanyService,
        private qualificationService: QualificationService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['resume']);
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.studentService.query().subscribe(
            (res: Response) => { this.students = res.json(); }, (res: Response) => this.onError(res.json()));
        this.schoolService.query().subscribe(
            (res: Response) => { this.schools = res.json(); }, (res: Response) => this.onError(res.json()));
        this.companyService.query().subscribe(
            (res: Response) => { this.companies = res.json(); }, (res: Response) => this.onError(res.json()));
        this.qualificationService.query().subscribe(
            (res: Response) => { this.qualifications = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.resume.id !== undefined) {
            this.resumeService.update(this.resume)
                .subscribe((res: Resume) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.resumeService.create(this.resume)
                .subscribe((res: Resume) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: Resume) {
        this.eventManager.broadcast({ name: 'resumeListModification', content: 'OK'});
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

    trackSchoolById(index: number, item: School) {
        return item.id;
    }

    trackCompanyById(index: number, item: Company) {
        return item.id;
    }

    trackQualificationById(index: number, item: Qualification) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-resume-popup',
    template: ''
})
export class ResumePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private resumePopupService: ResumePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.resumePopupService
                    .open(ResumeDialogComponent, params['id']);
            } else {
                this.modalRef = this.resumePopupService
                    .open(ResumeDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
