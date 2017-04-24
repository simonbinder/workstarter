import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { CompanyAdminComponent } from './company-admin.component';
import { CompanyAdminDetailComponent } from './company-admin-detail.component';
import { CompanyAdminPopupComponent } from './company-admin-dialog.component';
import { CompanyAdminDeletePopupComponent } from './company-admin-delete-dialog.component';

import { Principal } from '../../shared';


export const companyAdminRoute: Routes = [
  {
    path: 'company-admin',
    component: CompanyAdminComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.companyAdmin.home.title'
    }
  }, {
    path: 'company-admin/:id',
    component: CompanyAdminDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.companyAdmin.home.title'
    }
  }
];

export const companyAdminPopupRoute: Routes = [
  {
    path: 'company-admin-new',
    component: CompanyAdminPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.companyAdmin.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'company-admin/:id/edit',
    component: CompanyAdminPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.companyAdmin.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'company-admin/:id/delete',
    component: CompanyAdminDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.companyAdmin.home.title'
    },
    outlet: 'popup'
  }
];
