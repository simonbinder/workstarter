import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { Student } from './student.model';
import { StudentService } from './student.service';
import { AccountService, EditViewModalService } from '../../shared';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'jhi-student-detail',
    templateUrl: './student-detail.component.html',
    styleUrls: [
        'student.scss'
    ]
})
export class StudentDetailComponent implements OnInit, OnDestroy {

    student: Student;
    currentID: number;
    private subscription: any;
    isMyAccount: boolean = false;
    modalRef: NgbModalRef;

    //testdata
    tags: string[];
    education: any[];

    constructor(
        private account: AccountService,
        private editViewModalService: EditViewModalService,
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
            this.setPrivateAccountSettings(params['id']);
        });
    }

    setPrivateAccountSettings(id)
    {
        // Get own account id
        this.account.get().toPromise().then(userAccount => {
            if (userAccount) 
            {
                // Check if current page belongs to my account
                if(userAccount.id == id) 
                {
                    // Make class="isMyAccount" visible
                    this.isMyAccount = true;
                }
            } 
        });
    }

    edit(editComponent: string, student: Student, componentId: number) {
        this.modalRef = this.editViewModalService.open(editComponent, student, componentId);
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

    formatDate(date: string)
    {
        if(date!=null)
        {
            return date.substring(0,4);
        }
        else
        {
            return "";
        }
    }

    notNull(value: string)
    {
        if(value!=null && value!="string" && value!=" ")
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
