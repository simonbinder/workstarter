import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WorkstarterSharedModule } from '../../shared';

import {
    StudentService,
    StudentPopupService,
    StudentComponent,
    StudentDetailComponent,
    StudentDialogComponent,
    StudentPopupComponent,
    StudentDeletePopupComponent,
    StudentDeleteDialogComponent,
    studentRoute,
    studentPopupRoute,
} from './';

let ENTITY_STATES = [
    ...studentRoute,
    ...studentPopupRoute,
];

@NgModule({
    imports: [
        WorkstarterSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        StudentComponent,
        StudentDetailComponent,
        StudentDialogComponent,
        StudentDeleteDialogComponent,
        StudentPopupComponent,
        StudentDeletePopupComponent,
    ],
    entryComponents: [
        StudentComponent,
        StudentDialogComponent,
        StudentPopupComponent,
        StudentDeleteDialogComponent,
        StudentDeletePopupComponent,
    ],
    providers: [
        StudentService,
        StudentPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorkstarterStudentModule {}
