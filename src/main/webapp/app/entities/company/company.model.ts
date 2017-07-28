import { CompanyAdmin } from '../company-admin';
import { Resume } from '../resume';
import { Jobadvertisment } from '../jobadvertisment';
export class Company {

    public jobs?: any[];
    constructor(
        public id?: number,
        public companyName?: string,
        public companyAdmin?: CompanyAdmin,
        public resume?: Resume,
        public jobadvertisment?: Jobadvertisment,
        public admins?: CompanyAdmin,
        jobs?: any[],
    ) {
        this.jobs = jobs ? jobs : null;
    }
}
