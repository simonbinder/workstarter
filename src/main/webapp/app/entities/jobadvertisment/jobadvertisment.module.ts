import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WorkstarterSharedModule } from '../../shared';

import {
    JobadvertismentService,
    JobadvertismentPopupService,
    JobadvertismentComponent,
    JobadvertismentDetailComponent,
    JobadvertismentDialogComponent,
    JobadvertismentPopupComponent,
    JobadvertismentDeletePopupComponent,
    JobadvertismentDeleteDialogComponent,
    jobadvertismentRoute,
    jobadvertismentPopupRoute,
} from './';

let ENTITY_STATES = [
    ...jobadvertismentRoute,
    ...jobadvertismentPopupRoute,
];

@NgModule({
    imports: [
        WorkstarterSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        JobadvertismentComponent,
        JobadvertismentDetailComponent,
        JobadvertismentDialogComponent,
        JobadvertismentDeleteDialogComponent,
        JobadvertismentPopupComponent,
        JobadvertismentDeletePopupComponent,
    ],
    entryComponents: [
        JobadvertismentComponent,
        JobadvertismentDialogComponent,
        JobadvertismentPopupComponent,
        JobadvertismentDeleteDialogComponent,
        JobadvertismentDeletePopupComponent,
    ],
    providers: [
        JobadvertismentService,
        JobadvertismentPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorkstarterJobadvertismentModule {}
