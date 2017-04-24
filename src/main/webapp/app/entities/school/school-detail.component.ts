import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { School } from './school.model';
import { SchoolService } from './school.service';

@Component({
    selector: 'jhi-school-detail',
    templateUrl: './school-detail.component.html'
})
export class SchoolDetailComponent implements OnInit, OnDestroy {

    school: School;
    private subscription: any;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private schoolService: SchoolService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['school']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.schoolService.find(id).subscribe(school => {
            this.school = school;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
