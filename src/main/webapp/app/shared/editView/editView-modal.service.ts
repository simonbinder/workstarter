import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { JhiEditViewModalComponent } from './editView.component';

@Injectable()
export class EditViewModalService {
    private isOpen = false;
    constructor (
        private modalService: NgbModal,
    ) {}

    open (): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;
        let modalRef = this.modalService.open(JhiEditViewModalComponent);
        modalRef.result.then(result => {
            this.isOpen = false;
        }, (reason) => {
            this.isOpen = false;
        });
        return modalRef;
    }
}
