import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { Student } from './student.model';
import { StudentService } from './student.service';

@Component({
    selector: 'jhi-student-detail',
    templateUrl: './student-detail.component.html',
    styleUrls: [
        'student.scss'
    ]
})
export class StudentDetailComponent implements OnInit, OnDestroy {

    student: Student;
    private subscription: any;

    //testdata
    tags: string[];
    education: any[];

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private studentService: StudentService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['student']);
        

        //testdata
        this.tags = ['Windstorm','Bombasto','Magneta','Tornado'];
         this.education = [
            {year: '2017', job: 'Turmtaucher', location: 'Stuttgart-Vaihinger Kleranlagen', tasks: 'Chefreiniger des gesamten Chlorbeckens der Anlage'},
            {year: '2016', job: 'Perlentaucher', location: 'Karibische Inseln', tasks: 'Professioneller Perlentaucher in bis zu 200 Metern Tiefe.'},
            {year: '2015', job: 'Würstchenverkäufer', location: 'Mercedes Benz Arena Stuttgart', tasks: 'Spezialisierung auf Käseknacker und Bratwürste.'}
        ];

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
