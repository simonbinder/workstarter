import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService, AlertService } from 'ng-jhipster';
import { StudentService } from "../../../entities/student/student.service";
import { Student } from "../../../entities/student/student.model";
import { Jobadvertisment } from "../../../entities/jobadvertisment/jobadvertisment.model";


@Component({
  selector: 'job-apply',
  templateUrl: './job-apply.component.html',
  styleUrls: [
        '../../editView/editView.scss'
    ]
})

export class JobApply implements OnInit {
    activeModal: NgbActiveModal;
    alertService: AlertService;
    isSaving: boolean;
    information: string;

  @Input() _componentId;
  @Input() _student: any;


  companyAdminId: number;
  job: Jobadvertisment;
  applicationmail: string;


  constructor(
    private languageService: JhiLanguageService,
    private studentService: StudentService,
    private eventManager: EventManager
    
  ) 
  {
  }

  ngOnInit() {
    this.languageService.addLocation('editView');
    this.job = this._student;
    this.companyAdminId = this._componentId;
  }


  
  private sendMail()
  {
        if(this.applicationmail != null && this.applicationmail!="")
        {
            alert(this.applicationmail);
            this.close();
        }
  }

  private cancel()
  {
    this.close();
  }


    private close() {
        this.eventManager.broadcast({ name: 'JobApplyFinished', content: "close"});
    }

}