/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { HoardTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SalesOrderItemDetailComponent } from '../../../../../../main/webapp/app/entities/sales-order-item/sales-order-item-detail.component';
import { SalesOrderItemService } from '../../../../../../main/webapp/app/entities/sales-order-item/sales-order-item.service';
import { SalesOrderItem } from '../../../../../../main/webapp/app/entities/sales-order-item/sales-order-item.model';

describe('Component Tests', () => {

    describe('SalesOrderItem Management Detail Component', () => {
        let comp: SalesOrderItemDetailComponent;
        let fixture: ComponentFixture<SalesOrderItemDetailComponent>;
        let service: SalesOrderItemService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HoardTestModule],
                declarations: [SalesOrderItemDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SalesOrderItemService,
                    JhiEventManager
                ]
            }).overrideTemplate(SalesOrderItemDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SalesOrderItemDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SalesOrderItemService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new SalesOrderItem(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.salesOrderItem).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
