import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { Jobadvertisment } from './jobadvertisment.model';
import { JobadvertismentService } from './jobadvertisment.service';
import { EditViewModalService } from "../../shared/index";
import { NgbModalRef } from "@ng-bootstrap/ng-bootstrap";

@Component({
    selector: 'jhi-jobadvertisment-detail',
    templateUrl: './jobadvertisment-detail.component.html',
    styleUrls: [
        'jobadvertisment.scss'
    ]
})
export class JobadvertismentDetailComponent implements OnInit, OnDestroy {

    jobadvertisment: Jobadvertisment;
    private subscription: any;
    modalRef: NgbModalRef;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private jobadvertismentService: JobadvertismentService,
        private route: ActivatedRoute,
        private editViewModalService: EditViewModalService
    ) {
        this.jhiLanguageService.setLocations(['jobadvertisment']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.jobadvertismentService.find(id).subscribe(jobadvertisment => {
            this.jobadvertisment = jobadvertisment;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

    apply()
    {
        this.modalRef = this.editViewModalService.open("jobApply", this.jobadvertisment, -1);
    }

}
