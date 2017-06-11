import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { ProfessionComponent } from './profession.component';
import { ProfessionDetailComponent } from './profession-detail.component';
import { ProfessionPopupComponent } from './profession-dialog.component';
import { ProfessionDeletePopupComponent } from './profession-delete-dialog.component';

import { Principal } from '../../shared';


export const professionRoute: Routes = [
  {
    path: 'profession',
    component: ProfessionComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.profession.home.title'
    }
  }, {
    path: 'profession/:id',
    component: ProfessionDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.profession.home.title'
    }
  }
];

export const professionPopupRoute: Routes = [
  {
    path: 'profession-new',
    component: ProfessionPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.profession.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'profession/:id/edit',
    component: ProfessionPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.profession.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'profession/:id/delete',
    component: ProfessionDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.profession.home.title'
    },
    outlet: 'popup'
  }
];
