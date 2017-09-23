import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { Group } from './group.model';
import { GroupService } from './group.service';

@Component({
  selector: 'jhi-group-dashboard',
  templateUrl: './group-dashboard.component.html',
  styles: []
})
export class GroupDashboardComponent implements OnInit {

  private subscription: Subscription;

  constructor(
    private groupService: GroupService,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.subscription = this.route.params.subscribe((params) => {
      this.load(params['id']);
    });
  }

  load(id) {
    this.groupService.find(id).subscribe((group) => {
      this.groupService.setCurrentGroup(group);
    });
  }

}
