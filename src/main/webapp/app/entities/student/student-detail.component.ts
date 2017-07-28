import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService, EventManager } from 'ng-jhipster';
import { Student } from './student.model';
import { StudentService } from './student.service';
import { AccountService, EditViewModalService } from '../../shared';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from "rxjs/Subscription";
import { Response } from '@angular/http';
import { RequestOptions } from '@angular/http';
import {DomSanitizer} from '@angular/platform-browser';
import { Observable } from 'rxjs/Rx';


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
    eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private account: AccountService,
        private editViewModalService: EditViewModalService,
        private jhiLanguageService: JhiLanguageService,
        private studentService: StudentService,
        private sanitizer:DomSanitizer,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['student']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
            this.setPrivateAccountSettings(params['id']);
        });
        this.registerChangeInEditForms();
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

    fileChange(event) {
        if(!this.isMyAccount)
        {
            return;
        }
        let fileList: FileList = event.target.files;
        if(fileList.length > 0) {
            let file: File = fileList[0];
            let formData:FormData = new FormData();
            formData.append('file', file, file.name);
            this.studentService.uploadImage(this.student.id, formData)
                .subscribe((res: Response) =>
                        this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        }
}

   private onSaveSuccess (result: Response) {
        console.log("Saving successful");
    }

    private onSaveError (error) {
        console.log("Saving error");
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

    isEmpty(array: any[])
    {
        if(array.length < 1)
        {
            return true;
        }
        return false;
    }

    registerChangeInEditForms() {
        this.eventSubscriber = this.eventManager.subscribe('EditFormsFinished', (response) => this.realoadStudent(response));
    }

    private realoadStudent(information)
    {   
        console.log(information);
        if(information.content == "Created" || information.content == "Deleted")
        {
            this.load(this.student.id);
        }
        console.log(this.student);
    }

}
