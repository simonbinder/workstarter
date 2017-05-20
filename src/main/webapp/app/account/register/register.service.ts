import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Rx';

@Injectable()
export class Register {

    constructor (private http: Http) {}

    save(account: any, role: any): Observable<any> {
        var registerPath = 'api/' + role + '/register';
        return this.http.post(registerPath, account);
    }
}
