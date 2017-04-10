import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WorkstarterSharedModule } from '../../shared';

import {
    OfferingService,
    OfferingPopupService,
    OfferingComponent,
    OfferingDetailComponent,
    OfferingDialogComponent,
    OfferingPopupComponent,
    OfferingDeletePopupComponent,
    OfferingDeleteDialogComponent,
    offeringRoute,
    offeringPopupRoute,
} from './';

let ENTITY_STATES = [
    ...offeringRoute,
    ...offeringPopupRoute,
];

@NgModule({
    imports: [
        WorkstarterSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        OfferingComponent,
        OfferingDetailComponent,
        OfferingDialogComponent,
        OfferingDeleteDialogComponent,
        OfferingPopupComponent,
        OfferingDeletePopupComponent,
    ],
    entryComponents: [
        OfferingComponent,
        OfferingDialogComponent,
        OfferingPopupComponent,
        OfferingDeleteDialogComponent,
        OfferingDeletePopupComponent,
    ],
    providers: [
        OfferingService,
        OfferingPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorkstarterOfferingModule {}
