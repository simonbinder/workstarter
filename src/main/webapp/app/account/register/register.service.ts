import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Rx';

@Injectable()
export class Register {

    constructor (private http: Http) {}

    saveStudent(account: any): Observable<any> {
        var registerPath = 'api/students/register';
        return this.http.post(registerPath, account);
    }

    saveCompanyAdmin(account: any): Observable<any> {
        var registerPath = 'api/company-admins/register';
        return this.http.post(registerPath, account);
    }
}
