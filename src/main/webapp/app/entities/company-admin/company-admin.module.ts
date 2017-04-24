import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WorkstarterSharedModule } from '../../shared';

import {
    CompanyAdminService,
    CompanyAdminPopupService,
    CompanyAdminComponent,
    CompanyAdminDetailComponent,
    CompanyAdminDialogComponent,
    CompanyAdminPopupComponent,
    CompanyAdminDeletePopupComponent,
    CompanyAdminDeleteDialogComponent,
    companyAdminRoute,
    companyAdminPopupRoute,
} from './';

let ENTITY_STATES = [
    ...companyAdminRoute,
    ...companyAdminPopupRoute,
];

@NgModule({
    imports: [
        WorkstarterSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CompanyAdminComponent,
        CompanyAdminDetailComponent,
        CompanyAdminDialogComponent,
        CompanyAdminDeleteDialogComponent,
        CompanyAdminPopupComponent,
        CompanyAdminDeletePopupComponent,
    ],
    entryComponents: [
        CompanyAdminComponent,
        CompanyAdminDialogComponent,
        CompanyAdminPopupComponent,
        CompanyAdminDeleteDialogComponent,
        CompanyAdminDeletePopupComponent,
    ],
    providers: [
        CompanyAdminService,
        CompanyAdminPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorkstarterCompanyAdminModule {}
