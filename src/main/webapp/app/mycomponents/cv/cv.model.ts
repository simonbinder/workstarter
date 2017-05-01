export class User {
    public firstName?: string;
    public lastName?: string;

    public email?: string;
    public image?: string;
    public status?: string;

    constructor(
        firstName?: string,
        lastName?: string,
        email?: string,
        image?: string,
        status?: string,
    ) {
        this.firstName = firstName ? firstName : null;
        this.lastName = lastName ? lastName : null;
        this.email = email ? email : null;
        this.image = image ? image : null;
        this.status = status ? status : null;
        

    }
}
