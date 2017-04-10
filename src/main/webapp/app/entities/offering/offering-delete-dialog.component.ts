import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';

import { Offering } from './offering.model';
import { OfferingPopupService } from './offering-popup.service';
import { OfferingService } from './offering.service';

@Component({
    selector: 'jhi-offering-delete-dialog',
    templateUrl: './offering-delete-dialog.component.html'
})
export class OfferingDeleteDialogComponent {

    offering: Offering;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private offeringService: OfferingService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['offering']);
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete (id: number) {
        this.offeringService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'offeringListModification',
                content: 'Deleted an offering'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-offering-delete-popup',
    template: ''
})
export class OfferingDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private offeringPopupService: OfferingPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.offeringPopupService
                .open(OfferingDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
