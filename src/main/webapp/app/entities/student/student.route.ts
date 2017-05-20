import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { StudentComponent } from './student.component';
import { StudentDetailComponent } from './student-detail.component';
import { StudentPopupComponent } from './student-dialog.component';
import { StudentDeletePopupComponent } from './student-delete-dialog.component';

import { Principal } from '../../shared';


export const studentRoute: Routes = [
  {
    path: 'student',
    component: StudentComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.student.home.title'
    },
  }, {
    path: 'student/:id',
    component: StudentDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.student.home.title'
    }
  }
];

export const studentPopupRoute: Routes = [
  {
    path: 'student-new',
    component: StudentPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.student.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'student/:id/edit',
    component: StudentPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.student.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'student/:id/delete',
    component: StudentDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.student.home.title'
    },
    outlet: 'popup'
  }
];
