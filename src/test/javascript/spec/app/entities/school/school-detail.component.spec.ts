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
import { SchoolDetailComponent } from '../../../../../../main/webapp/app/entities/school/school-detail.component';
import { SchoolService } from '../../../../../../main/webapp/app/entities/school/school.service';
import { School } from '../../../../../../main/webapp/app/entities/school/school.model';

describe('Component Tests', () => {

    describe('School Management Detail Component', () => {
        let comp: SchoolDetailComponent;
        let fixture: ComponentFixture<SchoolDetailComponent>;
        let service: SchoolService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [SchoolDetailComponent],
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
                    SchoolService
                ]
            }).overrideComponent(SchoolDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SchoolDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SchoolService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new School(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.school).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
