import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService, AlertService } from 'ng-jhipster';
import { StudentService } from "../../../entities/student/student.service";
import { Student } from "../../../entities/student/student.model";
import { Profession } from "../../../entities/profession/profession.model";


@Component({
  selector: 'student-editJobs',
  templateUrl: './student-editJobs.component.html',
  styleUrls: [
        '../../editView/editView.scss'
    ]
})

export class StudentEditJobs implements OnInit {
    activeModal: NgbActiveModal;
    alertService: AlertService;
    profession: Profession;
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
    this.fillFormFromDb(this._componentId);
  }

  fillFormFromDb(professionsId)
  {
    if(professionsId == null || professionsId < 0)
    {
      this.profession = new Profession();
      console.log("no existing profession found with given id.");
      console.log(this.profession);
      return;
    }
    console.log(this.student);

    for(let prof of this.student.professions) {
      if(prof.id == professionsId)
      {
        this.profession = prof;
        console.log(this.profession);
        break;
      }
    }
  }

  
  private save ()
  {
    if(this._componentId == null || this._componentId < 0)
    {
        console.log("create new profession");
        this.createNew();
    }
    else
    {
        console.log("update profession");
        this.update();
    }
  }

  private createNew ()
  {
    this.information = "Created";
    this.studentService.createProfession(this.profession, this.student.id)
                .subscribe((res: Profession) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
  }

  private delete()
  {
        this.information = "Deleted";
        this.studentService.deleteProfession(this.profession, this.student.id, this.profession.id).subscribe(response => {
            this.onSaveSuccess(null), (res: Response) => this.onSaveError(res.json())
        });
    }

  private update () 
  {
        this.information = "Updated";
        this.isSaving = true;
        if (this.profession.id !== undefined) {
            this.studentService.updateProfession(this.profession, this.student.id, this.profession.id)
                .subscribe((res: Profession) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } 
        else {
            this.studentService.create(this.student)
                .subscribe((res: Profession) =>
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