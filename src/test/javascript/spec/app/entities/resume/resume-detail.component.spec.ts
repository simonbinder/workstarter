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
import { ResumeDetailComponent } from '../../../../../../main/webapp/app/entities/resume/resume-detail.component';
import { ResumeService } from '../../../../../../main/webapp/app/entities/resume/resume.service';
import { Resume } from '../../../../../../main/webapp/app/entities/resume/resume.model';

describe('Component Tests', () => {

    describe('Resume Management Detail Component', () => {
        let comp: ResumeDetailComponent;
        let fixture: ComponentFixture<ResumeDetailComponent>;
        let service: ResumeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [ResumeDetailComponent],
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
                    ResumeService
                ]
            }).overrideComponent(ResumeDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ResumeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ResumeService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Resume(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.resume).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
