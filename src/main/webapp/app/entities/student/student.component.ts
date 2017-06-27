import { Component, OnInit, OnDestroy } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService } from 'ng-jhipster';

import { Student } from './student.model';
import { StudentService } from './student.service';
import { ITEMS_PER_PAGE, Principal, SharedStudentService, AccountService } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { CompanyAdmin } from "../company-admin/company-admin.model";

@Component({
    selector: 'jhi-student',
    templateUrl: './student.component.html',
    styleUrls: [
        'search.scss'
    ],
})
export class StudentComponent implements OnInit, OnDestroy {
    students: Student[];
    companyAdmins: CompanyAdmin[];
    searchResults: any[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    message: any;
    messageSubscriber: Subscription;

    constructor(
        private accountService: AccountService,
        private jhiLanguageService: JhiLanguageService,
        private studentService: StudentService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private sharedStudentService : SharedStudentService,
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
        this.jhiLanguageService.setLocations(['student']);
    }

    loadAll() {
        if (this.currentSearch) {
            this.studentService.searchAll({
                query: this.currentSearch,
                }).subscribe(
                    (res: Response) => {
                        this.searchResults = res.json();
                        console.log(this.searchResults);
                        this.sortUsers(this.searchResults);
                    },
                    (res: Response) => this.onError(res.json())

                );
            return;
       }
        this.studentService.query().subscribe(
            (res: Response) => {
                this.searchResults = res.json();
                this.currentSearch = '';
            },
            (res: Response) => this.onError(res.json())
        );
    }

    fetchMessage(): void{
        this.messageSubscriber = this.sharedStudentService.getMessage().subscribe(message => { 
            this.message = message;
            this.search(this.message.text);
     });
    }


    search (query) {
        this.students = new Array();
        this.companyAdmins = new Array();

        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    private sortUsers(searchResults: any[])
    {
        searchResults.forEach(user => {
            this.accountService.getUser(user.id).toPromise().then(u => {
                if (u.user== "CompanyAdmin")
                {
                    if(this.isCompanyValid(user))
                    this.companyAdmins.push(user);
                }
                else
                {
                    this.students.push(user);
                }
            });
        });
        console.log(this.currentSearch);
        console.log("Studenten:");
        console.log(this.students);
        console.log("CompanyAdmins:");
        console.log(this.companyAdmins);
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInStudents();
        this.fetchMessage();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackIdStudent (index: number, item: any) {
        return item.id;
    }

    trackIdCompanyAdmin (index: number, item: CompanyAdmin) {
        return item.id;
    }

    registerChangeInStudents() {
        this.eventSubscriber = this.eventManager.subscribe('studentListModification', (response) => this.loadAll());
    }

    private isCompanyValid(companyAdmin)
    {
        if(companyAdmin.company != null)
        {
            if(companyAdmin.company.companyName != null)
            {
                return true;
            }
        }
        return false;
    }


    private onError (error) {
        this.alertService.error(error.message, null, null);
    }
}
