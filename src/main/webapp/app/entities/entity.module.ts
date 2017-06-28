import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { WorkstarterStudentModule } from './student/student.module';
import { WorkstarterCompanyAdminModule } from "./company-admin/company-admin.module";
import { WorkstarterJobadvertismentModule } from "./jobadvertisment/jobadvertisment.module";
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        WorkstarterStudentModule,
        WorkstarterCompanyAdminModule,
        WorkstarterJobadvertismentModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorkstarterEntityModule {}
