import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { ResumeComponent } from './resume.component';
import { ResumeDetailComponent } from './resume-detail.component';
import { ResumePopupComponent } from './resume-dialog.component';
import { ResumeDeletePopupComponent } from './resume-delete-dialog.component';

import { Principal } from '../../shared';


export const resumeRoute: Routes = [
  {
    path: 'resume',
    component: ResumeComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.resume.home.title'
    }
  }, {
    path: 'resume/:id',
    component: ResumeDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.resume.home.title'
    }
  }
];

export const resumePopupRoute: Routes = [
  {
    path: 'resume-new',
    component: ResumePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.resume.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'resume/:id/edit',
    component: ResumePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.resume.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'resume/:id/delete',
    component: ResumeDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.resume.home.title'
    },
    outlet: 'popup'
  }
];
