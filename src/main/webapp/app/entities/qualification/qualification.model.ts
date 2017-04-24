import { Resume } from '../resume';
export class Qualification {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public resume?: Resume,
    ) {
    }
}
