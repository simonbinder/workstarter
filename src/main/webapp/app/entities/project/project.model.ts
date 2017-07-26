import { Portfolio } from '../portfolio';
export class Project {
    constructor(
        public id?: number,
        public title?: string,
        public year?: any,
        public imageUrl?: string,
        public description?: string,
        public link?: string,
        public context?: string,
        public portfolio?: Portfolio,
    ) {
    }
}
