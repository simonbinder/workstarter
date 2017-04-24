import { Student } from '../student';
import { School } from '../school';
import { Company } from '../company';
import { Qualification } from '../qualification';
export class Resume {
    constructor(
        public id?: number,
        public title?: string,
        public description?: string,
        public student?: Student,
        public schools?: School,
        public companies?: Company,
        public internships?: Company,
        public qualifications?: Qualification,
    ) {
    }
}
