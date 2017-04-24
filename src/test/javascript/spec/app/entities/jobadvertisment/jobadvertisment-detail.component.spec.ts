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
import { JobadvertismentDetailComponent } from '../../../../../../main/webapp/app/entities/jobadvertisment/jobadvertisment-detail.component';
import { JobadvertismentService } from '../../../../../../main/webapp/app/entities/jobadvertisment/jobadvertisment.service';
import { Jobadvertisment } from '../../../../../../main/webapp/app/entities/jobadvertisment/jobadvertisment.model';

describe('Component Tests', () => {

    describe('Jobadvertisment Management Detail Component', () => {
        let comp: JobadvertismentDetailComponent;
        let fixture: ComponentFixture<JobadvertismentDetailComponent>;
        let service: JobadvertismentService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [JobadvertismentDetailComponent],
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
                    JobadvertismentService
                ]
            }).overrideComponent(JobadvertismentDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(JobadvertismentDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JobadvertismentService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Jobadvertisment(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.jobadvertisment).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
