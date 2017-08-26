/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { HoardTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PurchaseOrderDetailComponent } from '../../../../../../main/webapp/app/entities/purchase-order/purchase-order-detail.component';
import { PurchaseOrderService } from '../../../../../../main/webapp/app/entities/purchase-order/purchase-order.service';
import { PurchaseOrder } from '../../../../../../main/webapp/app/entities/purchase-order/purchase-order.model';

describe('Component Tests', () => {

    describe('PurchaseOrder Management Detail Component', () => {
        let comp: PurchaseOrderDetailComponent;
        let fixture: ComponentFixture<PurchaseOrderDetailComponent>;
        let service: PurchaseOrderService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HoardTestModule],
                declarations: [PurchaseOrderDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PurchaseOrderService,
                    JhiEventManager
                ]
            }).overrideTemplate(PurchaseOrderDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PurchaseOrderDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PurchaseOrderService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new PurchaseOrder(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.purchaseOrder).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
