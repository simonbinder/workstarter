import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Student } from './student.model';
import { Profession } from "../profession/profession.model";
import { School } from "../school/school.model";
import { Keywords } from "../keywords/keywords.model";

@Injectable()
export class StudentService {

    private resourceUrl = 'api/students';
    private resourceSearchUrl = 'api/_search/students';
    private resourceSearchAllUrl = 'api/_search/allaccounts';

    constructor(private http: Http) { }

    create(student: Student): Observable<Student> {
        let copy: Student = Object.assign({}, student);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(student: Student): Observable<Student> {
        let copy: Student = Object.assign({}, student);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    updateAccountInfo(student: Student): Observable<Response> {
        let copy: Student = Object.assign({}, student);
         return this.http.post(this.resourceUrl + '/account', copy);
    }

    ///////////////// Keywords //////////
     createKeywords(keyword: Keywords, studentId:number): Observable<Keywords> {
        let copy: Keywords = Object.assign({}, keyword);
        return this.http.post(`${this.resourceUrl}/${studentId}/${"keywords"}`, copy).map((res: Response) => {
            return res.json();
        });
    }

        deleteKeywords(studentId:number, keywordID:number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${studentId}/${"keywords"}/${keywordID}`);
    }


    ////////// Profession ///////
    updateProfession(profession: Profession, studentId:number, professionId:number): Observable<Profession> {
        let copy: Profession = Object.assign({}, profession);
        return this.http.put(`${this.resourceUrl}/${studentId}/${"profession"}/${professionId}`, copy).map((res: Response) => {
            return res.json();
        });
    }

    createProfession(profession: Profession, studentId:number): Observable<Profession> {
        let copy: Profession = Object.assign({}, profession);
        return this.http.post(`${this.resourceUrl}/${studentId}/${"profession"}`, copy).map((res: Response) => {
            return res.json();
        });
    }

    deleteProfession(profession: Profession, studentId:number, schoolId:number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${studentId}/${"profession"}/${schoolId}`);
    }

    ////////// School ///////
    updateSchool(school: School, studentId:number, schoolId:number): Observable<Profession> {
        let copy: School = Object.assign({}, school);
        return this.http.put(`${this.resourceUrl}/${studentId}/${"schools"}/${schoolId}`, copy).map((res: Response) => {
            return res.json();
        });
    }

    createSchool(school: School, studentId:number): Observable<Profession> {
        let copy: School = Object.assign({}, school);
        return this.http.post(`${this.resourceUrl}/${studentId}/${"schools"}`, copy).map((res: Response) => {
            return res.json();
        });
    }

    deleteSchool(school: School, studentId:number, schoolId:number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${studentId}/${"schools"}/${schoolId}`);
    }

    find(id: number): Observable<Student> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    uploadImage(id: number, file: FormData): Observable<Response>{
        return this.http.post(`${this.resourceUrl}/${id}/${"updatefile"}`, file);
    }

    query(req?: any): Observable<Response> {
        let options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
        ;
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<Response> {
        let options = this.createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
        ;
    }

    searchAll(req?: any): Observable<Response> {
        let options = this.createRequestOption(req);
        return this.http.get(this.resourceSearchAllUrl, options)
        ;
    }


    private createRequestOption(req?: any): BaseRequestOptions {
        let options: BaseRequestOptions = new BaseRequestOptions();
        if (req) {
            let params: URLSearchParams = new URLSearchParams();
            params.set('page', req.page);
            params.set('size', req.size);
            if (req.sort) {
                params.paramsMap.set('sort', req.sort);
            }
            params.set('query', req.query);

            options.search = params;
        }
        return options;
    }
}
