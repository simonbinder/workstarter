import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService, AlertService } from 'ng-jhipster';
import { CompanyAdmin } from "../../../entities/company-admin/company-admin.model";
import { CompanyAdminService } from "../../../entities/company-admin/company-admin.service";
import { Company } from "../../../entities/company/company.model";


@Component({
  selector: 'ca-editCompany',
  templateUrl: './ca-editCompany.component.html',
  styleUrls: [
        '../../editView/editView.scss'
    ]
})

export class CaEditCompany implements OnInit {
    activeModal: NgbActiveModal;
    alertService: AlertService;
    company: Company;
    isSaving: boolean;
    information: string;

  @Input() _componentId;
  @Input() _student: any;


  companyAdmin: CompanyAdmin;


  constructor(
    private languageService: JhiLanguageService,
    private companyAdminService: CompanyAdminService,
    private eventManager: EventManager
    
  ) 
  {
  }

  ngOnInit() {
    this.languageService.addLocation('editView');
    this.companyAdmin = this._student;
    this.company = this.companyAdmin.companies[0];
    console.log(this.companyAdmin);
    console.log(this.company);
  }


  
  private save ()
  {
    if(this._componentId == null || this._componentId < 0)
    {
        console.log("create new company");
        this.createNew();
    }
    else
    {
        console.log("update company");
        this.update();
    }
  }

  private createNew ()
  {
    this.information = "Created";
    this.companyAdminService.createcompany(this.company, this.companyAdmin.id)
                .subscribe((res: company) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
  }

  private delete()
  {
        this.information = "Deleted";
        this.companyAdminService.deletecompany(this.company, this.companyAdmin.id, this.company.id).subscribe(response => {
            this.onSaveSuccess(null), (res: Response) => this.onSaveError(res.json())
        });
    }

  private update () 
  {
        this.information = "Updated";
        this.isSaving = true;
        if (this.company.id !== undefined) {
            this.companyAdminService.updatecompany(this.company, this.companyAdmin.id, this.company.id)
                .subscribe((res: company) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.companyAdminService.create(this.companyAdmin)
                .subscribe((res: company) =>
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