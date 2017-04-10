import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { Offering } from './offering.model';
import { OfferingService } from './offering.service';

@Component({
    selector: 'jhi-offering-detail',
    templateUrl: './offering-detail.component.html'
})
export class OfferingDetailComponent implements OnInit, OnDestroy {

    offering: Offering;
    private subscription: any;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private offeringService: OfferingService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['offering']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.offeringService.find(id).subscribe(offering => {
            this.offering = offering;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
