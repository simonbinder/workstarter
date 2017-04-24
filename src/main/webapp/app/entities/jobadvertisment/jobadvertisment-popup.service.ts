import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Jobadvertisment } from './jobadvertisment.model';
import { JobadvertismentService } from './jobadvertisment.service';
@Injectable()
export class JobadvertismentPopupService {
    private isOpen = false;
    constructor (
        private modalService: NgbModal,
        private router: Router,
        private jobadvertismentService: JobadvertismentService

    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.jobadvertismentService.find(id).subscribe(jobadvertisment => {
                this.jobadvertismentModalRef(component, jobadvertisment);
            });
        } else {
            return this.jobadvertismentModalRef(component, new Jobadvertisment());
        }
    }

    jobadvertismentModalRef(component: Component, jobadvertisment: Jobadvertisment): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.jobadvertisment = jobadvertisment;
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
