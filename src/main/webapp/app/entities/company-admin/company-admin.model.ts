import { Company } from '../company';
export class CompanyAdmin {
    constructor(
        public id?: number,
        public firstname?: string,
        public lastname?: string,
        public companies?: Company,
        public company?: Company,
    ) {
    }
}
