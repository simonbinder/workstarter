import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CompanyAdmin } from './company-admin.model';
import { CompanyAdminService } from './company-admin.service';
@Injectable()
export class CompanyAdminPopupService {
    private isOpen = false;
    constructor (
        private modalService: NgbModal,
        private router: Router,
        private companyAdminService: CompanyAdminService

    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.companyAdminService.find(id).subscribe(companyAdmin => {
                this.companyAdminModalRef(component, companyAdmin);
            });
        } else {
            return this.companyAdminModalRef(component, new CompanyAdmin());
        }
    }

    companyAdminModalRef(component: Component, companyAdmin: CompanyAdmin): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.companyAdmin = companyAdmin;
        modalRef.result.then(result => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
