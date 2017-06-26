import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService, AlertService } from 'ng-jhipster';
import { StudentService } from "../../../entities/student/student.service";
import { Student } from "../../../entities/student/student.model";
import { School } from "../../../entities/school/school.model";


@Component({
  selector: 'student-editSchools',
  templateUrl: './student-editSchools.component.html',
  styleUrls: [
        '../../editView/editView.scss'
    ]
})

export class StudentEditSchools implements OnInit {
    activeModal: NgbActiveModal;
    alertService: AlertService;
    school: School;
    isSaving: boolean;
    information: string;

  @Input() _componentId;
  @Input() _student: any;


  student: Student;


  constructor(
    private languageService: JhiLanguageService,
    private studentService: StudentService,
    private eventManager: EventManager
    
  ) 
  {
  }

  ngOnInit() {
    this.languageService.addLocation('editView');
    this.student = this._student;
    console.log(this.student);
    this.fillFormFromDb(this._componentId);
  }

  fillFormFromDb(schoolsId)
  {
    if(schoolsId == null || schoolsId < 0)
    {
      this.school = new School();
      console.log("no existing school found with given id.");
      console.log(this.school);
      return;
    }
    console.log(this.student);

    for(let s of this.student.schools) {
      if(s.id == schoolsId)
      {
        this.school = s;
        console.log(this.school);
        break;
      }
    }
  }

  
  private save ()
  {
    if(this._componentId == null || this._componentId < 0)
    {
        console.log("create new school");
        this.createNew();
    }
    else
    {
        console.log("update school");
        this.update();
    }
  }

  private createNew ()
  {
    this.information = "Created";
    this.studentService.createSchool(this.school, this.student.id)
                .subscribe((res: School) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
  }

  private delete()
  {
        this.information = "Deleted";
        this.studentService.deleteSchool(this.school, this.student.id, this.school.id).subscribe(response => {
            this.onSaveSuccess(null), (res: Response) => this.onSaveError(res.json())
        });
    }

  private update () 
  {
        this.information = "Updated";
        this.isSaving = true;
        if (this.school.id !== undefined) {
            this.studentService.updateSchool(this.school, this.student.id, this.school.id)
                .subscribe((res: School) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.studentService.create(this.student)
                .subscribe((res: School) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: Student) {
        this.eventManager.broadcast({ name: 'EditFormsFinished', content: this.information});
        this.isSaving = false;
    }

    private onSaveError (error) {
        this.isSaving = false;
        this.onError(error);
    }

    private onError (error) {
        this.alertService.error(error.message, null, null);
    }

}