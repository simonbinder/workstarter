import { Student } from '../student';
export class Profession {
    constructor(
        public id?: number,
        public position?: string,
        public formOfEmployment?: string,
        public tasks?: string,
        public companyName?: string,
        public domain?: string,
        public sector?: string,
        public location?: string,
        public startDate?: any,
        public endDate?: any,
        public manyToOne?: Student,
    ) {
        this.id = id ? id : null;
        this.position = position ? position : null;
        this.formOfEmployment = formOfEmployment ? formOfEmployment : null;
        this.tasks = tasks ? tasks : null;
        this.companyName = companyName ? companyName : null;
        this.domain = domain ? domain : null;
        this.sector = sector ? sector : null;
        this.location = location ? location : null;
        this.startDate = startDate ? startDate : null;
        this.endDate = endDate ? endDate : null;
        this.manyToOne = manyToOne ? manyToOne : null;
    }
}
