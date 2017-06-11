import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Profession } from './profession.model';
import { ProfessionService } from './profession.service';
@Injectable()
export class ProfessionPopupService {
    private isOpen = false;
    constructor (
        private modalService: NgbModal,
        private router: Router,
        private professionService: ProfessionService

    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.professionService.find(id).subscribe(profession => {
                if (profession.startDate) {
                    profession.startDate = {
                        year: profession.startDate.getFullYear(),
                        month: profession.startDate.getMonth() + 1,
                        day: profession.startDate.getDate()
                    };
                }
                if (profession.endDate) {
                    profession.endDate = {
                        year: profession.endDate.getFullYear(),
                        month: profession.endDate.getMonth() + 1,
                        day: profession.endDate.getDate()
                    };
                }
                this.professionModalRef(component, profession);
            });
        } else {
            return this.professionModalRef(component, new Profession());
        }
    }

    professionModalRef(component: Component, profession: Profession): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.profession = profession;
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
