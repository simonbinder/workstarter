import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { KeywordsComponent } from './keywords.component';
import { KeywordsDetailComponent } from './keywords-detail.component';
import { KeywordsPopupComponent } from './keywords-dialog.component';
import { KeywordsDeletePopupComponent } from './keywords-delete-dialog.component';

import { Principal } from '../../shared';


export const keywordsRoute: Routes = [
  {
    path: 'keywords',
    component: KeywordsComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.keywords.home.title'
    }
  }, {
    path: 'keywords/:id',
    component: KeywordsDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.keywords.home.title'
    }
  }
];

export const keywordsPopupRoute: Routes = [
  {
    path: 'keywords-new',
    component: KeywordsPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.keywords.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'keywords/:id/edit',
    component: KeywordsPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.keywords.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'keywords/:id/delete',
    component: KeywordsDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'workstarterApp.keywords.home.title'
    },
    outlet: 'popup'
  }
];
