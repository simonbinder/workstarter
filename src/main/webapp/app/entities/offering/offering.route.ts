import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { OfferingComponent } from './offering.component';
import { OfferingDetailComponent } from './offering-detail.component';
import { OfferingPopupComponent } from './offering-dialog.component';
import { OfferingDeletePopupComponent } from './offering-delete-dialog.component';

import { Principal } from '../../shared';


export const offeringRoute: Routes = [
  {
    path: 'offering',
    component: OfferingComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.offering.home.title'
    }
  }, {
    path: 'offering/:id',
    component: OfferingDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.offering.home.title'
    }
  }
];

export const offeringPopupRoute: Routes = [
  {
    path: 'offering-new',
    component: OfferingPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.offering.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'offering/:id/edit',
    component: OfferingPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.offering.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'offering/:id/delete',
    component: OfferingDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.offering.home.title'
    },
    outlet: 'popup'
  }
];
