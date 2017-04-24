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
import { CompanyDetailComponent } from '../../../../../../main/webapp/app/entities/company/company-detail.component';
import { CompanyService } from '../../../../../../main/webapp/app/entities/company/company.service';
import { Company } from '../../../../../../main/webapp/app/entities/company/company.model';

describe('Component Tests', () => {

    describe('Company Management Detail Component', () => {
        let comp: CompanyDetailComponent;
        let fixture: ComponentFixture<CompanyDetailComponent>;
        let service: CompanyService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [CompanyDetailComponent],
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
                    CompanyService
                ]
            }).overrideComponent(CompanyDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CompanyDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CompanyService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Company(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.company).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
