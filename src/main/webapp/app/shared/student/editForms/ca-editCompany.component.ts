import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService, AlertService } from 'ng-jhipster';
import { CompanyAdmin } from "../../../entities/company-admin/company-admin.model";
import { CompanyAdminService } from "../../../entities/company-admin/company-admin.service";
import { Company } from "../../../entities/company/company.model";
import { Response } from '@angular/http';


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
    this.company = this.companyAdmin.company;

    let copy: CompanyAdmin = Object.assign({}, this.companyAdmin);
    console.log(this.companyAdmin);
    console.log(this.company);
  }


  
  private save ()
  {
        console.log("update company");
        this.companyAdmin
        this.update();
  }


  private update () 
  {
        this.information = "Updated";
        this.isSaving = true;
        this.companyAdminService.updateAccountInfo(this.companyAdmin)
            .subscribe((res: Response) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    
    }

    private onSaveSuccess (result: CompanyAdmin) {
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