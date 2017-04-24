import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { JobadvertismentComponent } from './jobadvertisment.component';
import { JobadvertismentDetailComponent } from './jobadvertisment-detail.component';
import { JobadvertismentPopupComponent } from './jobadvertisment-dialog.component';
import { JobadvertismentDeletePopupComponent } from './jobadvertisment-delete-dialog.component';

import { Principal } from '../../shared';


export const jobadvertismentRoute: Routes = [
  {
    path: 'jobadvertisment',
    component: JobadvertismentComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.jobadvertisment.home.title'
    }
  }, {
    path: 'jobadvertisment/:id',
    component: JobadvertismentDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.jobadvertisment.home.title'
    }
  }
];

export const jobadvertismentPopupRoute: Routes = [
  {
    path: 'jobadvertisment-new',
    component: JobadvertismentPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.jobadvertisment.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'jobadvertisment/:id/edit',
    component: JobadvertismentPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.jobadvertisment.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'jobadvertisment/:id/delete',
    component: JobadvertismentDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.jobadvertisment.home.title'
    },
    outlet: 'popup'
  }
];
