
export class School {
    constructor(
        public id?: number,
        public name?: string,
        public address?: string,
        public coreSubjects?: string,
        public targetDegree?: string,
        public start?: any,
        public end?: any,
    ) {
        this.id = id ? id : null;
        this.name = name ? name : null;
        this.address = address ? address : null;
        this.coreSubjects = coreSubjects ? coreSubjects : null;
        this.targetDegree = targetDegree ? targetDegree : null;
        this.start = start ? start : null;
        this.end = end ? end : null;
    }
}
