import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';

import { Searching } from './searching.model';
import { SearchingPopupService } from './searching-popup.service';
import { SearchingService } from './searching.service';

@Component({
    selector: 'jhi-searching-delete-dialog',
    templateUrl: './searching-delete-dialog.component.html'
})
export class SearchingDeleteDialogComponent {

    searching: Searching;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private searchingService: SearchingService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['searching']);
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete (id: number) {
        this.searchingService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'searchingListModification',
                content: 'Deleted an searching'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-searching-delete-popup',
    template: ''
})
export class SearchingDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private searchingPopupService: SearchingPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.searchingPopupService
                .open(SearchingDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
