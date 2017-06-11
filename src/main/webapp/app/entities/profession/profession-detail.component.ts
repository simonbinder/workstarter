import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { Profession } from './profession.model';
import { ProfessionService } from './profession.service';

@Component({
    selector: 'jhi-profession-detail',
    templateUrl: './profession-detail.component.html'
})
export class ProfessionDetailComponent implements OnInit, OnDestroy {

    profession: Profession;
    private subscription: any;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private professionService: ProfessionService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['profession']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.professionService.find(id).subscribe(profession => {
            this.profession = profession;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
