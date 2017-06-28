import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService, AlertService } from 'ng-jhipster';
import { StudentService } from "../../../entities/student/student.service";
import { Student } from "../../../entities/student/student.model";
import { Profession } from "../../../entities/profession/profession.model";
import { Keywords } from "../../../entities/keywords/keywords.model";
import { Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';


@Component({
  selector: 'student-editPresentation',
  templateUrl: './student-editPresentation.component.html',
  styleUrls: [
        '../../editView/editView.scss'
    ]
})

export class StudentEditPresentation implements OnInit {
    activeModal: NgbActiveModal;
    alertService: AlertService;
    profession: Profession;
    isSaving: boolean;
    information: string;
    tempKeywords: Keywords[];
    newKeyword: string;

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
      this.tempKeywords = this.student.keywords;
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
        this.update();
  }

  private addKeyword()
  {
      var keyword = new Keywords();
      keyword.name = this.newKeyword;
      this.tempKeywords.push(keyword);
      this.newKeyword = "";
  }

  private deleteKeyword(keyword: Keywords)
  {
    var index = this.tempKeywords.findIndex(x => x.name == keyword.name);
    console.log(index);
    if (index > -1) {
        this.tempKeywords.splice(index, 1);
    }
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
        this.student.keywords.forEach(keyword => {
            this.studentService.deleteKeywords(this.student.id, keyword.id);
        });

        this.tempKeywords.forEach(keyword => {
            this.studentService.createKeywords(keyword, this.student.id);
        });

        if (this.profession.id !== undefined) {
            this.studentService.updateAccountInfo(this.student)
                .subscribe((res: Response) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        } 

    }

    private onSaveSuccess (result: Response) {
        console.log("Saving successful");
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


    isEmpty(array: any[])
    {
        if(array.length < 1)
        {
            return true;
        }
        return false;
    }

}