import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { Keywords } from './keywords.model';
import { KeywordsPopupService } from './keywords-popup.service';
import { KeywordsService } from './keywords.service';

@Component({
    selector: 'jhi-keywords-dialog',
    templateUrl: './keywords-dialog.component.html'
})
export class KeywordsDialogComponent implements OnInit {

    keywords: Keywords;
    authorities: any[];
    isSaving: boolean;
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private keywordsService: KeywordsService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['keywords']);
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
        if (this.keywords.id !== undefined) {
            this.keywordsService.update(this.keywords)
                .subscribe((res: Keywords) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.keywordsService.create(this.keywords)
                .subscribe((res: Keywords) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: Keywords) {
        this.eventManager.broadcast({ name: 'keywordsListModification', content: 'OK'});
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
    selector: 'jhi-keywords-popup',
    template: ''
})
export class KeywordsPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private keywordsPopupService: KeywordsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.keywordsPopupService
                    .open(KeywordsDialogComponent, params['id']);
            } else {
                this.modalRef = this.keywordsPopupService
                    .open(KeywordsDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
