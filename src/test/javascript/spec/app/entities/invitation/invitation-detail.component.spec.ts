/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { HoardTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { InvitationDetailComponent } from '../../../../../../main/webapp/app/entities/invitation/invitation-detail.component';
import { InvitationService } from '../../../../../../main/webapp/app/entities/invitation/invitation.service';
import { Invitation } from '../../../../../../main/webapp/app/entities/invitation/invitation.model';

describe('Component Tests', () => {

    describe('Invitation Management Detail Component', () => {
        let comp: InvitationDetailComponent;
        let fixture: ComponentFixture<InvitationDetailComponent>;
        let service: InvitationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HoardTestModule],
                declarations: [InvitationDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    InvitationService,
                    JhiEventManager
                ]
            }).overrideTemplate(InvitationDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(InvitationDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InvitationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Invitation(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.invitation).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
