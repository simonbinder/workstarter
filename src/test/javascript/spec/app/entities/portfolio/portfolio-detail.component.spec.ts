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
import { PortfolioDetailComponent } from '../../../../../../main/webapp/app/entities/portfolio/portfolio-detail.component';
import { PortfolioService } from '../../../../../../main/webapp/app/entities/portfolio/portfolio.service';
import { Portfolio } from '../../../../../../main/webapp/app/entities/portfolio/portfolio.model';

describe('Component Tests', () => {

    describe('Portfolio Management Detail Component', () => {
        let comp: PortfolioDetailComponent;
        let fixture: ComponentFixture<PortfolioDetailComponent>;
        let service: PortfolioService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [PortfolioDetailComponent],
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
                    PortfolioService
                ]
            }).overrideComponent(PortfolioDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PortfolioDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PortfolioService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Portfolio(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.portfolio).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
