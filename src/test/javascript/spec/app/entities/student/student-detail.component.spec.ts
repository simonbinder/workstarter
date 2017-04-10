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
import { StudentDetailComponent } from '../../../../../../main/webapp/app/entities/student/student-detail.component';
import { StudentService } from '../../../../../../main/webapp/app/entities/student/student.service';
import { Student } from '../../../../../../main/webapp/app/entities/student/student.model';

describe('Component Tests', () => {

    describe('Student Management Detail Component', () => {
        let comp: StudentDetailComponent;
        let fixture: ComponentFixture<StudentDetailComponent>;
        let service: StudentService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [StudentDetailComponent],
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
                    StudentService
                ]
            }).overrideComponent(StudentDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StudentDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StudentService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Student(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.student).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
