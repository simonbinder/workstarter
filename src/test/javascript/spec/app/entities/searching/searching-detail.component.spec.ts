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
import { SearchingDetailComponent } from '../../../../../../main/webapp/app/entities/searching/searching-detail.component';
import { SearchingService } from '../../../../../../main/webapp/app/entities/searching/searching.service';
import { Searching } from '../../../../../../main/webapp/app/entities/searching/searching.model';

describe('Component Tests', () => {

    describe('Searching Management Detail Component', () => {
        let comp: SearchingDetailComponent;
        let fixture: ComponentFixture<SearchingDetailComponent>;
        let service: SearchingService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [SearchingDetailComponent],
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
                    SearchingService
                ]
            }).overrideComponent(SearchingDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SearchingDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SearchingService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Searching(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.searching).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
