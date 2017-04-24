import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { Portfolio } from './portfolio.model';
import { PortfolioService } from './portfolio.service';

@Component({
    selector: 'jhi-portfolio-detail',
    templateUrl: './portfolio-detail.component.html'
})
export class PortfolioDetailComponent implements OnInit, OnDestroy {

    portfolio: Portfolio;
    private subscription: any;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private portfolioService: PortfolioService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['portfolio']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.portfolioService.find(id).subscribe(portfolio => {
            this.portfolio = portfolio;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
