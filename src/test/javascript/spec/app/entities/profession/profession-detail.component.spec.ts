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
import { ProfessionDetailComponent } from '../../../../../../main/webapp/app/entities/profession/profession-detail.component';
import { ProfessionService } from '../../../../../../main/webapp/app/entities/profession/profession.service';
import { Profession } from '../../../../../../main/webapp/app/entities/profession/profession.model';

describe('Component Tests', () => {

    describe('Profession Management Detail Component', () => {
        let comp: ProfessionDetailComponent;
        let fixture: ComponentFixture<ProfessionDetailComponent>;
        let service: ProfessionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [ProfessionDetailComponent],
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
                    ProfessionService
                ]
            }).overrideComponent(ProfessionDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProfessionDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProfessionService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Profession(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.profession).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
