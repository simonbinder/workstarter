import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WorkstarterSharedModule } from '../../shared';

import {
    ResumeService,
    ResumePopupService,
    ResumeComponent,
    ResumeDetailComponent,
    ResumeDialogComponent,
    ResumePopupComponent,
    ResumeDeletePopupComponent,
    ResumeDeleteDialogComponent,
    resumeRoute,
    resumePopupRoute,
} from './';

let ENTITY_STATES = [
    ...resumeRoute,
    ...resumePopupRoute,
];

@NgModule({
    imports: [
        WorkstarterSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ResumeComponent,
        ResumeDetailComponent,
        ResumeDialogComponent,
        ResumeDeleteDialogComponent,
        ResumePopupComponent,
        ResumeDeletePopupComponent,
    ],
    entryComponents: [
        ResumeComponent,
        ResumeDialogComponent,
        ResumePopupComponent,
        ResumeDeleteDialogComponent,
        ResumeDeletePopupComponent,
    ],
    providers: [
        ResumeService,
        ResumePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorkstarterResumeModule {}
