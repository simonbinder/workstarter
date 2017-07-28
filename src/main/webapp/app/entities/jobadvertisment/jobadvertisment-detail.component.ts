import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { Jobadvertisment } from './jobadvertisment.model';
import { JobadvertismentService } from './jobadvertisment.service';
import { EditViewModalService, AccountService } from "../../shared/index";
import { NgbModalRef } from "@ng-bootstrap/ng-bootstrap";
import { Company } from "../company/company.model";
import { CompanyAdminService } from "../company-admin/company-admin.service";

@Component({
    selector: 'jhi-jobadvertisment-detail',
    templateUrl: './jobadvertisment-detail.component.html',
    styleUrls: [
        'jobadvertisment.scss'
    ]
})
export class JobadvertismentDetailComponent implements OnInit, OnDestroy {

    jobadvertisment: Jobadvertisment;
    companyAdminId: number;
    isMyAccount: boolean = false;
    private subscription: any;
    modalRef: NgbModalRef;
    company: Company;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private jobadvertismentService: JobadvertismentService,
        private route: ActivatedRoute,
        private editViewModalService: EditViewModalService,
        private companyAdminService: CompanyAdminService,
        private account: AccountService,
    ) {
        this.jhiLanguageService.setLocations(['jobadvertisment']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
            this.companyAdminId = +params['companyadmin'];
            this.setPrivateAccountSettings(this.companyAdminId);
            this.loadCa(this.companyAdminId);
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

    load(id) {
        this.jobadvertismentService.find(id).subscribe(jobadvertisment => {
            this.jobadvertisment = jobadvertisment;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

    apply()
    {
        this.modalRef = this.editViewModalService.open("jobApply", this.jobadvertisment, this.companyAdminId);
    }

    edit() {
        console.log(this.company);
        this.modalRef = this.editViewModalService.open("editJobadvertisment", this.company, this.jobadvertisment.id);
    }

    loadCa (id) {
        this.companyAdminService.find(id).subscribe(companyAdmin => {
            this.company = companyAdmin.company;
        });
    }

}
