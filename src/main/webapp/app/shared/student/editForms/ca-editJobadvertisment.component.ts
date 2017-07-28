import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService, AlertService } from 'ng-jhipster';
import { Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { CompanyAdminService } from "../../../entities/company-admin/company-admin.service";
import { Company } from "../../../entities/company/company.model";
import { Jobadvertisment } from "../../../entities/jobadvertisment/jobadvertisment.model";


@Component({
  selector: 'ca-editJobadvertisment',
  templateUrl: './ca-editJobadvertisment.component.html',
  styleUrls: [
        '../../editView/editView.scss'
    ]
})

export class CaEditJobadvertisment implements OnInit {
    activeModal: NgbActiveModal;
    alertService: AlertService;
    isSaving: boolean;
    information: string;
    company: Company;
    jobadvertisment: Jobadvertisment;

  @Input() _componentId;
  @Input() _student: any;




  constructor(
    private languageService: JhiLanguageService,
    private companyAdminService: CompanyAdminService,
    private eventManager: EventManager
    
  ) 
  {
  }

  ngOnInit() {
    this.languageService.addLocation('editView');
    this.company = this._student;
    this.fillFormFromDb(this._componentId);
  }

  fillFormFromDb(professionsId)
  {
    if(professionsId == null || professionsId < 0)
    {
      this.jobadvertisment = new Jobadvertisment();
      console.log("no existing jobadvertisment found with given id.");
      console.log(this.jobadvertisment);
      return;
    }
    console.log(this.company);

    for(let job of this.company.jobs) {
      if(job.id == professionsId)
      {
        this.jobadvertisment = job;
        console.log(this.jobadvertisment);
        break;
      }
    }
  }

  
  private save ()
  {
    if(this._componentId == null || this._componentId < 0)
    {
        console.log("create new jobadvertisment");
        this.createNew();
    }
    else
    {
        console.log("update jobadvertisment");
        this.update();
    }
  }

  private createNew ()
  {
    this.information = "Created";
    this.companyAdminService.createProfession(this.jobadvertisment, this.company.id)
                .subscribe((res: Jobadvertisment) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
  }

  private delete()
  {
        this.information = "Deleted";
        this.companyAdminService.deleteProfession(this.jobadvertisment, this.company.id, this.jobadvertisment.id).subscribe(response => {
            this.onSaveSuccess(null), (res: Response) => this.onSaveError(res.json())
        });
    }

  private update () 
  {
        this.information = "Updated";
        this.isSaving = true;
        if (this.jobadvertisment.id !== undefined) {
            this.companyAdminService.updateProfession(this.jobadvertisment, this.company.id, this.jobadvertisment.id)
                .subscribe((res: Jobadvertisment) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } 
        else {
            this.companyAdminService.create(this.company)
                .subscribe((res: Jobadvertisment) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: Company) {
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