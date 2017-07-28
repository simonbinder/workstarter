import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService, AlertService } from 'ng-jhipster';
import { StudentService } from "../../../entities/student/student.service";
import { Student } from "../../../entities/student/student.model";
import { Project } from "../../../entities/project/project.model";


@Component({
  selector: 'student-editProject',
  templateUrl: './student-editProject.component.html',
  styleUrls: [
        '../../editView/editView.scss'
    ]
})

export class StudentEditProject implements OnInit {
    activeModal: NgbActiveModal;
    alertService: AlertService;
    project: Project;
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

  fillFormFromDb(projectId)
  {
    if(projectId == null || projectId < 0)
    {
      this.project = new Project();
      console.log("no existing project found with given id.");
      console.log(this.project);
      return;
    }
    console.log(this.student);

    for(let s of this.student.projects) {
      if(s.id == projectId)
      {
        this.project = s;
        console.log(this.project);
        break;
      }
    }
  }

  
  private save ()
  {
    if(this._componentId == null || this._componentId < 0)
    {
        console.log("create new project");
        this.createNew();
    }
    else
    {
        console.log("update project");
        this.update();
    }
  }

  private createNew ()
  {
    this.information = "Created";
    this.studentService.createProject(this.project, this.student.id)
                .subscribe((res: Project) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
  }

  private delete()
  {
        this.information = "Deleted";
        this.studentService.deleteProject(this.project, this.student.id, this.project.id).subscribe(response => {
            this.onSaveSuccess(null), (res: Response) => this.onSaveError(res.json())
        });
    }

  private update () 
  {
        this.information = "Updated";
        this.isSaving = true;
        if (this.project.id !== undefined) {
            this.studentService.updateProject(this.project, this.student.id, this.project.id)
                .subscribe((res: Project) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.studentService.create(this.student)
                .subscribe((res: Project) =>
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