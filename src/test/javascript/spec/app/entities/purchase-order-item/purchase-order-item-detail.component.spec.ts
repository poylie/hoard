/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { HoardTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PurchaseOrderItemDetailComponent } from '../../../../../../main/webapp/app/entities/purchase-order-item/purchase-order-item-detail.component';
import { PurchaseOrderItemService } from '../../../../../../main/webapp/app/entities/purchase-order-item/purchase-order-item.service';
import { PurchaseOrderItem } from '../../../../../../main/webapp/app/entities/purchase-order-item/purchase-order-item.model';

describe('Component Tests', () => {

    describe('PurchaseOrderItem Management Detail Component', () => {
        let comp: PurchaseOrderItemDetailComponent;
        let fixture: ComponentFixture<PurchaseOrderItemDetailComponent>;
        let service: PurchaseOrderItemService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HoardTestModule],
                declarations: [PurchaseOrderItemDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PurchaseOrderItemService,
                    JhiEventManager
                ]
            }).overrideTemplate(PurchaseOrderItemDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PurchaseOrderItemDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PurchaseOrderItemService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new PurchaseOrderItem(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.purchaseOrderItem).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
