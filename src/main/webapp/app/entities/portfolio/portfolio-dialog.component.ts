import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { Portfolio } from './portfolio.model';
import { PortfolioPopupService } from './portfolio-popup.service';
import { PortfolioService } from './portfolio.service';

@Component({
    selector: 'jhi-portfolio-dialog',
    templateUrl: './portfolio-dialog.component.html'
})
export class PortfolioDialogComponent implements OnInit {

    portfolio: Portfolio;
    authorities: any[];
    isSaving: boolean;
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private portfolioService: PortfolioService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['portfolio']);
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
        if (this.portfolio.id !== undefined) {
            this.portfolioService.update(this.portfolio)
                .subscribe((res: Portfolio) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.portfolioService.create(this.portfolio)
                .subscribe((res: Portfolio) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: Portfolio) {
        this.eventManager.broadcast({ name: 'portfolioListModification', content: 'OK'});
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
    selector: 'jhi-portfolio-popup',
    template: ''
})
export class PortfolioPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private portfolioPopupService: PortfolioPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.portfolioPopupService
                    .open(PortfolioDialogComponent, params['id']);
            } else {
                this.modalRef = this.portfolioPopupService
                    .open(PortfolioDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
