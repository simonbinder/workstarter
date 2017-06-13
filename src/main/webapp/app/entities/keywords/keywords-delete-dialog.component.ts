import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';

import { Keywords } from './keywords.model';
import { KeywordsPopupService } from './keywords-popup.service';
import { KeywordsService } from './keywords.service';

@Component({
    selector: 'jhi-keywords-delete-dialog',
    templateUrl: './keywords-delete-dialog.component.html'
})
export class KeywordsDeleteDialogComponent {

    keywords: Keywords;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private keywordsService: KeywordsService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['keywords']);
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete (id: number) {
        this.keywordsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'keywordsListModification',
                content: 'Deleted an keywords'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-keywords-delete-popup',
    template: ''
})
export class KeywordsDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private keywordsPopupService: KeywordsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.keywordsPopupService
                .open(KeywordsDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
