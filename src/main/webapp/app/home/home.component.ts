import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';

import { Account, User, LoginModalService, Principal, UserService, AccountService } from '../shared';
import { Router } from '@angular/router';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [
        'home.scss'
    ]
})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    user: User;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: EventManager,
        private router: Router,
        private userService: UserService,
        private accountService: AccountService,
    ) {
        this.jhiLanguageService.setLocations(['home']);
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.account = account;
            if(this.isAuthenticated()){
                console.log("Your user id is: " + this.account.id);
                // Check if Student or CompanyAdmin
                this.gotoStudent(this.account.id);
            }
        });
        this.registerAuthenticationSuccess();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    gotoStudent(id: Number): void {
        this.router.navigate(['/student', id]);
    }

    gotoCompanyAdmin(id: Number): void {
        this.router.navigate(['/company-admin', id]);
    }

    gotoAccount(AccountId)
    {
        this.accountService.getUser(AccountId).toPromise().then(user => {

            console.log("I am a " + user.user);
            if (user.user== "CompanyAdmin")
            {
                this.gotoCompanyAdmin(AccountId);
            }
            else
            {
                this.gotoStudent(AccountId);
            }
        });
    }
}
