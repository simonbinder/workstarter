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
import { OfferingDetailComponent } from '../../../../../../main/webapp/app/entities/offering/offering-detail.component';
import { OfferingService } from '../../../../../../main/webapp/app/entities/offering/offering.service';
import { Offering } from '../../../../../../main/webapp/app/entities/offering/offering.model';

describe('Component Tests', () => {

    describe('Offering Management Detail Component', () => {
        let comp: OfferingDetailComponent;
        let fixture: ComponentFixture<OfferingDetailComponent>;
        let service: OfferingService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [OfferingDetailComponent],
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
                    OfferingService
                ]
            }).overrideComponent(OfferingDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(OfferingDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OfferingService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Offering(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.offering).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
