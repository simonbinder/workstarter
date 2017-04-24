import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { Resume } from './resume.model';
import { ResumeService } from './resume.service';

@Component({
    selector: 'jhi-resume-detail',
    templateUrl: './resume-detail.component.html'
})
export class ResumeDetailComponent implements OnInit, OnDestroy {

    resume: Resume;
    private subscription: any;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private resumeService: ResumeService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['resume']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.resumeService.find(id).subscribe(resume => {
            this.resume = resume;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
