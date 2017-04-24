import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { Jobadvertisment } from './jobadvertisment.model';
import { JobadvertismentService } from './jobadvertisment.service';

@Component({
    selector: 'jhi-jobadvertisment-detail',
    templateUrl: './jobadvertisment-detail.component.html'
})
export class JobadvertismentDetailComponent implements OnInit, OnDestroy {

    jobadvertisment: Jobadvertisment;
    private subscription: any;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private jobadvertismentService: JobadvertismentService,
        private route: ActivatedRoute
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

}
