import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { Project } from './project.model';
import { ProjectService } from './project.service';

@Component({
    selector: 'jhi-project-detail',
    templateUrl: './project-detail.component.html'
})
export class ProjectDetailComponent implements OnInit, OnDestroy {

    project: Project;
    private subscription: any;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private projectService: ProjectService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['project']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.projectService.find(id).subscribe(project => {
            this.project = project;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
