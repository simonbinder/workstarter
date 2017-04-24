import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';

import { Jobadvertisment } from './jobadvertisment.model';
import { JobadvertismentPopupService } from './jobadvertisment-popup.service';
import { JobadvertismentService } from './jobadvertisment.service';

@Component({
    selector: 'jhi-jobadvertisment-delete-dialog',
    templateUrl: './jobadvertisment-delete-dialog.component.html'
})
export class JobadvertismentDeleteDialogComponent {

    jobadvertisment: Jobadvertisment;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private jobadvertismentService: JobadvertismentService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['jobadvertisment']);
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete (id: number) {
        this.jobadvertismentService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'jobadvertismentListModification',
                content: 'Deleted an jobadvertisment'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-jobadvertisment-delete-popup',
    template: ''
})
export class JobadvertismentDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private jobadvertismentPopupService: JobadvertismentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.jobadvertismentPopupService
                .open(JobadvertismentDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
