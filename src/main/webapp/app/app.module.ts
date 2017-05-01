import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ng2-webstorage';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { WorkstarterSharedModule, UserRouteAccessService } from './shared';
import { WorkstarterHomeModule } from './home/home.module';
import { WorkstarterAdminModule } from './admin/admin.module';
import { WorkstarterAccountModule } from './account/account.module';
import { WorkstarterEntityModule } from './entities/entity.module';

import { LayoutRoutingModule } from './layouts';
import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';


import { MainmenuComponent } from './mycomponents/mainmenu/mainmenu.component';
import { CvComponent } from './mycomponents/cv/cv.component';

import {
    JhiMainComponent,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ActiveMenuDirective,
    ErrorComponent
} from './layouts';


@NgModule({
    imports: [
        NgbModule.forRoot(),
        BrowserModule,
        LayoutRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        WorkstarterSharedModule,
        WorkstarterHomeModule,
        WorkstarterAdminModule,
        WorkstarterAccountModule,
        WorkstarterEntityModule
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        MainmenuComponent,
        CvComponent,
        ErrorComponent,
        PageRibbonComponent,
        ActiveMenuDirective,
        FooterComponent
    ],
    providers: [
        ProfileService,
        { provide: Window, useValue: window },
        { provide: Document, useValue: document },
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService
    ],
    bootstrap: [ JhiMainComponent ]
})
export class WorkstarterAppModule {}
