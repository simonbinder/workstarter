import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WorkstarterSharedModule } from '../../shared';

import {
    CompanyService,
    CompanyPopupService,
    CompanyComponent,
    CompanyDetailComponent,
    CompanyDialogComponent,
    CompanyPopupComponent,
    CompanyDeletePopupComponent,
    CompanyDeleteDialogComponent,
    companyRoute,
    companyPopupRoute,
} from './';

let ENTITY_STATES = [
    ...companyRoute,
    ...companyPopupRoute,
];

@NgModule({
    imports: [
        WorkstarterSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CompanyComponent,
        CompanyDetailComponent,
        CompanyDialogComponent,
        CompanyDeleteDialogComponent,
        CompanyPopupComponent,
        CompanyDeletePopupComponent,
    ],
    entryComponents: [
        CompanyComponent,
        CompanyDialogComponent,
        CompanyPopupComponent,
        CompanyDeleteDialogComponent,
        CompanyDeletePopupComponent,
    ],
    providers: [
        CompanyService,
        CompanyPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorkstarterCompanyModule {}
