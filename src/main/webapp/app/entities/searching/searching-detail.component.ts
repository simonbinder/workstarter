import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { Searching } from './searching.model';
import { SearchingService } from './searching.service';

@Component({
    selector: 'jhi-searching-detail',
    templateUrl: './searching-detail.component.html'
})
export class SearchingDetailComponent implements OnInit, OnDestroy {

    searching: Searching;
    private subscription: any;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private searchingService: SearchingService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['searching']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.searchingService.find(id).subscribe(searching => {
            this.searching = searching;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
