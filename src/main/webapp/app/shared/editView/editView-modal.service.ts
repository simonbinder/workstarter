import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { JhiEditViewModalComponent } from './editView.component';

@Injectable()
export class EditViewModalService {
    private isOpen = false;
    constructor (
        private modalService: NgbModal,
    ) {}

    open (editComponent: string, student: any, componentId: number): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;
        let modalRef = this.modalService.open(JhiEditViewModalComponent);
        const contentComponentInstance = modalRef.componentInstance;
        contentComponentInstance._editComponent = editComponent;
        contentComponentInstance._componentId = componentId;
        contentComponentInstance._student = student;

        modalRef.result.then(result => {
            this.isOpen = false;
        }, (reason) => {
            this.isOpen = false;
        });
        return modalRef;
    }
}
