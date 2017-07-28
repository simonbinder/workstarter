import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { DatePipe } from '@angular/common';

import { CookieService } from 'angular2-cookie/services/cookies.service';
import {
    WorkstarterSharedLibsModule,
    WorkstarterSharedCommonModule,
    CSRFService,
    AuthService,
    AuthServerProvider,
    AccountService,
    UserService,
    StateStorageService,
    LoginService,
    EditViewService,
    LoginModalService,
    EditViewModalService,
    Principal,
    JhiTrackerService,
    HasAnyAuthorityDirective,
    JhiSocialComponent,
    SocialService,
    JhiLoginModalComponent,
    StudentEditJobs,
    JobApply,
    StudentEditSchools,
    StudentEditProject,
    StudentEditKeywords,
    StudentEditPresentation,
    CaEditCompany,
    JhiEditViewModalComponent,
    SharedStudentService
} from './';

@NgModule({
    imports: [
        WorkstarterSharedLibsModule,
        WorkstarterSharedCommonModule
    ],
    declarations: [
        JhiSocialComponent,
        JhiLoginModalComponent,
        StudentEditJobs,
        JobApply,
        StudentEditSchools,
        StudentEditProject,
        StudentEditKeywords,
        StudentEditPresentation,
        CaEditCompany,
        JhiEditViewModalComponent,
        HasAnyAuthorityDirective
    ],
    providers: [
        CookieService,
        LoginService,
        EditViewService,
        LoginModalService,
        EditViewModalService,
        AccountService,
        StateStorageService,
        Principal,
        CSRFService,
        JhiTrackerService,
        AuthServerProvider,
        SocialService,
        AuthService,
        UserService,
        DatePipe,
        SharedStudentService
    ],
    entryComponents: [
        JhiLoginModalComponent, 
        JhiEditViewModalComponent],
    exports: [
        WorkstarterSharedCommonModule,
        JhiSocialComponent,
        StudentEditJobs,
        JobApply,
        JhiLoginModalComponent,
        JhiEditViewModalComponent,
        HasAnyAuthorityDirective,
        DatePipe
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class WorkstarterSharedModule {}
