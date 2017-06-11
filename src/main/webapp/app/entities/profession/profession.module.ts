import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WorkstarterSharedModule } from '../../shared';

import {
    ProfessionService,
    ProfessionPopupService,
    ProfessionComponent,
    ProfessionDetailComponent,
    ProfessionDialogComponent,
    ProfessionPopupComponent,
    ProfessionDeletePopupComponent,
    ProfessionDeleteDialogComponent,
    professionRoute,
    professionPopupRoute,
} from './';

let ENTITY_STATES = [
    ...professionRoute,
    ...professionPopupRoute,
];

@NgModule({
    imports: [
        WorkstarterSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ProfessionComponent,
        ProfessionDetailComponent,
        ProfessionDialogComponent,
        ProfessionDeleteDialogComponent,
        ProfessionPopupComponent,
        ProfessionDeletePopupComponent,
    ],
    entryComponents: [
        ProfessionComponent,
        ProfessionDialogComponent,
        ProfessionPopupComponent,
        ProfessionDeleteDialogComponent,
        ProfessionDeletePopupComponent,
    ],
    providers: [
        ProfessionService,
        ProfessionPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorkstarterProfessionModule {}
