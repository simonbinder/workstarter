import { Resume } from '../resume';
export class School {
    constructor(
        public id?: number,
        public name?: string,
        public address?: string,
        public coreSubjects?: string,
        public resume?: Resume,
    ) {
    }
}
