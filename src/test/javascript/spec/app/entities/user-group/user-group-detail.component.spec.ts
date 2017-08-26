/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { HoardTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { UserGroupDetailComponent } from '../../../../../../main/webapp/app/entities/user-group/user-group-detail.component';
import { UserGroupService } from '../../../../../../main/webapp/app/entities/user-group/user-group.service';
import { UserGroup } from '../../../../../../main/webapp/app/entities/user-group/user-group.model';

describe('Component Tests', () => {

    describe('UserGroup Management Detail Component', () => {
        let comp: UserGroupDetailComponent;
        let fixture: ComponentFixture<UserGroupDetailComponent>;
        let service: UserGroupService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HoardTestModule],
                declarations: [UserGroupDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    UserGroupService,
                    JhiEventManager
                ]
            }).overrideTemplate(UserGroupDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(UserGroupDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserGroupService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new UserGroup(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.userGroup).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
