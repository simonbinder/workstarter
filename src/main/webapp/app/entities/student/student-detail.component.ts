import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { Student } from './student.model';
import { StudentService } from './student.service';

@Component({
    selector: 'jhi-student-detail',
    templateUrl: './student-detail.component.html'
})
export class StudentDetailComponent implements OnInit, OnDestroy {

    student: Student;
    private subscription: any;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private studentService: StudentService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['student']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.studentService.find(id).subscribe(student => {
            this.student = student;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
