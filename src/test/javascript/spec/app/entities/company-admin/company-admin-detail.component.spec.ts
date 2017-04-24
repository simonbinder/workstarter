import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { MockBackend } from '@angular/http/testing';
import { Http, BaseRequestOptions } from '@angular/http';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils } from 'ng-jhipster';
import { JhiLanguageService } from 'ng-jhipster';
import { MockLanguageService } from '../../../helpers/mock-language.service';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CompanyAdminDetailComponent } from '../../../../../../main/webapp/app/entities/company-admin/company-admin-detail.component';
import { CompanyAdminService } from '../../../../../../main/webapp/app/entities/company-admin/company-admin.service';
import { CompanyAdmin } from '../../../../../../main/webapp/app/entities/company-admin/company-admin.model';

describe('Component Tests', () => {

    describe('CompanyAdmin Management Detail Component', () => {
        let comp: CompanyAdminDetailComponent;
        let fixture: ComponentFixture<CompanyAdminDetailComponent>;
        let service: CompanyAdminService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [CompanyAdminDetailComponent],
                providers: [
                    MockBackend,
                    BaseRequestOptions,
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    {
                        provide: Http,
                        useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
                            return new Http(backendInstance, defaultOptions);
                        },
                        deps: [MockBackend, BaseRequestOptions]
                    },
                    {
                        provide: JhiLanguageService,
                        useClass: MockLanguageService
                    },
                    CompanyAdminService
                ]
            }).overrideComponent(CompanyAdminDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CompanyAdminDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CompanyAdminService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new CompanyAdmin(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.companyAdmin).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
