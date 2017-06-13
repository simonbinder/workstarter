import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WorkstarterSharedModule } from '../../shared';

import {
    KeywordsService,
    KeywordsPopupService,
    KeywordsComponent,
    KeywordsDetailComponent,
    KeywordsDialogComponent,
    KeywordsPopupComponent,
    KeywordsDeletePopupComponent,
    KeywordsDeleteDialogComponent,
    keywordsRoute,
    keywordsPopupRoute,
} from './';

let ENTITY_STATES = [
    ...keywordsRoute,
    ...keywordsPopupRoute,
];

@NgModule({
    imports: [
        WorkstarterSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        KeywordsComponent,
        KeywordsDetailComponent,
        KeywordsDialogComponent,
        KeywordsDeleteDialogComponent,
        KeywordsPopupComponent,
        KeywordsDeletePopupComponent,
    ],
    entryComponents: [
        KeywordsComponent,
        KeywordsDialogComponent,
        KeywordsPopupComponent,
        KeywordsDeleteDialogComponent,
        KeywordsDeletePopupComponent,
    ],
    providers: [
        KeywordsService,
        KeywordsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorkstarterKeywordsModule {}
