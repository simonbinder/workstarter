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
import { KeywordsDetailComponent } from '../../../../../../main/webapp/app/entities/keywords/keywords-detail.component';
import { KeywordsService } from '../../../../../../main/webapp/app/entities/keywords/keywords.service';
import { Keywords } from '../../../../../../main/webapp/app/entities/keywords/keywords.model';

describe('Component Tests', () => {

    describe('Keywords Management Detail Component', () => {
        let comp: KeywordsDetailComponent;
        let fixture: ComponentFixture<KeywordsDetailComponent>;
        let service: KeywordsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [KeywordsDetailComponent],
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
                    KeywordsService
                ]
            }).overrideComponent(KeywordsDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(KeywordsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(KeywordsService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Keywords(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.keywords).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
