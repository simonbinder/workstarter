import { Company } from '../company';
export class Jobadvertisment {
    constructor(
        public id?: number,
        public jobname?: string,
        public description?: string,
        public title?: string,
        public exercises?: string,
        public contact?: string,
        public location?: string,
        public tasks?: string,
        public company?: Company,
    ) {
    }
}
