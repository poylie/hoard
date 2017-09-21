import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'jhi-document',
  templateUrl: './document.component.html',
  styleUrls: [
    'document.scss'
  ]
})
export class DocumentComponent implements OnInit {

  message: string;

  constructor() {
    this.message = 'DocumentComponent message';
  }

  ngOnInit() {
  }

}
