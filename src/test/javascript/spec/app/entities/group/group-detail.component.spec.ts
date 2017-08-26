/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { HoardTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { GroupDetailComponent } from '../../../../../../main/webapp/app/entities/group/group-detail.component';
import { GroupService } from '../../../../../../main/webapp/app/entities/group/group.service';
import { Group } from '../../../../../../main/webapp/app/entities/group/group.model';

describe('Component Tests', () => {

    describe('Group Management Detail Component', () => {
        let comp: GroupDetailComponent;
        let fixture: ComponentFixture<GroupDetailComponent>;
        let service: GroupService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HoardTestModule],
                declarations: [GroupDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    GroupService,
                    JhiEventManager
                ]
            }).overrideTemplate(GroupDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(GroupDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GroupService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Group(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.group).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
