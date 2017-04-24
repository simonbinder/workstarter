import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { SearchingComponent } from './searching.component';
import { SearchingDetailComponent } from './searching-detail.component';
import { SearchingPopupComponent } from './searching-dialog.component';
import { SearchingDeletePopupComponent } from './searching-delete-dialog.component';

import { Principal } from '../../shared';


export const searchingRoute: Routes = [
  {
    path: 'searching',
    component: SearchingComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.searching.home.title'
    }
  }, {
    path: 'searching/:id',
    component: SearchingDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.searching.home.title'
    }
  }
];

export const searchingPopupRoute: Routes = [
  {
    path: 'searching-new',
    component: SearchingPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.searching.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'searching/:id/edit',
    component: SearchingPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.searching.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'searching/:id/delete',
    component: SearchingDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.searching.home.title'
    },
    outlet: 'popup'
  }
];
