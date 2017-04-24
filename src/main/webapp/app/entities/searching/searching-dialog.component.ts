import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { Searching } from './searching.model';
import { SearchingPopupService } from './searching-popup.service';
import { SearchingService } from './searching.service';
import { Student, StudentService } from '../student';

@Component({
    selector: 'jhi-searching-dialog',
    templateUrl: './searching-dialog.component.html'
})
export class SearchingDialogComponent implements OnInit {

    searching: Searching;
    authorities: any[];
    isSaving: boolean;

    students: Student[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private searchingService: SearchingService,
        private studentService: StudentService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['searching']);
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
        if (this.searching.id !== undefined) {
            this.searchingService.update(this.searching)
                .subscribe((res: Searching) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.searchingService.create(this.searching)
                .subscribe((res: Searching) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: Searching) {
        this.eventManager.broadcast({ name: 'searchingListModification', content: 'OK'});
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
    selector: 'jhi-searching-popup',
    template: ''
})
export class SearchingPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private searchingPopupService: SearchingPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.searchingPopupService
                    .open(SearchingDialogComponent, params['id']);
            } else {
                this.modalRef = this.searchingPopupService
                    .open(SearchingDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
