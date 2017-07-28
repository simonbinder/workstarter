import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { CompanyAdmin } from './company-admin.model';
import { Jobadvertisment } from "../jobadvertisment/jobadvertisment.model";
@Injectable()
export class CompanyAdminService {

    private resourceUrl = 'api/company-admins';
    private resourceSearchUrl = 'api/_search/company-admins';
    private companyUrl = 'api/companies';

    constructor(private http: Http) { }

    ////////// Jobadvertisments ///////
    updateProfession(job: Jobadvertisment, companyId:number, jobId:number): Observable<Jobadvertisment> {
        let copy: Jobadvertisment = Object.assign({}, job);
        return this.http.put(`${this.resourceUrl}/${companyId}/${"jobadvertisment"}/${jobId}`, copy).map((res: Response) => {
            return res.json();
        });
    }

    createProfession(job: Jobadvertisment, companyId:number): Observable<Jobadvertisment> {
        let copy: Jobadvertisment = Object.assign({}, job);
        return this.http.post(`${this.resourceUrl}/${companyId}/${"jobadvertisment"}`, copy).map((res: Response) => {
            return res.json();
        });
    }

    deleteProfession(job: Jobadvertisment, companyId:number, jobId:number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${companyId}/${"jobadvertisment"}/${jobId}`);
    }
    ///////////


    create(companyAdmin: CompanyAdmin): Observable<CompanyAdmin> {
        let copy: CompanyAdmin = Object.assign({}, companyAdmin);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(companyAdmin: CompanyAdmin): Observable<CompanyAdmin> {
        let copy: CompanyAdmin = Object.assign({}, companyAdmin);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    updateAccountInfo(companyAdmin: CompanyAdmin): Observable<Response> {
        let copy: CompanyAdmin = Object.assign({}, companyAdmin);
         return this.http.post(this.resourceUrl + '/account', copy);
    }

    find(id: number): Observable<CompanyAdmin> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
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
