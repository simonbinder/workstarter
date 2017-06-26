import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService, EventManager } from 'ng-jhipster';
import { CompanyAdmin } from './company-admin.model';
import { CompanyAdminService } from './company-admin.service';
import { NgbModalRef } from "@ng-bootstrap/ng-bootstrap";
import { Subscription } from "rxjs/Subscription";
import { AccountService, EditViewModalService } from "../../shared/index";

@Component({
    selector: 'jhi-company-admin-detail',
    templateUrl: './company-admin-detail.component.html',
    styleUrls: [
        'company-admin.scss'
    ]
})
export class CompanyAdminDetailComponent implements OnInit, OnDestroy {

    
    companyAdmin: CompanyAdmin;
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
        private companyAdminService: CompanyAdminService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['company-admin']);
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

    edit(editComponent: string, companyAdmin: CompanyAdmin, componentId: number) {
        this.modalRef = this.editViewModalService.open(editComponent, companyAdmin, componentId);
    }


    load (id) {
        this.companyAdminService.find(id).subscribe(companyAdmin => {
            this.companyAdmin = companyAdmin;
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
        this.eventSubscriber = this.eventManager.subscribe('EditFormsFinished', (response) => this.realoadCompanyAdmin(response));
    }

    private realoadCompanyAdmin(information)
    {   
        console.log(information);
        if(information.content == "Created" || information.content == "Deleted")
        {
            this.load(this.companyAdmin.id);
        }
        console.log(this.companyAdmin);
    }

}
