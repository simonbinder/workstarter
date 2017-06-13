import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { Keywords } from './keywords.model';
import { KeywordsService } from './keywords.service';

@Component({
    selector: 'jhi-keywords-detail',
    templateUrl: './keywords-detail.component.html'
})
export class KeywordsDetailComponent implements OnInit, OnDestroy {

    keywords: Keywords;
    private subscription: any;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private keywordsService: KeywordsService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['keywords']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.keywordsService.find(id).subscribe(keywords => {
            this.keywords = keywords;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
