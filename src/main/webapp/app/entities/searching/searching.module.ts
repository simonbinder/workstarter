import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WorkstarterSharedModule } from '../../shared';

import {
    SearchingService,
    SearchingPopupService,
    SearchingComponent,
    SearchingDetailComponent,
    SearchingDialogComponent,
    SearchingPopupComponent,
    SearchingDeletePopupComponent,
    SearchingDeleteDialogComponent,
    searchingRoute,
    searchingPopupRoute,
} from './';

let ENTITY_STATES = [
    ...searchingRoute,
    ...searchingPopupRoute,
];

@NgModule({
    imports: [
        WorkstarterSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        SearchingComponent,
        SearchingDetailComponent,
        SearchingDialogComponent,
        SearchingDeleteDialogComponent,
        SearchingPopupComponent,
        SearchingDeletePopupComponent,
    ],
    entryComponents: [
        SearchingComponent,
        SearchingDialogComponent,
        SearchingPopupComponent,
        SearchingDeleteDialogComponent,
        SearchingDeletePopupComponent,
    ],
    providers: [
        SearchingService,
        SearchingPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorkstarterSearchingModule {}
