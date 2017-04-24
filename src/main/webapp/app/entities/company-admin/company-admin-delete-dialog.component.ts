import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';

import { CompanyAdmin } from './company-admin.model';
import { CompanyAdminPopupService } from './company-admin-popup.service';
import { CompanyAdminService } from './company-admin.service';

@Component({
    selector: 'jhi-company-admin-delete-dialog',
    templateUrl: './company-admin-delete-dialog.component.html'
})
export class CompanyAdminDeleteDialogComponent {

    companyAdmin: CompanyAdmin;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private companyAdminService: CompanyAdminService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['companyAdmin']);
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete (id: number) {
        this.companyAdminService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'companyAdminListModification',
                content: 'Deleted an companyAdmin'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-company-admin-delete-popup',
    template: ''
})
export class CompanyAdminDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private companyAdminPopupService: CompanyAdminPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.companyAdminPopupService
                .open(CompanyAdminDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
