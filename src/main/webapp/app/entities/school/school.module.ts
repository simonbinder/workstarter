import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WorkstarterSharedModule } from '../../shared';

import {
    SchoolService,
    SchoolPopupService,
    SchoolComponent,
    SchoolDetailComponent,
    SchoolDialogComponent,
    SchoolPopupComponent,
    SchoolDeletePopupComponent,
    SchoolDeleteDialogComponent,
    schoolRoute,
    schoolPopupRoute,
} from './';

let ENTITY_STATES = [
    ...schoolRoute,
    ...schoolPopupRoute,
];

@NgModule({
    imports: [
        WorkstarterSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        SchoolComponent,
        SchoolDetailComponent,
        SchoolDialogComponent,
        SchoolDeleteDialogComponent,
        SchoolPopupComponent,
        SchoolDeletePopupComponent,
    ],
    entryComponents: [
        SchoolComponent,
        SchoolDialogComponent,
        SchoolPopupComponent,
        SchoolDeleteDialogComponent,
        SchoolDeletePopupComponent,
    ],
    providers: [
        SchoolService,
        SchoolPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorkstarterSchoolModule {}
