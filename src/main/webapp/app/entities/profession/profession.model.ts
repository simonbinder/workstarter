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
    }
}
